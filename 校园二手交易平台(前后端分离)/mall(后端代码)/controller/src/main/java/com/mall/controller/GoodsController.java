package com.mall.controller;

import com.mall.entity.Goods;
import com.mall.service.GoodsService;
import com.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/goods")
@CrossOrigin
@Slf4j
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    //获得首页展示商品
    @GetMapping("/indexlist")
    public ResultVo getIndexGoods(){
        return goodsService.getIndexGoods();
    }
    //模糊查询
    @GetMapping("/findlist")
    public ResultVo findGoods(String keyWord){
        return goodsService.getGoodsBykeyWord(keyWord);
    }

    //根据商品id查询
    @GetMapping("/goods")
    public ResultVo getGoodsById(int goodsId){

//      return goodsService.getGoodsById(Integer.parseInt(goodsId));
        return goodsService.getGoodsById(goodsId);
    }
    //获得全部商品
    @GetMapping("/list")
    public ResultVo getAllGoods(){
        return goodsService.getAllGoods();
    }

    //添加商品
    @PostMapping("/add")
    public ResultVo addGoods(@RequestBody Goods goods){
        return goodsService.addGoods(goods);
    }
    //修改商品上架、下架（由卖家使用）
    @PutMapping("/update")
    public ResultVo updateGoods(@RequestBody Goods goods){
        return goodsService.updateGoods(goods);
    }

}
