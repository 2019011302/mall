package com.mall.controller;

import com.mall.service.TariffService;
import com.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tariff")
@CrossOrigin
@Slf4j
public class TariffController {
    @Autowired
    TariffService tariffService;

    @GetMapping("/list")
    public ResultVo getAllTariffs() {
        return tariffService.getAllTariffs();
    }
}
