package com.mall.controller;

import com.mall.entity.Refund;
import com.mall.service.RefundService;
import com.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/refund")
@CrossOrigin
@Slf4j
public class RefundController {

    @Autowired
    RefundService refundService;

    @PostMapping("/add")
    public ResultVo addRefund(@RequestBody Refund refund){
        return refundService.addRefund(refund);
    }

    @GetMapping("/one")
    public ResultVo getOneRefund(@RequestParam("orderItemId") int orderItemId){
        return refundService.getOneRefund(orderItemId);
    }

    @PutMapping("check")
    public ResultVo checkRefund(@RequestParam("refundId") int refundId ,
                                @RequestParam("other")String other , @RequestParam("bool")Boolean bool){
        //Refund refund = (Refund) map.get("refund");
        //Refund refund = JSONObject.parseObject(JSONObject.toJSONString(map.get("refund")), Refund.class);
       // Boolean bool = (Boolean) map.get("bool");
        return refundService.checkRefund(refundId , other , bool );
    }
}
