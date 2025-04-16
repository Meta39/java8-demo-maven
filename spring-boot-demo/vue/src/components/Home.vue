<template>
  <div style="color: #666;font-size: 14px;">
    <div style="padding-bottom: 20px">
      <el-card>
        <br>
        尊重原创：本项目参考：<el-link type="primary" href="'https://www.bilibili.com/video/BV1U44y1W77D/?spm_id_from=333.999.0.0&vd_source=a2afdfe0edac03f9d312801f00a93550'">《B站程序员青戈：从0开始带你手撸一套SpringBoot+Vue后台管理系统（2022年最新版）
        》</el-link>
      </el-card>
    </div>
    <div style="height: 1px; background: #ddd; margin: 20px 0"/>

    <el-row>
      <el-col :span="12" style="line-height: 30px">
        <el-card style="width: 25vh">
          <b>前端</b>
          <div>安装项目：npm install<br>启动项目：npm run serve<br>打包项目：npm run build</div>
          <div>nodejs：14.16.0</div>
          <div>vue/cli：4.5.14</div>
          <div>Vue2</div>
          <div>Element-UI：2.15.6</div>
          <div>Vue-Router</div>
          <div>Axios：0.25.0</div>
          <div>VueX：3.6.2</div>
        </el-card>
      </el-col>
      <el-col :span="12" style="line-height: 30px">
        <el-card style="width: 25vh">
          <div><b>后端</b></div>
          <div>JWT(我个人感觉不好用)</div>
          <div>Poi</div>
          <div>Lombok</div>
          <div>MySQL8</div>
          <div>Redis</div>
          <div>SpringBoot2</div>
          <div>Mybatis-plus：3.5.2</div>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
    }
  }
}
</script>
