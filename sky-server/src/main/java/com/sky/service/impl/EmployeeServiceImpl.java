package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee=new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        employee.setStatus(StatusConstant.ENABLE);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employeeMapper.insert(employee);
    }

    /**
     * 分页查询员工信息。
     *
     * @param employeePageQueryDTO 员工分页查询数据传输对象，包含分页和查询条件。
     * @return 分页结果，包含总记录数和当前页记录。
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        // 使用PageHelper插件启动分页功能
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        // 调用mapper层执行分页查询
        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
        // 从Page对象中获取总记录数和结果集
        long total = page.getTotal();
        List<Employee> records = page.getResult();
        // 封装分页结果并返回
        return new PageResult(total, records);
    }

    /**
     * 根据给定状态和员工ID启用或禁用员工账号。
     *
     * @param status 员工状态，通常为启用或禁用的标记值
     * @param id 员工的唯一标识ID
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        // 使用构建器模式创建员工对象，并设置状态和ID
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();
        // 调用数据访问层更新员工状态
        employeeMapper.update(employee);
    }

    /**
     * 根据员工ID获取员工详细信息，密码字段将被隐藏。
     *
     * @param id 员工的唯一标识ID
     * @return 员工对象，包含除密码外的所有信息
     */
    @Override
    public Employee getById(Long id) {
        // 从数据访问层获取员工对象
        Employee employee = employeeMapper.getById(id);
        // 为了安全，将密码设置为隐藏字符
        employee.setPassword("****");
        return employee;
    }

    /**
     * 更新员工信息。
     *
     * @param employeeDTO 包含更新信息的数据传输对象
     */
    @Override
    public void update(EmployeeDTO employeeDTO) {
        // 创建员工实体对象
        Employee employee = new Employee();
        // 使用 BeanUtils 工具类复制 DTO 对象的属性到员工实体对象
        BeanUtils.copyProperties(employeeDTO, employee);
        // 调用数据访问层更新员工信息
        employeeMapper.update(employee);
    }
}