package com.mall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.entity.Bill;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillDao extends BaseMapper<Bill> {

}
