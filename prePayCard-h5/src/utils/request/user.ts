import { password } from "../md5";
import request from "./request";

export const login = (data: any) => {
    //处理密码
    data.loginPasswd = password(data.loginPasswd);

    return request({
        method: "POST",
        url: "/UserLogin",
        data,
    })
}

export const register = (data: any) => {

    //处理密码
    data.loginPasswd = password(data.loginPasswd);
    data.payPasswd = password(data.payPasswd);

    return request({
        method: "POST",
        url: "/UserRegister",
        data,
    })
}

export const getBalanceByUser = ()=>{
    return request({
        method: "GET",
        url: "/getBalance",
    })
}