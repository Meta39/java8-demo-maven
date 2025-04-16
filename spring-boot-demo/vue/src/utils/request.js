import axios from 'axios'
import router from "@/router";
import {Message} from "element-ui";

const request = axios.create({
    baseURL: process.env.VUE_APP_BASE_URL,
    timeout: 30000
})

// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    let token = localStorage.getItem("token") ? localStorage.getItem("token") : ''
    if (token) {
        config.headers['token'] = token;  // 设置请求头
    }
    return config
}, error => {
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        //如果是返回没有认证的错误码，则跳转登录页面。
        if (res.code === 2) {//特殊
            router.push("/login")
        }
        //自定义状态码1为成功，其他为失败。
        if (res.code === 1) {//成功，返回data
            return res.data;
        } else {//失败
            Message({
                message: res.msg,
                type: "error",
                duration: 5 * 1000
            })
            return Promise.reject(new Error(res.msg))
        }
    },
    error => {
        console.log('err', error.response) // for debug
        const {status, data} = error.response;
        //404要特殊处理
        if (status === 404) {
            Message({
                message: "后端接口404：" + data.msg,
                type: "error",
                duration: 5 * 1000
            })
        }
        return Promise.reject(error)
    }
)

/**
 * 封装get请求
 * @param url 请求Url
 * @param params 参数
 */
export const doGet = (url, params) => {
    return request({
        url: url,
        method: "GET",
        params: params
    })
}

/**
 * 封装post请求
 * @param url 请求Url
 * @param requestBody 请求体
 * @param params 参数
 */
export const doPost = (url, requestBody, params) => {
    return request({
        url: url,
        method: "POST",
        data: requestBody, //对应后端的@RequestBody
        params: params, //对应后端的@RequestParam，即：在url后面拼接参数
    })
}

/**
 * 封装put请求
 * @param url 请求Url
 * @param requestBody 请求体
 * @param params 参数
 */
export const doPut = (url, requestBody, params) => {
    return request({
        url: url,
        method: "PUT",
        data: requestBody, //对应后端的@RequestBody
        params: params, //对应后端的@RequestParam，即：在url后面拼接参数
    })
}

/**
 * 封装delete请求
 * @param url 请求Url
 * @param requestBody 请求体
 * @param params 参数
 */
export const doDelete = (url, requestBody, params) => {
    return request({
        url: url,
        method: "DELETE",
        data: requestBody, //对应后端的@RequestBody
        params: params, //对应后端的@RequestParam，即：在url后面拼接参数
    })
}

export default request

