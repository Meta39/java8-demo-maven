<template>
  <el-card style="width: 500px;">
    <el-form label-width="120px" size="small" :model="form" :rules="rules" ref="pass">

      <el-form-item label="原密码" prop="password">
        <el-input v-model="form.password" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="form.newPassword" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item label="确认新密码" prop="confirmPassword">
        <el-input v-model="form.confirmPassword" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="save">确 定</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import RSAUtil from "@/utils/RSAUtil"

export default {
  name: "Password",
  data() {
    return {
      form: {},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      rules: {
        password: [
          { required: true, message: '请输入原密码', trigger: 'blur' },
          { min: 1, message: '长度不少于1位', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 1, message: '长度不少于1位', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请重新输入新密码', trigger: 'blur' },
          { min: 1, message: '长度不少于1位', trigger: 'blur' }
        ],
      }
    }
  },
  methods: {
    save() {
      this.$refs.pass.validate((valid) => {
        if (valid) {
          if (this.form.newPassword !== this.form.confirmPassword) {
            this.$message.error("2次输入的新密码不相同")
            return false
          }
          this.form.pwd = RSAUtil.getRSAEncryptString(this.form.password)
          this.form.newPwd = RSAUtil.getRSAEncryptString(this.form.newPassword)
          this.form.password = '' //提交前清空密码。防止密码泄露。
          this.form.newPassword = '' //提交前清空新密码。防止密码泄露。
          this.form.confirmPassword = ''
          console.log("form",this.form)
          this.doPost("/updatePwd",this.form).then(res => {
            this.$message.success("修改密码成功")
            this.$store.commit("logout")
          })
        }
      })
    },
  }
}
</script>

<style>
</style>
