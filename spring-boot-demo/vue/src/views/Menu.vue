<template>
  <div>
    <div class="block">
      <el-input placeholder="输入关键字进行过滤" v-model="filterMenu"></el-input>
      <el-tree
          :data="tableData"
          :props="authorizeTreeProps"
          node-key="id"
          highlight-current
          default-expand-all
          :expand-on-click-node="false"
          :filter-node-method="filterNode"
          ref="tree">
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}【{{ nodeTypeFormatter[data.nodeType - 1].label }}】</span>
        <span>
          <el-button
              v-if="data.nodeType !== 3"
              type="text"
              size="mini"
              @click="() => insertOrUpdateButton(null,data)">
            新增
          </el-button>
          <el-button
              type="text"
              size="mini"
              @click="() => insertOrUpdateButton(data,null)">
            编辑
          </el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              :title="'您确定删除【'+data.menuName+'】吗?'"
              @confirm="del(data.id)"
          >
            <el-button size="mini" type="text" slot="reference">删除</el-button>
          </el-popconfirm>
        </span>
      </span>
      </el-tree>
    </div>

    <el-dialog :title="dialogTitle + '菜单信息'" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="120px" size="small">
        <el-form-item label="新增类型" v-if="insertOrUpdate === 0">
          <template>
            <el-select v-model="insertType" placeholder="请选择新增类型" @change="(value) => insertTypeChange(value)">
              <el-option
                  v-for="item in insertTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </template>
        </el-form-item>
        <el-form-item label="节点类型">
          <template>
            <el-select v-model="form.nodeType" placeholder="请选择节点类型">
              <el-option
                  v-for="item in nodeTypeFormatter"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </template>
        </el-form-item>
        <el-form-item label="菜单名称">
          <el-input v-model.trim="form.menuName" autocomplete="off"></el-input>
        </el-form-item>
        <!-- 菜单才显示 -->
        <el-form-item label="前端请求路径" v-if="form.nodeType === 2">
          <el-input v-model.trim="form.path" autocomplete="off"></el-input>
        </el-form-item>
        <!-- 菜单才显示 -->
        <el-form-item label="Vue对应的页面" v-if="form.nodeType === 2">
          <el-input v-model.trim="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <!-- 按钮才显示权限 -->
        <el-form-item label="权限名称" v-if="form.nodeType === 3">
          <el-input v-model.trim="form.authorizeName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="层级">
          <el-input-number v-model="form.level" controls-position="right" placeholder="请输入层级" :min="1" :max="10"></el-input-number>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" controls-position="right" placeholder="请输入排序" :min="1" :max="9999"></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Menu",
  data() {
    return {
      filterMenu:'', //过滤节点
      tableData: [],
      form: {},
      formCopy: {}, //复制表单内容
      dialogFormVisible: false,
      authorizeTreeProps: {
        label: 'menuName',
        children: 'childAuthorize'
      },
      dialogTitle: '', //新增/编辑的标题
      insertOrUpdate: 0, // 0表示编辑，1表示新增
      nodeTypeFormatter: [{
        value:1,
        label: "文件夹"
      },{
        value:2,
        label: "菜单"
      },{
        value:3,
        label: "按钮"
      }],
      insertType: 0,
      insertTypeOptions: [{
        value: 0,
        label: '新增子级'
      }, {
        value: 1,
        label: '新增同级'
      }],
    }
  },
  created() {
    this.load()
  },
  //监听节点
  watch: {
    filterMenu(val) {
      this.$refs.tree.filter(val);
    }
  },
  methods: {
    load() {
      this.doGet("/authorize/tree").then(res => {
        this.tableData = res
      })
    },
    //过滤节点
    filterNode(value, data) {
      if (!value) return true;
      return data.menuName.indexOf(value) !== -1;
    },
    //控制新增/编辑按钮
    insertOrUpdateButton(row, rowCopy) {
      //传row就是编辑，不传row就是新增
      this.dialogFormVisible = true
      if (row) {
        this.insertOrUpdate = 1
        this.dialogTitle = '编辑'
        this.form = row
        this.formCopy = {}
      } else {
        this.insertOrUpdate = 0
        this.dialogTitle = '新增'
        this.form = {
          pid:rowCopy.id, //默认新增子级，因此新增的时候需要先给pid赋值，防止pid为空
          nodeType:2, //默认新增菜单
          level:1, //默认层级
          sort:1, //默认排序
        }
        this.formCopy = rowCopy //复制表单内容
      }
    },
    save() {
      if (this.insertOrUpdate === 0) {//新增
        this.doPost("/authorize", this.form).then(res => {
          this.$message.success("新增成功")
          this.dialogFormVisible = false
          this.load()
        })
      } else if (this.insertOrUpdate === 1) {//编辑
        this.doPut("/authorize", this.form).then(res => {
          this.$message.success("编辑成功")
          this.dialogFormVisible = false
          this.load()
        })
      }
    },
    del(id) {
      this.doDelete("/authorize/" + id).then(res => {
        this.$message.success("删除成功")
        this.load()
      })
    },
    //新增类型改成事件触发
    insertTypeChange(value) {
      //新增子级
      if (value === 0) {
        this.form.pid = this.formCopy.id
      } else if (value === 1) {//新增同级
        this.form.pid = this.formCopy.pid
      }
    },
  }
}
</script>

<style>
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 16px;
  padding-right: 8px;
}
</style>