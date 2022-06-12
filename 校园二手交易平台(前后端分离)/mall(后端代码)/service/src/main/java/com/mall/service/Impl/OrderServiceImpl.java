package com.mall.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall.dao.*;
import com.mall.entity.*;
import com.mall.service.OrderService;
import com.mall.vo.ResStatus;
import com.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderDao orderDao;
    @Autowired
    OrderItemDao orderItemDao ;
    @Autowired
    UserDao userDao;
    @Autowired
    ShoppingCarDao shoppingCarDao;
    @Autowired
    GoodsDao goodsDao;
    @Autowired
    BillDao billDao;
    @Autowired
    SellerDao sellerDao;
    @Autowired
    TariffDao tariffDao;
    @Autowired
    BlackListDao blackListDao;

    //从购物车下单、支持多种商品下单
    //添加订单、订单项
    @Transactional
    @Override
    public ResultVo addOrder(int userId , double sumMoney , double realMoney , String carIdList){
        Date orderTime = new Date();
        //保留两位小数
        DecimalFormat df = new DecimalFormat("#.##");
        //流程：生成订单，添加订单、得到订单号，生成订单项，设置订单号，添加订单项，修改商品表改库存和销量，修改用户账户余额，删除购物车
//        1.获得用户信息
        User user = userDao.selectById(userId);
        if(user.getUserAccount() < realMoney){
            return new ResultVo(ResStatus.NO, "账户余额不足，请充值", null);
        }else if(isBlackList(userId , carIdList)){
            return new ResultVo(ResStatus.NO, "被卖家拉黑，无法购买", null);
        } else{
            //        2.设置订单信息
//        订单序号 用户id 下单时间 总额 实际付款 送货地址 收货号码
            Order order = new Order();
            order.setUserId(userId);
            order.setOrderTime(orderTime);
            order.setOrderSum(sumMoney);
            order.setOrderMoney(realMoney);
            order.setOrderAddress(user.getUserAddress());
            order.setOrderPhone(user.getUserPhone());
            order.setOrderName(user.getUserName());

//        3.添加订单
            try{
                 orderDao.insert(order);
//        4.获得待购买的购物车的信息
                String[] idList = carIdList.split(",");
                //用户积分（取整）
                int reward =(int) Math.floor(user.getUserRewardpoints());
//        List<ShoppingCar> ShoppingCarList = new ArrayList<>();
                for(int i = 0 ; i < idList.length ; i++){
                    ShoppingCar shoppingCar = shoppingCarDao.getShoppingCarByCarId(Integer.parseInt(idList[i]));
                    Goods goods = goodsDao.selectById(shoppingCar.getGoodsId());
                    //5.一条购物车记录可以生成一个订单项
                    OrderItem orderItem = new OrderItem();
                    //订单项序号 商品数量 实际付款 总额 交货方式 发货时间 收货时间 订单项状态 评论状态（0未评价，1已评价） 积分 商品号
                    //订单号
                    //商品数量
                    orderItem.setGoodsNumber(shoppingCar.getCarNumber());
                    //订单项金额
                    double orderitemSum = shoppingCar.getCarNumber() * shoppingCar.getGoodsPrice();
                    orderItem.setOrderItemSum(Double.parseDouble(df.format(orderitemSum)));
                    //计算实际付款金额
                    //积分后的付款金额
                    double orderitemRealMoney  = Double.parseDouble(df.format(orderitemSum -orderitemSum / sumMoney * reward /100));
                    orderItem.setOrderItemMoney(orderitemRealMoney);
                    //交货方式
                    orderItem.setOrderItemWay(goods.getGoodsWay());
                    //订单状态
                    orderItem.setOrderItemStatus("待发货");
                    //评论状态
                    orderItem.setOrderItemIsComment(0);
                    //订单项积分
                   // orderItem.setOrderItemRewardpoints(orderitemRealMoney);
                    //商品号
                    orderItem.setGoodsId(shoppingCar.getGoodsId());
                    //订单号
                    orderItem.setOrderId(order.getOrderId());
                    //6.添加订单项
                    orderItemDao.addOrderItem(orderItem);
                    //7.修改商品的库存和销量以及购买人数
                    int stock = goods.getGoodsStock();
                    int sales = goods.getGoodsSales();
                    int buyers = goods.getGoodsBuyers();
                    goods.setGoodsStock(stock - shoppingCar.getCarNumber());
                    goods.setGoodsSales(sales + shoppingCar.getCarNumber());
                    goods.setGoodsBuyers(buyers+1);
                    goodsDao.updateById(goods);

                    //8.删除购物车记录
                    shoppingCarDao.deleteShoppingCar(Integer.parseInt(idList[i]));

                    //修改卖家交易额
                    //个体用户
                    if("user".equals(goods.getSellerType())){
                        User seller = userDao.selectById(goods.getSellerId());
                        seller.setUserSale(Double.parseDouble(df.format(orderitemSum+seller.getUserSale())));
                        userDao.updateById(seller);
                    }else if("seller".equals(goods.getSellerType())){//商家用户
                        Seller seller = sellerDao.selectById(goods.getSellerId());
                        seller.setUserSale(Double.parseDouble(df.format(orderitemSum+seller.getUserSale())));
                        sellerDao.updateById(seller);
                    }

                }
                //9.修改用户账户余额,修改用户积分
                double account = Double.parseDouble(df.format(user.getUserAccount() - realMoney));
                int point = (int)((sumMoney - realMoney)*100);
                user.setUserAccount(account);
                double  surplus_point =  Double.parseDouble(df.format(user.getUserRewardpoints() - point));
                user.setUserRewardpoints(surplus_point);
                userDao.updateById(user);
                //10.添加用户账单
                Bill bill = new Bill();
                bill.setBillTime(orderTime);
                bill.setBillMoney(-realMoney);
                bill.setBillAccount(account);
                bill.setUserId(userId);
                bill.setUserType("user");
                String other = "订单id"+String.valueOf(order.getOrderId());
                bill.setBillOther(other);
                billDao.insert(bill);

            }catch (Exception e) {
                e.printStackTrace();
                return new ResultVo(ResStatus.NO, "下单失败", null);
            }
            return new ResultVo(ResStatus.OK , "下单成功" ,user);
        }
    }

        //根据用户名获得订单项
        @Override
    public ResultVo getOrderItemByUserId(int userId){
        List<OrderItem> list = orderItemDao.getOrderItemByUserId(userId);
        if(list==null){
            return new ResultVo(ResStatus.NO , "fail" , null);
        }
        return new ResultVo(ResStatus.OK , "success" , list);
    }

    @Override
    public ResultVo getOrderItemBySellerId(int sellerId) {
        List<OrderItem> list = orderItemDao.getOrderItemBySellerId(sellerId);
        if(list==null){
            return new ResultVo(ResStatus.NO , "fail" , null);
        }
        return new ResultVo(ResStatus.OK , "success" , list);
    }

    @Override
    public ResultVo updateOrderItem(OrderItem orderItem) {
        int i = orderItemDao.updateOrderItem(orderItem);
        if(i>0){//修改成功
            return new ResultVo(ResStatus.OK , "success" , null);
        }
        return new ResultVo(ResStatus.NO , "fail" , null);
    }

    @Override
    public ResultVo getOrderItemById(int orderItemId) {
        OrderItem item = orderItemDao.getOrderItemById(orderItemId);
        if(item != null){
            return new ResultVo(ResStatus.OK , "success" , item);
        }
        return new ResultVo(ResStatus.NO , "fail" , null);
    }

    //从商品详情页下单
    @Transactional
    @Override
    public ResultVo addOneOrder(int userId , int goodsId , int goodsNumber ,  double sumMoney ,  double realMoney ){

        Date orderTime = new Date();
        //保留两位小数
        DecimalFormat df = new DecimalFormat("#.##");
        //流程：生成订单，添加订单、得到订单号，生成订单项，设置订单号，添加订单项，修改商品表改库存和销量，修改用户账户余额，删除购物车
//        1.获得用户信息和商品信息
        User user = userDao.selectById(userId);
        Goods goods = goodsDao.selectById(goodsId);
        if(user.getUserAccount() < realMoney){
            return new ResultVo(ResStatus.NO, "账户余额不足，请充值", null);
        }else if(isBlack(userId , goods.getSellerId() , goods.getSellerType())){
            return new ResultVo(ResStatus.NO, "已被卖家拉黑，不能购买", null);
        }else{
            //        2.设置订单信息
//        订单序号 用户id 下单时间 总额 实际付款 送货地址 收货号码
            Order order = new Order();
            order.setUserId(userId);
            order.setOrderTime(orderTime);
            order.setOrderSum(sumMoney);
            order.setOrderMoney(realMoney);
            order.setOrderAddress(user.getUserAddress());
            order.setOrderPhone(user.getUserPhone());
            order.setOrderName(user.getUserName());
//        3.添加订单
            try{
                orderDao.insert(order);

//        4.添加订单项
                OrderItem orderItem = new OrderItem();
                //订单项序号 商品数量 实际付款 总额 交货方式 发货时间 收货时间 订单项状态 评论状态（0未评价，1已评价） 积分 商品号
                //订单号
                //商品数量
                orderItem.setGoodsNumber(goodsNumber);
                //订单项金额
                orderItem.setOrderItemSum(sumMoney);
                //实际付款金额
                //积分后的付款金额
                orderItem.setOrderItemMoney(realMoney);
                //交货方式
                orderItem.setOrderItemWay(goods.getGoodsWay());
                //订单状态
                orderItem.setOrderItemStatus("待发货");
                //评论状态
                orderItem.setOrderItemIsComment(0);
                //订单项积分
                //orderItem.setOrderItemRewardpoints(realMoney);
                //商品号
                orderItem.setGoodsId(goodsId);
                //订单号
                orderItem.setOrderId(order.getOrderId());
                //添加订单项
                orderItemDao.addOrderItem(orderItem);
                //5.修改商品的库存和销量以及购买人数
                int stock = goods.getGoodsStock();
                int sales = goods.getGoodsSales();
                int buyers = goods.getGoodsBuyers();
                goods.setGoodsStock(stock - goodsNumber);
                goods.setGoodsSales(sales + goodsNumber);
                goods.setGoodsBuyers(buyers+1);
                goodsDao.updateById(goods);
                //6.修改用户账户余额
                double account = Double.parseDouble(df.format(user.getUserAccount() - realMoney));
                int point = (int)((sumMoney - realMoney)*100);
                user.setUserAccount(account);
                double  surplus_point =  Double.parseDouble(df.format(user.getUserRewardpoints() - point));
                user.setUserRewardpoints(surplus_point);
                userDao.updateById(user);
                //7.添加用户账单
                Bill bill = new Bill();
                bill.setBillTime(orderTime);
                bill.setBillMoney(-realMoney);
                bill.setBillAccount(account);
                bill.setUserId(userId);
                String other = "订单id"+String.valueOf(order.getOrderId());
                bill.setBillOther(other);
                bill.setUserType("user");
                billDao.insert(bill);
                //修改卖家销售额
                //修改卖家交易额
                //个体用户
                if("user".equals(goods.getSellerType())){
                    User seller = userDao.selectById(goods.getSellerId());
                    seller.setUserSale(Double.parseDouble(df.format(sumMoney+seller.getUserSale())));
                    userDao.updateById(seller);
                }else if("seller".equals(goods.getSellerType())){//商家用户
                    Seller seller = sellerDao.selectById(goods.getSellerId());
                    seller.setUserSale(Double.parseDouble(df.format(sumMoney+seller.getUserSale())));
                    sellerDao.updateById(seller);
                }

            }catch (Exception e) {
                e.printStackTrace();
                return new ResultVo(ResStatus.NO, "下单失败", null);
            }
            return new ResultVo(ResStatus.OK , "下单成功" ,user);
        }
    }

    //收货:订单项id，买家id
    @Override
    public ResultVo takeDelivery(int orderItemId){
        OrderItem orderItem = orderItemDao.getOrderItemById(orderItemId);
        orderItem.setOrderItemStatus("交易完成");
        User user = userDao.getUserByOrderItemId(orderItemId);
        //1.修改订单项  2根据订单项信息获得积分 3.修改用户积分
        int i = orderItemDao.updateOrderItem(orderItem);
        if(i>0){//修改成功
//          1.根据订单项信息获得积分
            double item_reward = orderItem.getOrderItemMoney();
            //2.修改买家积分
           // User user = userDao.selectById(userId);
            double user_reward = user.getUserRewardpoints();
            //保留两位小数
            DecimalFormat df = new DecimalFormat("#.##");
            double all = Double.parseDouble(df.format(item_reward + user_reward));
            user.setUserRewardpoints(all);
            userDao.updateById(user);
            //卖家收到货款。个体用户、商家
            //0.找商品
            Goods goods = goodsDao.selectById(orderItem.getGoodsId());
            //1.找卖家
            if("user".equals(goods.getSellerType())){//商家需要交手续费，个人是不需要的
                User seller = userDao.selectById(goods.getSellerId());
                //2.根据等级查找费率表所对应的费率
//                QueryWrapper<Tariff> wrapper = new QueryWrapper();
//                wrapper.eq("tariff_scale", seller.getUserScale() );
//                Tariff tariff = tariffDao.selectOne(wrapper);
                double sellerMoney = orderItem.getOrderItemSum();
                double sum =  Double.parseDouble(df.format(seller.getUserAccount() + sellerMoney));
                //3.修改货款
                seller.setUserAccount(sum);
                userDao.updateById(seller);
                //4.生成账单
                Bill bill = new Bill();
                bill.setBillTime(new Date());
                bill.setBillMoney(sellerMoney);
                bill.setBillAccount(sum);
                bill.setUserId(seller.getUserId());
                bill.setUserType("user");
                String other = "订单项id"+String.valueOf(orderItemId);
                bill.setBillOther(other);
                billDao.insert(bill);
            }else if("seller".equals(goods.getSellerType())){//商家用户
                Seller seller = sellerDao.selectById(goods.getSellerId());
                //2.根据等级查找费率表所对应的费率
                QueryWrapper<Tariff> wrapper = new QueryWrapper();
                wrapper.eq("tariff_scale", seller.getSellerScale() );
                Tariff tariff = tariffDao.selectOne(wrapper);
                double sellerMoney = (1-tariff.getTariffRate())*orderItem.getOrderItemSum();
                double sum =  Double.parseDouble(df.format(seller.getUserAccount() + sellerMoney));
                //3.修改货款
                seller.setUserAccount(sum);
                sellerDao.updateById(seller);
                //4.生成账单
                Bill bill = new Bill();
                bill.setBillTime(new Date());
                bill.setBillMoney(sellerMoney);
                bill.setBillAccount(sum);
                bill.setUserId(seller.getSellerId());
                bill.setUserType("seller");
                String other = "订单项id"+String.valueOf(orderItemId);
                bill.setBillOther(other);
                billDao.insert(bill);
            }
            int j = userDao.updateById(user);
            if(j>0){
                return new ResultVo(ResStatus.OK , "success" , user);
            }
            else{
                return new ResultVo(ResStatus.NO , "fail" , null);
            }
        }
        return new ResultVo(ResStatus.NO , "fail" , null);
    }
    //判断用户是否在黑名单上，对于单个商品下单
    public Boolean isBlack(int userId , int sellerId , String sellerType){
        QueryWrapper<Black> wrapper = new QueryWrapper<>();
        Map<String , Object> map = new HashMap<>();
        map.put("seller_id",sellerId);
        map.put("seller_type" ,sellerType );
        map.put("user_id" , userId);
        wrapper.allEq(map);
        Black black = blackListDao.selectOne(wrapper);
        if(black != null){
            return true;//表示在黑名单上
        }
        return false;//表示不在黑名单上
    }

    //判断用户是否在黑名单上，对于多个商品下单
    public Boolean isBlackList(int userId , String carIdList){
        String[] idList = carIdList.split(",");
        //判断黑名单
        for(int i = 0 ; i < idList.length ; i++){
            //1.获得购物车
            ShoppingCar shoppingCar = shoppingCarDao.getShoppingCarByCarId(Integer.parseInt(idList[i]));
            //2.获得商品
            Goods goods = goodsDao.selectById(shoppingCar.getGoodsId());
            //3.获得商家id和卖家类型
            int sellerId = goods.getSellerId();
            String sellerType = goods.getSellerType();
            //4.判断是否在黑名单上
            if(isBlack(userId , sellerId , sellerType)){
                return true;
            }
        }
        return false;
    }
}
