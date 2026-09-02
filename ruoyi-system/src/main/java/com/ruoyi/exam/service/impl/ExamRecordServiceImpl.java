package com.ruoyi.exam.service.impl;

import java.util.Calendar;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.exam.mapper.ExamRecordMapper;
import com.ruoyi.exam.domain.ExamRecord;
import com.ruoyi.exam.service.IExamRecordService;

/**
 * 考试记录Service业务层处理
 *
 * @author ruoyi
 * @date 2026-04-02
 */
@Service
public class ExamRecordServiceImpl implements IExamRecordService
{
    @Autowired
    private ExamRecordMapper examRecordMapper;

    /**
     * 查询考试记录
     */
    @Override
    public ExamRecord selectExamRecordById(Long id)
    {
        return examRecordMapper.selectExamRecordById(id);
    }

    /**
     * 查询考试记录列表
     */
    @Override
    public List<ExamRecord> selectExamRecordList(ExamRecord examRecord)
    {
        return examRecordMapper.selectExamRecordList(examRecord);
    }

    /**
     * 新增考试记录
     */
    @Override
    public int insertExamRecord(ExamRecord examRecord)
    {
        // ==========================================
        // 【新增逻辑】自动计算结束时间
        // 规则：结束时间 = 开启时间 + 考试时长(分钟)
        // ==========================================
        if (examRecord.getStartTime() != null && examRecord.getDuration() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(examRecord.getStartTime());
            // 加上时长（分钟）
            calendar.add(Calendar.MINUTE, examRecord.getDuration());
            // 设置计算后的时间
            examRecord.setEndTime(calendar.getTime());
        }

        examRecord.setCreateTime(DateUtils.getNowDate());
        return examRecordMapper.insertExamRecord(examRecord);
    }

    /**
     * 修改考试记录
     */
    @Override
    public int updateExamRecord(ExamRecord examRecord)
    {
        // ==========================================
        // 【新增逻辑】修改时也自动计算结束时间
        // ==========================================
        if (examRecord.getStartTime() != null && examRecord.getDuration() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(examRecord.getStartTime());
            calendar.add(Calendar.MINUTE, examRecord.getDuration());
            examRecord.setEndTime(calendar.getTime());
        }

        examRecord.setUpdateTime(DateUtils.getNowDate());
        return examRecordMapper.updateExamRecord(examRecord);
    }

    /**
     * 批量删除考试记录
     */
    @Override
    public int deleteExamRecordByIds(Long[] ids)
    {
        return examRecordMapper.deleteExamRecordByIds(ids);
    }

    /**
     * 删除考试记录信息
     */
    @Override
    public int deleteExamRecordById(Long id)
    {
        return examRecordMapper.deleteExamRecordById(id);
    }
}