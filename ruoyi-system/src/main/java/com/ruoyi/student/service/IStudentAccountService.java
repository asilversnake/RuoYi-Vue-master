package com.ruoyi.student.service;

import java.util.List;
import com.ruoyi.student.domain.StudentAccount;

/**
 * 学生账号Service接口
 * 
 * @author ruoyi
 * @date 2026-04-02
 */
public interface IStudentAccountService 
{
    /**
     * 查询学生账号
     * 
     * @param id 学生账号主键
     * @return 学生账号
     */
    public StudentAccount selectStudentAccountById(Long id);

    /**
     * 查询学生账号列表
     * 
     * @param studentAccount 学生账号
     * @return 学生账号集合
     */
    public List<StudentAccount> selectStudentAccountList(StudentAccount studentAccount);

    /**
     * 新增学生账号
     * 
     * @param studentAccount 学生账号
     * @return 结果
     */
    public int insertStudentAccount(StudentAccount studentAccount);

    /**
     * 修改学生账号
     * 
     * @param studentAccount 学生账号
     * @return 结果
     */
    public int updateStudentAccount(StudentAccount studentAccount);

    /**
     * 批量删除学生账号
     * 
     * @param ids 需要删除的学生账号主键集合
     * @return 结果
     */
    public int deleteStudentAccountByIds(Long[] ids);

    /**
     * 删除学生账号信息
     * 
     * @param id 学生账号主键
     * @return 结果
     */
    public int deleteStudentAccountById(Long id);
}
