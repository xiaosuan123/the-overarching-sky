package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 地址簿服务实现类，提供地址簿管理的相关业务逻辑。
 */
@Service
@Slf4j
public class AddressBookServiceImpl implements AddressBookService {

    /**
     * 注入地址簿数据访问层，用于执行数据库操作。
     */
    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 根据条件查询地址簿列表。
     *
     * @param addressBook 包含查询条件的地址簿对象。
     * @return 地址簿列表。
     */
    @Override
    public List<AddressBook> list(AddressBook addressBook) {
        return addressBookMapper.list(addressBook);
    }

    /**
     * 保存地址簿记录，同时设置用户ID和默认地址标记。
     *
     * @param addressBook 要保存的地址簿记录对象。
     */
    @Override
    public void save(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId()); // 设置当前登录用户的ID
        addressBook.setIsDefault(0); // 设置为非默认地址
        addressBookMapper.insert(addressBook); // 插入新地址簿记录
    }

    /**
     * 根据ID获取单个地址簿记录。
     *
     * @param id 地址簿记录的ID。
     * @return 对应ID的地址簿对象。
     */
    @Override
    public AddressBook getById(Long id) {
        return addressBookMapper.getById(id);
    }

    /**
     * 更新地址簿记录。
     *
     * @param addressBook 包含更新信息的地址簿对象。
     */
    @Override
    public void update(AddressBook addressBook) {
        addressBookMapper.update(addressBook);
    }

    /**
     * 设置指定地址为默认地址，并更新其他地址的默认标记。
     *
     * @param addressBook 包含要设置为默认的地址信息的地址簿对象。
     */
    @Override
    public void setDefault(AddressBook addressBook) {
        addressBook.setIsDefault(0); // 先将所有地址的默认标记置为非默认
        addressBook.setUserId(BaseContext.getCurrentId()); // 设置当前登录用户的ID
        addressBookMapper.updateIsDefaultByUserId(addressBook); // 更新用户的所有地址

        addressBook.setIsDefault(1); // 将指定地址设置为默认
        addressBookMapper.update(addressBook); // 更新指定地址的默认标记
    }

    /**
     * 根据ID删除地址簿记录。
     *
     * @param id 要删除的地址簿记录的ID。
     */
    @Override
    public void deleteById(Long id) {
        addressBookMapper.deleteById(id);
    }
}