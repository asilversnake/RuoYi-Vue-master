package com.ruoyi.exam.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 考试记录对象 exam_record
 *
 * @author ruoyi
 * @date 2026-04-02
 */
public class ExamRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 考试名称 */
    @Excel(name = "考试名称")
    private String examName;

    /** 开启时间 */
    // 【修改点】加上了 HH:mm:ss，确保时间不会丢失
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开启时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 考生ID */
    @Excel(name = "考生ID")
    private String studentId;

    /** 考试分数 */
    @Excel(name = "考试分数")
    private Long score;

    /** 做对题目数 */
    @Excel(name = "做对题目数")
    private Long correctCount;

    /** 做错题目数 */
    @Excel(name = "做错题目数")
    private Long wrongCount;

    // ==========================================
    // 【新增字段】考试时长和结束时间
    // ==========================================

    /** 考试时长(分钟) */
    @Excel(name = "考试时长")
    private Integer duration;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    // ==========================================
    // Getter & Setter 方法
    // ==========================================

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setExamName(String examName)
    {
        this.examName = examName;
    }

    public String getExamName()
    {
        return examName;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStudentId(String studentId)
    {
        this.studentId = studentId;
    }

    public String getStudentId()
    {
        return studentId;
    }

    public void setScore(Long score)
    {
        this.score = score;
    }

    public Long getScore()
    {
        return score;
    }

    public void setCorrectCount(Long correctCount)
    {
        this.correctCount = correctCount;
    }

    public Long getCorrectCount()
    {
        return correctCount;
    }

    public void setWrongCount(Long wrongCount)
    {
        this.wrongCount = wrongCount;
    }

    public Long getWrongCount()
    {
        return wrongCount;
    }

    public void setDuration(Integer duration)
    {
        this.duration = duration;
    }

    public Integer getDuration()
    {
        return duration;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("examName", getExamName())
                .append("startTime", getStartTime())
                .append("studentId", getStudentId())
                .append("score", getScore())
                .append("correctCount", getCorrectCount())
                .append("wrongCount", getWrongCount())
                .append("duration", getDuration())
                .append("endTime", getEndTime())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}