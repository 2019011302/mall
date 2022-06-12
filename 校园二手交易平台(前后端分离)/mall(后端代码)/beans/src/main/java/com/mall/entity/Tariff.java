package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("tariffs")
public class Tariff {
    @TableId(value = "tariff_id" , type = IdType.AUTO)
    private Integer tariffId;
    private Integer tariffScale;
    private double tariffRate;

}
