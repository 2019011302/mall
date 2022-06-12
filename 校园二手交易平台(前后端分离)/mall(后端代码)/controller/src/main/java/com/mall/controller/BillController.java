package com.mall.controller;

import com.mall.entity.Bill;
import com.mall.service.BillService;
import com.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/bill")
@CrossOrigin
@Slf4j
public class BillController {

    @Autowired
    BillService billService;
    //添加账单
    @PostMapping("/add")
    public ResultVo addBills(@RequestBody  Bill bill){

        return billService.addBills(bill);
    }

    //查询用户账单
    @GetMapping("/list")
    public ResultVo getBillByUserId (int userId , String userType){

        return billService.getBillByUserId(userId , userType);
    }
}
