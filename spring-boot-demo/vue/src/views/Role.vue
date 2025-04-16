<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入角色名称" suffix-icon="el-icon-search" v-model="roleName"></el-input>
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
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" v-if="false" width="80"></el-table-column>
      <el-table-column prop="roleName" label="名称" width="150"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="140"></el-table-column>
      <el-table-column prop="updateTime" label="更新时间" width="140"></el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button v-if="scope.row.id !== 1" type="info" @click="selectMenu(scope.row)">分配菜单权限</el-button>
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
          :page-sizes="[10, 15, 20, 50, 100]"
          :page-size="size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog :title="dialogTitle + '角色信息'" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="80px" size="small">
        <el-form-item label="名称">
          <el-input v-model="form.roleName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker v-model="form.createTime" type="datetime" disabled/>
        </el-form-item>
        <el-form-item label="更新时间">
          <el-date-picker v-model="form.updateTime" type="datetime" disabled/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="菜单分配" :visible.sync="menuDialogVis" width="30%">
      <el-tree
          :props="props"
          :data="menuData"
          show-checkbox
          node-key="id"
          ref="tree"
          :default-expanded-keys="expends"
          :default-checked-keys="checks">
         <span class="custom-tree-node" slot-scope="{ node, data }">
            <span>{{ data.menuName }}</span>
         </span>
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="menuDialogVis = false">取 消</el-button>
        <el-button type="primary" @click="saveRoleMenu">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Role",
  data() {
    return {
      tableData: [],
      total: 0,
      page: 1,
      size: 10,
      roleName: "",
      form: {},
      dialogFormVisible: false,
      dialogTitle: '', //新增/编辑的标题
      insertOrUpdate: 0, // 0表示编辑，1表示新增
      menuDialogVis: false,
      multipleSelection: [],
      menuData: [],
      props: {
        children: 'childAuthorize',//后端传过来的子节点名称
        label: 'menuName',//显示给用户看的子节点名称
      },
      expends: [],
      checks: [],
      roleId: 0,
      allMenuIds:[],
      ids: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.doGet("/role/page", {
        page: this.page,
        size: this.size,
        roleName: this.roleName,
      }).then(res => {
        this.tableData = res.records
        this.total = res.total
      })

      this.doGet("/role/menu/1").then(res => {
        this.allMenuIds = res.authorizeIds
        this.menuData = res.authorizes //选中角色的树形数组
        // 把后台返回的菜单数据处理成 id数组
        this.expends = this.menuData.map(v => v.id)
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
        this.form = {}
      }
    },
    //新增或编辑
    save() {
      if (this.insertOrUpdate === 0) {//新增
        this.doPost("/role", this.form).then(res => {
          this.$message.success("新增成功")
          this.dialogFormVisible = false
          this.load()
        })
      } else if (this.insertOrUpdate === 1) {//编辑
        this.doPut("/role", this.form).then(res => {
          this.$message.success("编辑成功")
          this.dialogFormVisible = false
          this.load()
        })
      }
    },
    saveRoleMenu() {
      //巨坑：保存的时候要把父节点也保存到数据库，否则element回显数据的时候会错误！！！
      this.doPost("/role/roleMenu/" + this.roleId, this.$refs.tree.getCheckedNodes(false,true).map(node => node.id)).then(res => {
        this.$message.success("绑定成功")
        this.menuDialogVis = false
      })
    },
    del(id) {
      this.doDelete("/role/" + id).then(res => {
        this.$message.success("删除成功")
        this.load()
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      if (ids.length <= 0){
        this.$message.error("请选择要删除的角色！")
      }else {
        this.doDelete("/role/deleteBatch", ids).then(res => {
          this.$message.success("批量删除成功")
          this.load()
        })
      }
    },
    reset() {
      this.roleName = ""
      this.load()
    },
    handleSizeChange(size) {
      console.log(size)
      this.size = size
      this.load()
    },
    handleCurrentChange(page) {
      console.log(page)
      this.page = page
      this.load()
    },
    async selectMenu(role) {
      this.roleId = role.id
      // 获取选中的角色的权限
      this.doGet("/role/menu/" + this.roleId).then(res => {
        this.checks = res.authorizeIds //获取选中的角色的权限id
        this.allMenuIds.forEach(id => {
          if (!this.checks.includes(id)) {
            // 可能会报错：Uncaught (in promise) TypeError: Cannot read properties of undefined (reading 'setChecked')
            this.$nextTick(() => {
              this.$refs.tree.setChecked(id, false)
            })
          }
        })
        this.menuDialogVis = true
      })
    },
  }
}
</script>


<style>
</style>
