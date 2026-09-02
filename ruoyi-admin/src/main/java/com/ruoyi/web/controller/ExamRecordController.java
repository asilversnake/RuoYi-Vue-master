package com.ruoyi.exam.controller;

import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.Calendar; // 【新增】必须引入 Calendar 用于时间计算
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.exam.domain.ExamRecord;
import com.ruoyi.exam.service.IExamRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

@RestController
@RequestMapping("/exam/recording")
public class ExamRecordController extends BaseController
{
    @Autowired
    private IExamRecordService examRecordService;

    // ... (上面原有的 list, export, getInfo, add, edit, remove 方法保持不变) ...

    /**
     * 查询考试记录列表
     */
    @PreAuthorize("@ss.hasPermi('exam:recording:list')")
    @GetMapping("/list")
    public TableDataInfo list(ExamRecord examRecord)
    {
        startPage();
        List<ExamRecord> list = examRecordService.selectExamRecordList(examRecord);
        return getDataTable(list);
    }

    /**
     * 导出考试记录列表
     */
    @PreAuthorize("@ss.hasPermi('exam:recording:export')")
    @Log(title = "考试记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExamRecord examRecord)
    {
        List<ExamRecord> list = examRecordService.selectExamRecordList(examRecord);
        ExcelUtil<ExamRecord> util = new ExcelUtil<ExamRecord>(ExamRecord.class);
        util.exportExcel(response, list, "考试记录数据");
    }

    /**
     * 获取考试记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('exam:recording:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(examRecordService.selectExamRecordById(id));
    }

    /**
     * 新增考试记录
     */
    @PreAuthorize("@ss.hasPermi('exam:recording:add')")
    @Log(title = "考试记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExamRecord examRecord)
    {
        return toAjax(examRecordService.insertExamRecord(examRecord));
    }

    /**
     * 修改考试记录
     */
    @PreAuthorize("@ss.hasPermi('exam:recording:edit')")
    @Log(title = "考试记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExamRecord examRecord)
    {
        return toAjax(examRecordService.updateExamRecord(examRecord));
    }

    /**
     * 删除考试记录
     */
    @PreAuthorize("@ss.hasPermi('exam:recording:remove')")
    @Log(title = "考试记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(examRecordService.deleteExamRecordByIds(ids));
    }

    // ==========================================
    // 修改：供 Unity 调用的接口 (支持 30 分钟限时)
    // ==========================================
    /**
     * Unity 开始考试接口
     * 逻辑：
     * 1. 接收考试名称。
     * 2. 固定时长为 30 分钟。
     * 3. 计算结束时间 = 当前时间 + 30分钟。
     * 4. 将名称、时长、结束时间存入数据库。
     */
    @Anonymous
    @PostMapping("/startExam")
    public AjaxResult startExamFromUnity(@RequestBody Map<String, Object> data) // 使用 Object 兼容数字解析
    {
        try {
            // 1. 获取考试名称
            String examName = (String) data.get("examName");

            if (examName == null || examName.trim().isEmpty()) {
                return error("考试名称不能为空");
            }

            // 2. 定义固定时长：30 分钟
            int limitMinutes = 30;

            // 3. 计算结束时间 (当前时间 + 30分钟)
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date()); // 设置为当前时间
            calendar.add(Calendar.MINUTE, limitMinutes); // 加上 30 分钟
            Date endTime = calendar.getTime(); // 获取计算后的时间

            // 4. 创建记录对象并赋值
            ExamRecord record = new ExamRecord();
            record.setExamName(examName);
            record.setStartTime(new Date()); // 开始时间：现在

            // 【关键修改】设置时长和截止时间
            record.setDuration(limitMinutes); // 存入时长 30
            record.setEndTime(endTime);       // 存入具体的截止时间点

            // 5. 保存到数据库
            examRecordService.insertExamRecord(record);

            // 6. 返回结果给 Unity (可选：把结束时间也返回去，方便客户端校对)
            return success("考试已开始，限时30分钟");
        } catch (Exception e) {
            e.printStackTrace();
            return error("系统错误：" + e.getMessage());
        }
    }
}