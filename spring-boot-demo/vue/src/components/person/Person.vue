<template>
  <div class="horizontal">
    <div>
      <el-card style="width: 75vh">
        <el-form label-width="80px" size="small">
          <el-form-item label="用户名">
            <el-input v-model="form.username" disabled autocomplete="off"></el-input>
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
            <el-input type="textarea"
                      placeholder="请输入备注"
                      maxlength="200" show-word-limit
                      :rows="4"
                      v-model="form.remark"
                      autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="save">确 定</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
    <div style="margin-left: 5px;width: 130vh">
      <Article></Article>
    </div>
  </div>
</template>

<script>

import Article from "@/components/person/Article";

export default {
  name: "Person",
  components: {Article},
  data() {
    return {
      serverIp: process.env.VUE_APP_BASE_URL,
      form: {},
      sexSelect: [
        {
          value: 0,
          label: '女'
        }, {
          value: 1,
          label: '男'
        }
      ],
    }
  },
  created() {
    this.getUser().then(res => {
      this.form = res
    })
  },
  methods: {
    async getUser() {
      return (await this.doGet("/user/getUserInfoByToken"))
    },
    save() {
      this.doPut("/user", this.form).then(res => {
        this.$message.success("保存成功")
        // 更新浏览器存储的用户信息
        this.getUser().then(res => {
          let userInfo = {}
          userInfo.nickname = res.nickname;//昵称
          userInfo.roleNames = res.roleNames;//角色名称集合
          localStorage.setItem("user", JSON.stringify(userInfo))
          //触发父级更新User的方法
          this.$emit("refreshUser");
        })
      })
    },
  }
}
</script>

<style>
.horizontal{
  display:flex;
  flex-wrap: wrap;
}
</style>
