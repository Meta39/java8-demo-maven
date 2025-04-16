<template>
  <div class="wrapper">
    <div
        style="margin: 200px auto; background-color: #fff; width: 350px; height: 280px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>登 录</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input size="medium" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <!-- @keyup.enter.native 按回车提交表单 -->
          <el-input size="medium" prefix-icon="el-icon-lock" @keyup.enter.native="login" show-password v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item style="margin: 10px 0; text-align: right">
          <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">注册</el-button>
          <el-button type="primary" size="small" autocomplete="off" @click="login">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import {setRoutes} from "@/router";
import RSAUtil from "@/utils/RSAUtil";

export default {
  name: "Login",
  data() {
    return {
      user: {},
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 1, max: 10, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    login() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          this.user.pwd = RSAUtil.getRSAEncryptString(this.user.password)
          this.user.password = '';//提交前把密码清除，防止传给后端时被拦截，导致密码泄露。
          // console.log("pwd",this.user.pwd)
          this.doPost("/login", this.user).then(res => {
            //清空数据，防止旧数据或者错误数据干扰。
            localStorage.clear()
            let userInfo = {}
            userInfo.nickname = res.nickname;//昵称
            userInfo.roleNames = res.roleNames;//角色名称集合
            localStorage.setItem("token", res.token);//token
            localStorage.setItem("user", JSON.stringify(userInfo))  // 存储用户信息到浏览器
            localStorage.setItem("menus", res.menus ? JSON.stringify(res.menus) : {})  // 存储用户菜单信息到浏览器
            localStorage.setItem("authorizes",res.authorizes ? JSON.stringify(res.authorizes) : {}) // 存储用户权限信息到浏览器
            // 动态设置当前用户的路由
            setRoutes()
            this.$message.success("登录成功")
            this.$router.push("/")
          })
        }
      });
    }
  }
}
</script>

<style scoped>
.wrapper {
  height: 100vh;
  background-image: linear-gradient(to bottom right, #FC466B, #3F5EFB);
  overflow: hidden;
}
</style>
