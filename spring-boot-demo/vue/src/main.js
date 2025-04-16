import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/gloable.css'
import request from "@/utils/request";
import {doGet} from "@/utils/request";
import {doPost} from "@/utils/request";
import {doPut} from "@/utils/request";
import {doDelete} from "@/utils/request";

// main.js全局注册
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
// use
Vue.use(mavonEditor)

Vue.config.productionTip = false

Vue.use(ElementUI, { size: "mini" });

Vue.prototype.request=request
Vue.prototype.doGet = doGet; //全局get请求方法
Vue.prototype.doPost = doPost; //全局post请求方法
Vue.prototype.doPut = doPut; //全局put请求方法
Vue.prototype.doDelete = doDelete; //全局delete请求方法

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
