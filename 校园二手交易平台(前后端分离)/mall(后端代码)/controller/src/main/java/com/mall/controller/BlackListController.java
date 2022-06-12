package com.mall.controller;

import com.mall.entity.Black;
import com.mall.service.BlackListService;
import com.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blacklist")
@CrossOrigin
@Slf4j
public class BlackListController {
    @Autowired
    BlackListService blackListService;

    //添加黑名单
    @PostMapping("/add")
    public ResultVo addBlackList(@RequestParam("orderItemId") int orderItemId , @RequestParam("sellerId")int sellerId , @RequestParam("sellerType")String sellerType){
        return blackListService.addBlackList(orderItemId , sellerId , sellerType);
    }
    //查看黑名单
    @GetMapping("/select")
    public ResultVo selectBlackList(@RequestParam("sellerId") int sellerId  , @RequestParam("sellerType")String sellerType){
        return blackListService.selectBlackList(sellerId , sellerType);
    }
    //删除黑名单
    @DeleteMapping("/delete")
    public ResultVo deleteBlackList(@RequestParam("blackId") int blackId){
        return blackListService.deleteBlackList(blackId);
    }
}
