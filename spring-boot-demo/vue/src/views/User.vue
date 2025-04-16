<template>
  <div>
    <div style="margin: 10px 0">
      用户名：
      <el-input style="width: 200px" placeholder="请输入用户名" suffix-icon="el-icon-search" v-model="username"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="insertOrUpdateButton()">新增</el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定批量删除这些数据吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">批量删除</el-button>
      </el-popconfirm>
      <el-upload :action="serverIp + '/user/importExcel'" :show-file-list="false" accept="xlsx"
                 :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">导入</el-button>
      </el-upload>
      <el-button type="primary" @click="exportExcel" class="ml-5">导出</el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" v-if="false"></el-table-column>
      <el-table-column prop="username" label="用户名" width="120"></el-table-column>
      <el-table-column label="角色" width="120">
        <template slot-scope="scope">
          <el-tag :type="scope.row.id === 1 ? 'danger' : 'primary'"
                  v-for="item in scope.row.roles"
                  :key="item.id">{{ item.roleName }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="nickname" label="昵称" width="120"></el-table-column>
      <el-table-column prop="sex" label="性别" width="50">
        <template slot-scope="scope">
          <el-tag type="danger" v-if="scope.row.sex === 0">女</el-tag>
          <el-tag v-if="scope.row.sex === 1">男</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" width="120"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="140"></el-table-column>
      <el-table-column prop="updateTime" label="更新时间" width="140"></el-table-column>
      <el-table-column prop="lastLoginTime" label="最后登录时间" width="140"></el-table-column>
      <el-table-column label="禁用" width="70">
        <template slot-scope="scope">
          <el-switch
              v-if="scope.row.id !== 1"
              v-model="scope.row.isBan"
              active-color="#ff4949"
              inactive-color="#ccc"
              :active-value="1"
              :inactive-value="0"
              @change="isBan(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button v-if="scope.row.id !== 1" type="warning" class="mr-5" @click="shareRoles(scope.row)">分配角色
          </el-button>
          <el-button v-if="scope.row.id !== 1" type="success" @click="insertOrUpdateButton(scope.row)">编辑</el-button>
          <el-popconfirm
              v-if="scope.row.id !== 1"
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="page"
          :page-sizes="[10, 15, 20, 50,100]"
          :page-size="size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <!-- 分配角色 -->
    <el-dialog title="分配角色" :visible.sync="dialogRoleFormVisible" width="75vh">
      <el-select v-model="roleSelect" multiple clearable placeholder="请选择角色">
        <el-option
            v-for="item in roles"
            :key="item.id"
            :label="item.roleName"
            :value="item.id">
        </el-option>
      </el-select>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogRoleFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveUserRoles()">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 新增/更新用户 -->
    <el-dialog :title="dialogTitle + '用户信息'" :visible.sync="dialogFormVisible" width="75vh">
      <el-form label-width="100px" size="small">
        <el-form-item label="ID" v-if="false">
          <el-input v-model="form.id" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.sex" placeholder="请选择性别">
            <el-option
                v-for="item in sexSelect"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="禁用" v-if="form.id !== 1">
          <el-switch
              v-model="form.isBan"
              active-color="#ff4949"
              inactive-color="#ccc"
              :active-value="1"
              :inactive-value="0"></el-switch>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker v-model="form.createTime" type="datetime" disabled/>
        </el-form-item>
        <el-form-item label="更新时间">
          <el-date-picker v-model="form.updateTime" type="datetime" disabled/>
        </el-form-item>
        <el-form-item label="最后登录时间">
          <el-date-picker v-model="form.lastLoginTime" type="datetime" disabled/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

export default {
  name: "User",
  data() {
    return {
      serverIp: process.env.VUE_APP_BASE_URL,
      tableData: [],
      total: 0,
      page: 1,
      size: 10,
      username: "",
      email: "",
      form: {},
      dialogFormVisible: false,
      dialogRoleFormVisible: false,
      selectUserId: null,
      roleSelect: [],//选中的角色id
      dialogTitle: '', //新增/编辑的标题
      insertOrUpdate: 0, // 0表示编辑，1表示新增
      multipleSelection: [],
      roles: [],
      sexSelect: [
        {
          value: 0,
          label: '女'
        }, {
          value: 1,
          label: '男'
        }
      ]
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.doGet("/user/page", {
        page: this.page,
        size: this.size,
        username: this.username
      }).then(res => {
        this.tableData = res.records
        this.total = res.total
      })

      // TODO 获取权限，通过v-if来实现实现是否展示相应的按钮
      this.doGet("/role").then(res => {
        this.roles = res
      })
    },
    //分配角色
    shareRoles(row) {
      this.dialogRoleFormVisible = true;//显示对话框
      const role = row.roles
      this.selectUserId = row.id
      if (role === null || role === undefined) {//防止没有角色报错
        this.roleSelect = []
      } else {
        this.roleSelect = role.map(r => r.id)
      }
    },
    //保存分配的角色
    saveUserRoles() {
      this.doPut('/user/shareRoles/' + this.selectUserId, this.roleSelect).then(res => {
        this.dialogRoleFormVisible = false;
        this.$message.success("分配角色成功")
        this.load()
      })
    },
    //控制新增/编辑按钮
    insertOrUpdateButton(row) {
      //传row就是编辑，不传row就是新增
      this.dialogFormVisible = true
      if (row) {
        this.insertOrUpdate = 1
        this.dialogTitle = '编辑'
        this.form = row
      } else {
        this.insertOrUpdate = 0
        this.dialogTitle = '新增'
        this.form = {
          sex: 1,//默认为男
          isBan: 0,//默认不禁用
        }
      }
    },
    //编辑
    save() {
      if (this.insertOrUpdate === 0) {//新增
        this.form.pwd = '123456'
        this.doPost("/user", this.form).then(res => {
          this.$message.success("新增成功")
          this.dialogFormVisible = false
          this.load()
        })
      } else if (this.insertOrUpdate === 1) {//编辑
        this.doPut("/user", this.form).then(res => {
          this.$message.success("编辑成功")
          this.dialogFormVisible = false
          this.load()
        })
      }
    },
    del(id) {
      this.doDelete("/user/" + id).then(res => {
        this.$message.success("删除成功")
        this.load()
      })
    },
    //获取复选框的值把值赋值给multipleSelection
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    delBatch() {
      //获取handleSelectionChange的值
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      if (ids.length <= 0) {
        this.$message.error("请选择要删除的用户！")
      } else {
        this.doDelete("/user/deleteBatch", ids).then(res => {
          this.$message.success("批量删除成功")
          this.load()
        })
      }
    },
    reset() {
      this.username = ""
      this.load()
    },
    handleSizeChange(size) {
      this.size = size
      this.load()
    },
    handleCurrentChange(page) {
      this.page = page
      this.load()
    },
    exportExcel() {
      //导出指定的用户
      let userIds = this.multipleSelection.map(user => user.id)  // [{}, {}, {}] => [1,2,3]
      this.doPost('/user/exportExcel', userIds).then(res => {
        window.open(this.serverIp + `/` + res) //对应springboot CORSConfiguration配置的静态访问地址
      })
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    },
    isBan(row) {
      this.doPut('/user/isBan/' + row.id, null, {isBan: row.isBan}).then(res => {
        this.$message.success("操作成功")
        this.load()
      })
    }
  }
}
</script>