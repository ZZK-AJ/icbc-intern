import router from "@/router";
import axios from "axios";
import { Toast } from "vant";
import { ToastWrapperInstance } from "vant/lib/toast/types";
import store from "@/store/index"

let toast: ToastWrapperInstance | null = null;
const request = axios.create({
    timeout: 10000, 
    baseURL: "/api/"
})
const httpCode: { [key: number]: string; } = {
    400: '请求参数错误',
    401: '暂未登录',
    403: '服务器拒绝本次访问',
    404: '请求资源未找到',
    500: '内部服务器错误',
    501: '服务器不支持该请求中使用的方法',
    502: '网关错误',
    504: '网关超时'
}

//请求拦截 每次请求带上token
request.interceptors.request.use(
    config => {
        toast = Toast.loading({
            duration: 0,
            message: "加载中"
        });
        let token = store.state.token;
        if (token) {
            if (config && config.headers) {
                config.headers.Authorization = token;
            }
        }
        return config
    }, error => {
        console.log(error);
        return Promise.reject(error)
    }
)

/** 添加响应拦截器  **/
request.interceptors.response.use(response => {

    setTimeout(() => {
        if (toast) {
            toast.clear();
        }
    },300)



    return response;
}, error => {
    const tips = error.response?error.response.status in httpCode ? httpCode[error.response.status] : error.response.data.message?error.response.data.message:"请求出错":"请求超时";
    Toast.fail(tips)
    if (error.response.status === 401) {
        router.push("/401");
    }
})

export default request;