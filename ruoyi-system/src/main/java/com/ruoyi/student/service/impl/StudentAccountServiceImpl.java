package com.ruoyi.student.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // 1. 引入加密工具

import com.ruoyi.student.mapper.StudentAccountMapper;
import com.ruoyi.student.domain.StudentAccount;
import com.ruoyi.student.service.IStudentAccountService;

/**
 * 学生账号Service业务层处理
 *
 * @author ruoyi
 * @date 2026-04-02
 */
@Service
public class StudentAccountServiceImpl implements IStudentAccountService
{
    @Autowired
    private StudentAccountMapper studentAccountMapper;

    // 2. 注入加密工具类（若依框架自带，直接注入即可）
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 查询学生账号
     */
    @Override
    public StudentAccount selectStudentAccountById(Long id)
    {
        return studentAccountMapper.selectStudentAccountById(id);
    }

    /**
     * 查询学生账号列表
     */
    @Override
    public List<StudentAccount> selectStudentAccountList(StudentAccount studentAccount)
    {
        return studentAccountMapper.selectStudentAccountList(studentAccount);
    }

    /**
     * 新增学生账号
     */
    @Override
    public int insertStudentAccount(StudentAccount studentAccount)
    {
        // --- 查重逻辑 ---
        StudentAccount checkAccount = new StudentAccount();
        checkAccount.setUsername(studentAccount.getUsername());
        List<StudentAccount> list = studentAccountMapper.selectStudentAccountList(checkAccount);

        if (!list.isEmpty()) {
            throw new RuntimeException("该账号已被注册");
        }

        studentAccount.setCreateTime(DateUtils.getNowDate());

        // --- 密码加密逻辑 ---
        String rawPassword = studentAccount.getPassword();
        if (rawPassword != null && !rawPassword.isEmpty()) {
            // 将明文密码加密后存入
            studentAccount.setPassword(passwordEncoder.encode(rawPassword));
        }

        return studentAccountMapper.insertStudentAccount(studentAccount);
    }

    /**
     * 修改学生账号
     */
    @Override
    public int updateStudentAccount(StudentAccount studentAccount)
    {
        studentAccount.setUpdateTime(DateUtils.getNowDate());

        // 修改时的密码处理：只有输入了新密码才加密更新
        String rawPassword = studentAccount.getPassword();
        if (rawPassword != null && !rawPassword.isEmpty()) {
            studentAccount.setPassword(passwordEncoder.encode(rawPassword));
        } else {
            // 如果密码为空，设为null，配合Mapper的动态SQL，避免覆盖原密码
            studentAccount.setPassword(null);
        }

        return studentAccountMapper.updateStudentAccount(studentAccount);
    }

    /**
     * 批量删除学生账号
     */
    @Override
    public int deleteStudentAccountByIds(Long[] ids)
    {
        return studentAccountMapper.deleteStudentAccountByIds(ids);
    }

    /**
     * 删除学生账号信息
     */
    @Override
    public int deleteStudentAccountById(Long id)
    {
        return studentAccountMapper.deleteStudentAccountById(id);
    }

    // ==========================================
    // 【新增】登录验证方法
    // ==========================================
    /**
     * 登录验证
     * @param username 账号
     * @param rawPassword 明文密码
     * @return 验证结果信息
     */
    public String login(String username, String rawPassword) {
        // 1. 根据用户名从数据库查出账号信息
        StudentAccount queryAccount = new StudentAccount();
        queryAccount.setUsername(username);
        List<StudentAccount> list = studentAccountMapper.selectStudentAccountList(queryAccount);

        // 2. 判断账号是否存在
        if (list == null || list.isEmpty()) {
            return "账号不存在";
        }

        StudentAccount user = list.get(0);

        // 3. 【关键】比对密码
        // matches(明文密码, 数据库里的密文)
        // 如果匹配成功返回 true，否则返回 false
        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            return "登录成功";
        } else {
            return "密码错误";
        }
    }
}