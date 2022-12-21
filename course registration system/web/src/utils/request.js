import axios from 'axios'
import router from "@/router";

const request = axios.create({
    baseURL:"/api",
    timeout: 60000
})

// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    const userJson = sessionStorage.getItem("userToken")
    config.headers['token'] = userJson;  // 设置请求头
    //取出sessionStorage的字符串信息，在未登陆的情况下永远强制跳转到登陆


    // if(!userJson && config.url!=="/user/register" && config.url!=="/departments/getDepartments"){
    //     router.push("/login")
    // }
    return config
}, error => {
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        let jwtWrong = ["-435","-436","-437","-438","-439"]
        if(jwtWrong.indexOf(res.code) != -1){
            router.push("/login");
            sessionStorage.setItem("userToken","-1")
            return res
        }
        // 如果是返回的文件
        if (response.config.responseType === 'blob') {
            return res
        }
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        return res;
    },
    error => {
        console.log('err' + error) // for debug
        return Promise.reject(error)
    }
)


export default request

