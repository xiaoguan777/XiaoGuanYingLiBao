import axios from "axios";
import qs from "qs";
axios.defaults.baseURL="http://localhost:8000/api"
axios.defaults.timeout=50000
//get请求方法,url请求的地址,去掉了baseurl以外的部分
//params请求的参数
function doGet(url,params){
    return axios(
        {
            url:url,
            method:"get",
            params:params
        }
    )
}
//传递的是json数据,在请求报文中是json格式
function doPostJson(url,params){
    return axios(
        {
            url:url,
            method:"post",
            data:params
        }
    )
}
function doPost(url,params){
    let requestData=qs.stringify(params)
    return axios.post(url,requestData);
}
//创建拦截器
axios.interceptors.request.use(function (config){
        if(config.url=='/v1/user/realName'||config.url=='/v1/user/userAcountInfo'
        ||config.url=='/v1/recharge/records'||config.url=='/v1/invest/detail'
        ||config.url=='/v1/income/detail'||config.url=='/v1/invest/product'
        ||config.url=='/v1/user/judge/login'){
            let storageToken=window.localStorage.getItem("token");
            let userInfo=window.localStorage.getItem("userInfo");
            if(storageToken&&userInfo){
                //在header中传递token和userId
                config.headers['Authorization']='Bearer '+storageToken;
                config.headers['uid']=JSON.parse(userInfo).uid;
            }
        }
        return config;
    },
    function (err){
        console.log("请求错误"+err);
    })
axios.interceptors.response.use(function (resp){
        if (resp&&resp.data.code==3000) {
            window.location.href='/page/user/login?err=3000';
        }
        return resp;
    },
    function (err){
    console.log("响应错误"+err)
    })
//导出，暴露这个函数.其他模块才能使用
export {doGet,doPostJson,doPost}