package com.mall.service;

import com.mall.entity.Goods;
import com.mall.vo.ResultVo;

import java.util.List;

public interface GoodsService {

    //获得首页商品
    public ResultVo getIndexGoods();

    //模糊查询商品,关键词查询
    public ResultVo getGoodsBykeyWord(String keyWord);

    //根据商品id查找商品
    public ResultVo getGoodsById(int goodsId);

    //获得全部商品
    public ResultVo getAllGoods();

    //添加商品
    public ResultVo addGoods(Goods goods);

    public ResultVo updateGoods(Goods goods);
}
