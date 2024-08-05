package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

/**
 * 地址簿服务接口，定义了地址簿相关的业务操作。
 */
public interface AddressBookService {

    /**
     * 根据条件查询地址簿列表。
     * <p>
     * 此方法允许根据传入的地址簿对象的条件来查询地址簿列表。
     *
     * @param addressBook 包含查询条件的地址簿对象。
     * @return 地址簿列表。
     */
    List<AddressBook> list(AddressBook addressBook);

    /**
     * 保存地址簿记录。
     * <p>
     * 此方法用于保存一个新的地址簿记录，包括插入新记录或更新现有记录。
     *
     * @param addressBook 要保存的地址簿记录对象。
     */
    void save(AddressBook addressBook);

    /**
     * 根据ID获取地址簿记录。
     * <p>
     * 通过地址簿记录的ID来获取具体的地址簿对象。
     *
     * @param id 地址簿记录的ID。
     * @return 对应ID的地址簿对象。
     */
    AddressBook getById(Long id);

    /**
     * 更新地址簿记录。
     * <p>
     * 此方法用于更新现有的地址簿记录。
     *
     * @param addressBook 包含更新信息的地址簿对象。
     */
    void update(AddressBook addressBook);

    /**
     * 设置默认地址。
     * <p>
     * 此方法用于将指定的地址设置为用户默认地址，可能涉及更新当前默认地址的标记。
     *
     * @param addressBook 包含要设置为默认的地址信息的地址簿对象。
     */
    void setDefault(AddressBook addressBook);

    /**
     * 根据ID删除地址簿记录。
     * <p>
     * 通过地址簿记录的ID来删除对应的地址簿对象。
     *
     * @param id 要删除的地址簿记录的ID。
     */
    void deleteById(Long id);
}