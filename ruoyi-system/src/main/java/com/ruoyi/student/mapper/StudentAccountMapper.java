package com.ruoyi.student.mapper;

import java.util.List;
import com.ruoyi.student.domain.StudentAccount;

/**
 * 学生账号Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-02
 */
public interface StudentAccountMapper 
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
     * 删除学生账号
     * 
     * @param id 学生账号主键
     * @return 结果
     */
    public int deleteStudentAccountById(Long id);

    /**
     * 批量删除学生账号
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStudentAccountByIds(Long[] ids);
}
