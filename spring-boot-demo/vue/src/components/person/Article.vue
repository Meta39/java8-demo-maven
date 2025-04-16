<template>
  <div class="article-class">
    <el-card>
      <div style="margin: 10px 0">
        标题：
        <el-input style="width: 200px" placeholder="请输入标题" suffix-icon="el-icon-search" v-model="title"></el-input>
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
      <el-table
          :data="tableData"
          border
          stripe
          :header-cell-class-name="'headerBg'"
          @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="title" label="文章标题"></el-table-column>
        <el-table-column prop="content" label="文章内容" show-overflow-tooltip></el-table-column>
        <el-table-column prop="createTime" label="创建时间"></el-table-column>
        <el-table-column prop="updateTime" label="更新时间"></el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button @click="showView(scope.row)">查看</el-button>
            <el-button type="success" @click="insertOrUpdateButton(scope.row)">编辑</el-button>
            <el-popconfirm
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
            :page-sizes="[10, 15, 20, 50]"
            :page-size="size"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>

      <!--新增/编辑-->
      <el-drawer :title="dialogTitle + '文章'"
                 :before-close="handleCloseArticle"
                 size="100%"
                 direction="ttb"
                 :wrapperClosable="false"
                 :visible.sync="dialogFormVisible"
                 destroy-on-close>
        <el-form label-width="80px" size="small">
          <el-form-item label="文章标题">
            <el-input v-model="form.title" autocomplete="off" style="width: 75vh"></el-input>
          </el-form-item>
          <el-form-item label="文章内容">
            <mavon-editor ref="md" v-model="form.content" :ishljs="true" @imgAdd="imgAdd"/>
          </el-form-item>
        </el-form>
        <div style="text-align: right;margin: 10px 10px 10px 10px;" class="demo-drawer__footer">
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定要关闭未保存的文章内容吗？"
              @confirm="handleCloseArticle2"
          >
            <el-button type="danger" slot="reference">取消</el-button>
          </el-popconfirm>
          <el-button type="primary" @click="save">确定</el-button>
        </div>
      </el-drawer>
    </el-card>

    <!-- 查看文章 -->
    <el-drawer :visible.sync="showViewVisible"
               size="100%"
               direction="ttb"
               :wrapperClosable="false"
               destroy-on-close>
      <h1 style="text-align: center;font-size: 24px;">{{ showTitle }}</h1>
      <mavon-editor
          style="margin-top: 10px;"
          class="md"
          :value="content"
          :subfield="false"
          :defaultOpen="'preview'"
          :toolbarsFlag="false"
          :editable="false"
          :scrollStyle="true"
          :ishljs="true"
      />
    </el-drawer>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Article",
  data() {
    return {
      //动态内容高度
      contentHeight: {
        height: '',
      },
      title: '',
      showTitle: '',
      form: {},
      tableData: [],
      multipleSelection: [],
      page: 1,
      size: 10,
      total: 0,
      dialogTitle: '', //新增/编辑的标题
      insertOrUpdate: 0, // 0表示编辑，1表示新增
      dialogFormVisible: false,
      showViewVisible: false, //查看文章
      content: '',
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.doGet("/article/page", {
        params: {
          page: this.page,
          size: this.size,
          title: this.title,
        }
      }).then(res => {
        this.tableData = res.records
        this.total = res.total
      })
    },
    reset() {
      this.title = ""
      this.load()
    },
    //查看文章
    showView(row) {
      this.content = row.content
      this.showTitle = row.title
      this.showViewVisible = true
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
    // 绑定@imgAdd event。md里面插入图片地址
    imgAdd(pos, $file) {
      let $vm = this.$refs.md
      // 第一步.将图片上传到服务器.
      const formData = new FormData();
      formData.append('file', $file);
      axios({
        //上传到文件服务器
        url: 'http://localhost:9090/file/upload',
        method: 'post',
        data: formData,
        headers: {'Content-Type': 'multipart/form-data'},
      }).then((res) => {
        // 第二步.将返回的url替换到文本原位置![...](./0) -> ![...](url)
        $vm.$img2Url(pos, res.data);
      })
    },
    //保存
    save() {
      if (this.insertOrUpdate === 0) {//新增
        this.doPost("/article", this.form).then(res => {
          this.$message.success("新增成功")
          this.dialogFormVisible = false
          this.load()
        })
      } else if (this.insertOrUpdate === 1) {//编辑
        this.doPut("/article", this.form).then(res => {
          this.$message.success("编辑成功")
          this.dialogFormVisible = false
          this.load()
        })
      }
    },
    del(id) {
      this.doDelete("/article/" + id).then(res => {
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
      this.doDelete("/article/deleteBatch", ids).then(res => {
        this.$message.success("批量删除成功")
        this.load()
      })
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
    //关闭新增/编辑界面提示
    handleCloseArticle(done) {
      this.$confirm('确认关闭未保存的文章吗？')
          .then(_ => {
            done();
          })
          .catch(_ => {
          });
    },
    handleCloseArticle2(){
      this.dialogFormVisible = false
    }
  }
}
</script>

<style>

</style>