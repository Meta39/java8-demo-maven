<template>
  <div style="overflow: hidden; height: 100vh">
    <el-result icon="error" title="404" subTitle="找不到页面">
      <template slot="extra">
        <el-button type="primary" size="medium" @click="goBack()">返回首页</el-button>
      </template>
    </el-result>
  </div>
</template>

<script>
export default {
  name: "NotFound",
  methods:{
    goBack(){
      this.$router.push({path:'/'})
    }
  }
}
</script>
