package com.mall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.entity.Admin;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminDao extends BaseMapper<Admin> {//继承BaseMapper，对数据库admin表的单表操作，不需要在mapper配置文件中写
    //若是需要做多表查询，则可以在这里定义接口。然后配置相应的mapper即可
}
