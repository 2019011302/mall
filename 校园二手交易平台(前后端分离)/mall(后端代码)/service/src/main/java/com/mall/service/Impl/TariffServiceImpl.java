package com.mall.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall.dao.TariffDao;
import com.mall.entity.Goods;
import com.mall.entity.Tariff;
import com.mall.service.TariffService;
import com.mall.vo.ResStatus;
import com.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TariffServiceImpl implements TariffService {
    @Autowired
    TariffDao tariffDao;
    @Override
    public ResultVo getAllTariffs() {
        List<Tariff> list = tariffDao.selectList(null);
        if(list == null){
            return new ResultVo(ResStatus.NO , "fail" , null);
        }
        return new ResultVo(ResStatus.OK , "success" , list);
    }
}
