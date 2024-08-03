package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 员工数据访问层接口，提供对员工数据的操作。
 */
@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工信息。
     *
     * @param username 员工的用户名
     * @return 查询到的员工对象
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 插入新的员工数据。
     * 在执行插入操作时，自动填充创建时间、更新时间、创建用户和更新用户字段。
     *
     * @param employee 要插入的员工对象
     */
    @Insert("insert into employee (name, username, password, phone, sex, id_number, create_time, update_time, create_user, update_user, status) " +
            "values " +
            "(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser},#{status})")
    @AutoFill(value = OperationType.INSERT)
    void insert(Employee employee);

    /**
     * 分页查询员工数据。
     *
     * @param employeePageQueryDTO 分页查询条件
     * @return 分页包装的员工数据
     */
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 更新员工数据。
     * 在执行更新操作时，自动填充更新时间和更新用户字段。
     *
     * @param employee 包含更新信息的员工对象
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Employee employee);

    /**
     * 根据员工ID查询员工信息。
     *
     * @param id 员工的唯一标识ID
     * @return 查询到的员工对象
     */
    @Select("select * from employee where id=#{id}")
    Employee getById(Long id);
}