package com.ruoyi.student.controller;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.student.domain.StudentAccount;
import com.ruoyi.student.service.IStudentAccountService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import java.util.regex.Pattern; // 【新增】引入正则表达式包，用于校验密码

/**
 * 学生账号Controller
 *
 * @author ruoyi
 * @date 2026-04-02
 */
@RestController
@RequestMapping("/student/account")
public class StudentAccountController extends BaseController
{
    @Autowired
    private IStudentAccountService studentAccountService;

    // ... (你原本的列表、导出、增删改查代码保持不变) ...
    /**
     * 查询学生账号列表
     */
    @PreAuthorize("@ss.hasPermi('student:account:list')")
    @GetMapping("/list")
    public TableDataInfo list(StudentAccount studentAccount)
    {
        startPage();
        List<StudentAccount> list = studentAccountService.selectStudentAccountList(studentAccount);
        return getDataTable(list);
    }

    /**
     * 导出学生账号列表
     */
    @PreAuthorize("@ss.hasPermi('student:account:export')")
    @Log(title = "学生账号", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StudentAccount studentAccount)
    {
        List<StudentAccount> list = studentAccountService.selectStudentAccountList(studentAccount);
        ExcelUtil<StudentAccount> util = new ExcelUtil<StudentAccount>(StudentAccount.class);
        util.exportExcel(response, list, "学生账号数据");
    }

    /**
     * 获取学生账号详细信息
     */
    @PreAuthorize("@ss.hasPermi('student:account:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(studentAccountService.selectStudentAccountById(id));
    }

    /**
     * 新增学生账号
     */
    @PreAuthorize("@ss.hasPermi('student:account:add')")
    @Log(title = "学生账号", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StudentAccount studentAccount)
    {
        return toAjax(studentAccountService.insertStudentAccount(studentAccount));
    }

    /**
     * 修改学生账号
     */
    @PreAuthorize("@ss.hasPermi('student:account:edit')")
    @Log(title = "学生账号", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StudentAccount studentAccount)
    {
        return toAjax(studentAccountService.updateStudentAccount(studentAccount));
    }

    /**
     * 删除学生账号
     */
    @PreAuthorize("@ss.hasPermi('student:account:remove')")
    @Log(title = "学生账号", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(studentAccountService.deleteStudentAccountByIds(ids));
    }

    // ==========================================
    // 【新增】Unity 注册接口
    // ==========================================
    /**
     * Unity 注册接口
     * 路径: POST /student/account/register
     * 规则：密码8-15位，必须包含数字和字母
     */
    @Anonymous // 允许匿名访问
    @PostMapping("/register")
    public AjaxResult register(@RequestBody StudentAccount account)
    {
        String username = account.getUsername();
        String password = account.getPassword();

        // 1. 基础非空校验
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
        {
            return AjaxResult.error("账号或密码不能为空");
        }

        // 2. 检查账号是否已存在
        StudentAccount query = new StudentAccount();
        query.setUsername(username);
        List<StudentAccount> list = studentAccountService.selectStudentAccountList(query);
        if (list != null && !list.isEmpty())
        {
            return AjaxResult.error("该账号已被注册");
        }

        // 3. 密码强度校验 (正则表达式)
        // 规则：长度8-15位，必须包含至少一个数字和一个字母
        String regex = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,15}$";
        if (!Pattern.matches(regex, password))
        {
            return AjaxResult.error("密码格式错误：必须为8-15位，且包含数字和字母");
        }

        // 4. 执行注册
        account.setCreateBy("UnityUser"); // 设置创建人，避免空指针
        int rows = studentAccountService.insertStudentAccount(account);

        if (rows > 0)
        {
            return AjaxResult.success("注册成功");
        }
        else
        {
            return AjaxResult.error("注册失败");
        }
    }

    // ==========================================
    // 你原本的登录接口 (保持不变)
    // ==========================================
    @Anonymous
    @PostMapping("/login")
    public AjaxResult login(@RequestBody StudentAccount account)
    {
        String username = account.getUsername();
        String inputPassword = account.getPassword();

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(inputPassword))
        {
            return AjaxResult.error("账号或密码不能为空");
        }

        StudentAccount query = new StudentAccount();
        query.setUsername(username);
        List<StudentAccount> userList = studentAccountService.selectStudentAccountList(query);

        if (userList == null || userList.isEmpty())
        {
            return AjaxResult.error("账号不存在");
        }

        StudentAccount dbAccount = userList.get(0);

        if (!inputPassword.equals(dbAccount.getPassword()))
        {
            return AjaxResult.error("密码错误");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("id", dbAccount.getId());
        result.put("username", dbAccount.getUsername());
        result.put("createTime", dbAccount.getCreateTime());

        return AjaxResult.success("登录成功", result);
    }
}