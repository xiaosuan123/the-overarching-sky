package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 地址簿数据访问接口。
 * 提供了对地址簿数据的增删改查操作。
 */
@Mapper
public interface AddressBookMapper {

    /**
     * 根据条件查询地址簿列表。
     *
     * @param addressBook 包含查询条件的地址簿对象。
     * @return 地址簿列表。
     */
    List<AddressBook> list(AddressBook addressBook);

    /**
     * 插入新的地址簿记录。
     *
     * @param addressBook 包含新记录信息的地址簿对象。
     */
    @Insert("insert into address_book" +
            "        (user_id, consignee, phone, sex, province_code, province_name, city_code, city_name, district_code," +
            "         district_name, detail, label, is_default)" +
            "        values (#{userId}, #{consignee}, #{phone}, #{sex}, #{provinceCode}, #{provinceName}, #{cityCode}, #{cityName}," +
            "                #{districtCode}, #{districtName}, #{detail}, #{label}, #{isDefault})")
    void insert(AddressBook addressBook);

    /**
     * 根据ID查询地址簿记录。
     *
     * @param id 地址簿记录的ID。
     * @return 对应ID的地址簿对象。
     */
    @Select("select *from address_book where id=#{id}")
    AddressBook getById(Long id);

    /**
     * 更新地址簿记录。
     *
     * @param addressBook 包含更新信息的地址簿对象。
     */
    void update(AddressBook addressBook);

    /**
     * 根据用户ID更新默认地址标记。
     *
     * @param addressBook 包含新默认地址标记和用户ID的地址簿对象。
     */
    @Update("update address_book set is_default=#{isDefault} where user_id=#{userId}")
    void updateIsDefaultByUserId(AddressBook addressBook);

    /**
     * 根据ID删除地址簿记录。
     *
     * @param id 要删除的地址簿记录的ID。
     */
    @Delete("delete from address_book where id = #{id}")
    void deleteById(Long id);


}