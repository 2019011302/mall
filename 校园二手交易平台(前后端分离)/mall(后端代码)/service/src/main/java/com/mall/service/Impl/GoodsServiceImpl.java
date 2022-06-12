package com.mall.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall.dao.GoodsDao;
import com.mall.entity.Goods;
import com.mall.service.GoodsService;
import com.mall.vo.ResStatus;
import com.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    GoodsDao goodsDao;
    @Override
    public ResultVo getIndexGoods() {
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq("goods_status",2);
        List<Goods> list = goodsDao.selectList(wrapper);
        if(list == null){
            return new ResultVo(ResStatus.NO , "fail" , null);
        }
        return new ResultVo(ResStatus.OK , "success" , list);
    }

    @Override
    public ResultVo getGoodsBykeyWord(String keyWord) {
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.like("goods_name",keyWord)
                .gt("goods_status",0);
        List<Goods> list = goodsDao.selectList(wrapper);
        if(list == null){
            return new ResultVo(ResStatus.NO , "fail" , null);
        }
        return new ResultVo(ResStatus.OK , "success" , list);
    }

    @Override
    public ResultVo getGoodsById(int goodsId) {
        Goods goods = goodsDao.selectById(goodsId);
        if(goods == null){
            return new ResultVo(ResStatus.NO , "fail" , null);
        }
        return new ResultVo(ResStatus.OK , "success" , goods);
    }

    @Override
    public ResultVo getAllGoods() {
        List<Goods> list = goodsDao.selectList(null);
        if(list == null){
            return new ResultVo(ResStatus.NO , "fail" , null);
        }
        return new ResultVo(ResStatus.OK , "success" , list);
    }

    @Override
    public ResultVo addGoods(Goods goods) {
        goods.setGoodsBuyers(0);
        goods.setGoodsPraiserate(0);
        goods.setGoodsSales(0);
        goods.setGoodsScore(0);
        goods.setGoodsStatus(0);
        int i = goodsDao.insert(goods);
        if(i > 0){
            int img = goods.getGoodsId();
            goods.setGoodsImg(String.valueOf(img));
            goodsDao.updateById(goods);
            return new ResultVo(ResStatus.OK , "success" , img);

        }
        return new ResultVo(ResStatus.NO , "fail" , null);
    }

    @Override
    public ResultVo updateGoods(Goods goods) {
        int i = goodsDao.updateById(goods);
        if(i>0){
            return new ResultVo(ResStatus.OK , "success",null);
        }
        return new ResultVo(ResStatus.NO , "fail",null);
    }
}
