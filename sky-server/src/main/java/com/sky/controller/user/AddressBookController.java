package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地址簿控制器，提供HTTP接口以实现地址簿的管理操作。
 */
@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "C端-地址簿接口")
public class AddressBookController {

    /**
     * 注入地址簿服务，用于执行具体的业务逻辑。
     */
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 查询当前登录用户的所有地址信息。
     * <p>
     * 此GET接口根据当前登录用户的ID查询所有关联的地址信息。
     *
     * @return 查询到的地址列表，封装在Result对象中。
     */
    @GetMapping("/list")
    @ApiOperation("查询当前登录用户的所有地址信息")
    public Result<List<AddressBook>> list() {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);
        return Result.success(list); // 返回查询到的地址列表
    }

    /**
     * 新增地址。
     * <p>
     * 此POST接口接收一个JSON格式的地址簿对象，并将其保存到数据库中。
     *
     * @param addressBook 包含新地址信息的地址簿对象。
     * @return 操作结果，包含成功或失败的状态信息。
     */
    @PostMapping
    @ApiOperation("新增地址")
    public Result save(@RequestBody AddressBook addressBook) {
        addressBookService.save(addressBook);
        return Result.success();
    }

    /**
     * 根据id查询地址。
     * <p>
     * 此GET接口根据传入的ID查询特定的地址信息。
     *
     * @param id 要查询的地址的ID。
     * @return 根据ID查询到的地址信息，封装在Result对象中。
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址")
    public Result<AddressBook> getById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 根据id修改地址。
     * <p>
     * 此PUT接口接收一个包含新地址信息的地址簿对象，根据对象中的ID进行更新。
     *
     * @param addressBook 包含更新地址信息的地址簿对象。
     * @return 操作结果，包含成功或失败的状态信息。
     */
    @PutMapping
    @ApiOperation("根据id修改地址")
    public Result update(@RequestBody AddressBook addressBook) {
        addressBookService.update(addressBook);
        return Result.success();
    }

    /**
     * 设置默认地址。
     * <p>
     * 此DELETE接口接收一个地址簿对象，将其设置为用户的默认收货地址。
     *
     * @param addressBook 包含要设置为默认地址信息的地址簿对象。
     * @return 操作结果，包含成功或失败的状态信息。
     */
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    /**
     * 根据id删除地址。
     * <p>
     * 此DELETE接口根据传入的ID删除对应的地址簿记录。
     *
     * @param id 要删除的地址的ID。
     * @return 操作结果，包含成功或失败的状态信息。
     */
    @DeleteMapping
    @ApiOperation("根据id删除地址")
    public Result deleteById(Long id) {
        addressBookService.deleteById(id);
        return Result.success();
    }

    /**
     * 查询默认地址。
     * <p>
     * 此GET接口查询并返回当前登录用户设置的默认收货地址。
     *
     * @return 默认地址信息，如果没有设置，默认地址则返回错误信息。
     */
    @GetMapping("/default")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> getDefault() {
        AddressBook addressBook = new AddressBook();
        addressBook.setIsDefault(1);
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);
        if (list != null && list.size() == 1) {
            return Result.success(list.get(0));
        }
        return Result.error("没有查询到默认地址");
    }
}