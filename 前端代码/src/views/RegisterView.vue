<template>
  <div>
      <ProjectHeader></ProjectHeader>
      <div class="login-content">
          <div class="login-flex">
              <div class="login-left">
                  <p>万民用户知心托付&nbsp;&nbsp;&nbsp;&nbsp;<span>{{historyAvgRate}}%</span>历史年化收益</p>
                  <p>千万级技术研发投入&nbsp;&nbsp;&nbsp;&nbsp;亿级注册资本平台  </p>
              </div>
              <!---->
              <div class="login-box">
                  <h3 class="login-title">用户注册</h3>
                  <form action="" id="register_Submit">
                      <div class="alert-input">
                          <input type="text" class="form-border user-num" @blur="checkPhone" v-model="phone" name="phone" placeholder="请输入11位手机号">
                          <div class="err">{{phoneErr}}</div>
                          <p class="prompt_num"></p>
                          <input type="password" placeholder="请输入6-20位英文和数字混合密码"
                                 class="form-border user-pass" autocomplete name="pwd" v-model="password" @blur="checkPassword">
                          <div class="err">{{passwordErr}}</div>
                          <p class="prompt_pass"></p>
                          <div class="form-yzm form-border">
                              <input class="yzm-write" type="text" name="scode" placeholder="输入短信验证码" v-model="code" @keyup="checkCode">
                              <input class="yzm-send" type="button" v-bind:value="yzmText" @click="requestSsmCode" id="yzmBtn" readonly="readonly" >
                          </div>
                          <div class="err">{{codeErr}}</div>
                          <p class="prompt_yan"></p>
                      </div>
                      <div class="alert-input-agree">
                          <input type="checkbox" v-model="agree"/>我已阅读并同意<a href="javascript:;" target="_blank">《小关金融网注册服务协议》</a>
                      </div>
                      <div class="alert-input-btn">
                          <input type="button" class="login-submit" @click="requestUserRegister" value="注册" >
                      </div>
                  </form>
                  <div class="login-skip">
                      已有账号？ <a href="javascript:void(0);" @click="golink('/page/user/login')" >登录</a>
                  </div>
              </div>

          </div>
      </div>
      <project-foot></project-foot>
  </div>
</template>

<script>
import ProjectHeader from "@/components/common/ProjectHeader.vue";
import ProjectFoot from "@/components/common/ProjectFoot.vue";
import {doGet, doPost} from "@/api/httpRequest";
import layx from "vue-layx";
import md5 from 'js-md5';

export default {
    name: "RegisterView",
    components:{
        // eslint-disable-next-line vue/no-unused-components
        ProjectHeader,
        // eslint-disable-next-line vue/no-unused-components
        ProjectFoot
    },
    data(){
        return{
            historyAvgRate:0.0,
            phone:'',
            phoneErr:'',
            password:'',
            passwordErr:'',
            code:'',
            codeErr:'',
            yzmText:'获取验证码',
            flag:true,//保证计时器只执行一次
            agree:false
        }
    },
    mounted() {
        //向服务器发请求,获取数据，更新资源
        doGet("/v1/plat/info").then(resp=>{
            if (resp) {
                this.historyAvgRate=resp.data.date.historyAvgRate;
            }
        })
    },
    methods:{
        golink(url,params){
            this.$router.push({
                path:url,
                query:params
            })
        },
        checkPhone(){
            if(this.phone==''||this.phone==undefined){
                this.phoneErr='请输入手机号';
            }else if(!/^1[1-9]\d{9}$/.test(this.phone)){
                this.phoneErr='手机号格式不正确';
            }else {
                this.phoneErr='';
                doGet("/v1/user/phone/exists",{phone:this.phone}).then(resp=>{
                    if (resp&&resp.data.code===7){
                        this.phoneErr='';
                    }else {
                        this.phoneErr=resp.data.msg;
                    }
                })
            }
        },
        checkPassword(){
            if (this.password == '' || this.password == undefined) {
                this.passwordErr='请输入密码';
            }else if (!(this.password.length>=6&&this.password.length<=20)){
                this.passwordErr='密码必须在6到20位数之间';
            }else if(!/^[0-9a-zA-Z]+$/.test(this.password)){
                this.passwordErr='你的密码只能使用数字和大小写英文字母';
            }else if(!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(this.password)){
                this.passwordErr='密码应同时包含英文和数字';
            }else {
                this.passwordErr='';
            }
        },
        checkCode(){
            if (this.code == null && this.code == undefined) {
                this.codeErr='请输入验证码';
            }else if(this.code.length!=4){
                this.codeErr='验证码为四位，请输入正确的验证码长度'
            }else {
                this.codeErr='';
            }
        },
        requestSsmCode(){
            this.checkPhone();
            if (this.phoneErr == ''&&this.flag) {
                this.flag=false;
                //倒计时
                let second=60;//倒计时间
                let timerId=setInterval(()=>{
                    if(second<=0){
                        this.yzmText="获取验证码";
                        clearInterval(timerId);
                        this.flag=true;
                    }else {
                        this.yzmText=second+"秒后重新获取"
                        second=second-1
                    }
                },1000)
                //发起请求给服务器,发送验证码
                doGet("/v1/sms/code/register",{phone:this.phone}).then(resp=>{
                    if (resp&&(resp.data.code==7||resp.data.code==1006)) {
                        layx.msg(resp.data.msg,{dialogIcon: 'success',position:'tc',autodestroy:800})
                    }else{
                        layx.msg(resp.data.msg,{dialogIcon: 'warn',position:'tc',autodestroy:800});
                    }
                })
            }
        },
        requestUserRegister(){
            this.checkPhone();
            this.checkPassword();
            this.checkCode();
            if(this.agree){
                if (this.phoneErr == '' && this.passwordErr == '' && this.codeErr == '') {
                    //数据正确
                    let newPassword=md5(this.password);
                    doPost('/v1/user/register',{
                        phone:this.phone,
                        pwd:newPassword,
                        scode:this.code
                    }).then(resp=>{
                        if (resp&&resp.data.code==7) {
                            //跳转登录页面
                            this.$router.push({
                                path:"/page/user/login",
                            })
                        }
                    })
                }
            }else {
                layx.msg("需要同意服务协议",{dialogIcon: 'warn',position:'tc',autodestroy:400})
            }
        }
    }
}
</script>

<style scoped>
    .err{

        color: red;
        font-size: 12px;
    }
</style>