<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="考试名称" prop="examName">
        <el-input
          v-model="queryParams.examName"
          placeholder="请输入考试名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <!-- 开启时间改为 datetime 类型，精确到分 -->
      <el-form-item label="开启时间" prop="startTime">
        <el-date-picker clearable
                        v-model="queryParams.startTime"
                        type="datetime"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        placeholder="请选择开启时间">
        </el-date-picker>
      </el-form-item>

      <!-- 修改点：考生名字 -> 考生ID -->
      <el-form-item label="考生ID" prop="studentId">
        <el-input
          v-model="queryParams.studentId"
          placeholder="请输入考生ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="考试分数" prop="score">
        <el-input
          v-model="queryParams.score"
          placeholder="请输入考试分数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="做对题目数" prop="correctCount">
        <el-input
          v-model="queryParams.correctCount"
          placeholder="请输入做对题目数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="做错题目数" prop="wrongCount">
        <el-input
          v-model="queryParams.wrongCount"
          placeholder="请输入做错题目数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <!-- 考试时长搜索框 -->
      <el-form-item label="考试时长" prop="duration">
        <el-input
          v-model="queryParams.duration"
          placeholder="请输入时长(分钟)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['exam:recording:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['exam:recording:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['exam:recording:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['exam:recording:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="recordingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="考试名称" align="center" prop="examName" />

      <!-- 列表开启时间显示精确到分 -->
      <el-table-column label="开启时间" align="center" prop="startTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>

      <!-- 考试时长列 -->
      <el-table-column label="考试时长" align="center" width="120">
        <template slot-scope="scope">
          <!-- 修复 duration 为空时不显示数字的问题，默认显示 0 -->
          <span>{{ scope.row.duration || 0 }} 分钟</span>
        </template>
      </el-table-column>

      <!-- 结束时间列 -->
      <el-table-column label="结束时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>

      <!-- 修改点：考生名字 -> 考生ID -->
      <el-table-column label="考生ID" align="center" prop="studentId" />

      <el-table-column label="考试分数" align="center" prop="score" />
      <el-table-column label="做对题目数" align="center" prop="correctCount" />
      <el-table-column label="做错题目数" align="center" prop="wrongCount" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['exam:recording:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['exam:recording:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改考试记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="考试名称" prop="examName">
          <el-input v-model="form.examName" placeholder="请输入考试名称" />
        </el-form-item>

        <!-- 弹窗内开启时间选择器改为 datetime -->
        <el-form-item label="开启时间" prop="startTime">
          <el-date-picker clearable
                          v-model="form.startTime"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="请选择开启时间">
          </el-date-picker>
        </el-form-item>

        <!-- 考试时长输入框 -->
        <el-form-item label="考试时长" prop="duration">
          <el-input v-model="form.duration" placeholder="请输入考试时长(分钟)" />
        </el-form-item>

        <!-- 结束时间输入框 -->
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker clearable
                          v-model="form.endTime"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="请选择结束时间">
          </el-date-picker>
        </el-form-item>

        <!-- 修改点：考生名字 -> 考生ID -->
        <el-form-item label="考生ID" prop="studentId">
          <el-input v-model="form.studentId" placeholder="请输入考生ID" />
        </el-form-item>

        <el-form-item label="考试分数" prop="score">
          <el-input v-model="form.score" placeholder="请输入考试分数" />
        </el-form-item>
        <el-form-item label="做对题目数" prop="correctCount">
          <el-input v-model="form.correctCount" placeholder="请输入做对题目数" />
        </el-form-item>
        <el-form-item label="做错题目数" prop="wrongCount">
          <el-input v-model="form.wrongCount" placeholder="请输入做错题目数" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRecording, getRecording, delRecording, addRecording, updateRecording } from "@/api/exam/recording"

export default {
  name: "Recording",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 考试记录表格数据
      recordingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        examName: null,
        startTime: null,
        studentId: null, // 修改点：studentName -> studentId
        score: null,
        correctCount: null,
        wrongCount: null,
        duration: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询考试记录列表 */
    getList() {
      this.loading = true
      listRecording(this.queryParams).then(response => {
        this.recordingList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        examName: null,
        startTime: null,
        studentId: null, // 修改点：studentName -> studentId
        score: null,
        correctCount: null,
        wrongCount: null,
        duration: null,
        endTime: null,
        createTime: null,
        updateTime: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加考试记录"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getRecording(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改考试记录"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateRecording(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addRecording(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除考试记录编号为"' + ids + '"的数据项？').then(function() {
        return delRecording(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.$modal.confirm('是否确认导出所有考试记录数据项？').then(() => {
        this.$download('exam/recording/export', {
          ...this.queryParams
        }, `recording_${new Date().getTime()}.xlsx`)
      }).catch(() => {})
    }
  }
}
</script>
