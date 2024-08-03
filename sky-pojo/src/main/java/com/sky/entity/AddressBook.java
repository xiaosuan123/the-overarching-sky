package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 地址簿实体类，用于存储和管理用户的地址信息。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 地址簿的唯一标识符。
     */
    private Long id;

    /**
     * 用户ID，与用户实体关联。
     */
    private Long userId;

    /**
     * 收货人的姓名。
     */
    private String consignee;

    /**
     * 收货人的手机号码。
     */
    private String phone;

    /**
     * 收货人的性别，其中"0"代表女性，"1"代表男性。
     */
    private String sex;

    /**
     * 省级区划编号。
     */
    private String provinceCode;

    /**
     * 省级名称。
     */
    private String provinceName;

    /**
     * 市级区划编号。
     */
    private String cityCode;

    /**
     * 市级名称。
     */
    private String cityName;

    /**
     * 区级区划编号。
     */
    private String districtCode;

    /**
     * 区级名称。
     */
    private String districtName;

    /**
     * 详细地址信息，包括街道、门牌号等。
     */
    private String detail;

    /**
     * 地址标签，用于描述地址的特征或用途。
     */
    private String label;

    /**
     * 是否设置为默认地址，其中0表示"否"，1表示"是"。
     */
    private Integer isDefault;
}