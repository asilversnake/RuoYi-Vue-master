package com.ruoyi.exam.service;

import java.util.List;
import com.ruoyi.exam.domain.ExamRecord;

/**
 * 考试记录Service接口
 * 
 * @author ruoyi
 * @date 2026-04-02
 */
public interface IExamRecordService 
{
    /**
     * 查询考试记录
     * 
     * @param id 考试记录主键
     * @return 考试记录
     */
    public ExamRecord selectExamRecordById(Long id);

    /**
     * 查询考试记录列表
     * 
     * @param examRecord 考试记录
     * @return 考试记录集合
     */
    public List<ExamRecord> selectExamRecordList(ExamRecord examRecord);

    /**
     * 新增考试记录
     * 
     * @param examRecord 考试记录
     * @return 结果
     */
    public int insertExamRecord(ExamRecord examRecord);

    /**
     * 修改考试记录
     * 
     * @param examRecord 考试记录
     * @return 结果
     */
    public int updateExamRecord(ExamRecord examRecord);

    /**
     * 批量删除考试记录
     * 
     * @param ids 需要删除的考试记录主键集合
     * @return 结果
     */
    public int deleteExamRecordByIds(Long[] ids);

    /**
     * 删除考试记录信息
     * 
     * @param id 考试记录主键
     * @return 结果
     */
    public int deleteExamRecordById(Long id);
}
