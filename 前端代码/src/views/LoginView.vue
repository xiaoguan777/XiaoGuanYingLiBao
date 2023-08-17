<template>
    <div>
        <project-header></project-header>
        <div class="login-content">
            <div class="login-flex">
                <div class="login-left">
                    <h3>加入小关金融网</h3>
                    <p>坐享<span>{{platInfo.historyAvgRate}}%</span>历史年化收益</p>
                    <p>平台用户<span>{{platInfo.registerUsers}}</span>位  </p>
                    <p>累计成交金额<span>{{platInfo.sumBidMoney}}</span>元</p>
                </div>
                <!---->
                <div class="login-box">
                    <h3 class="login-title">欢迎登录</h3>
                    <form action="" id="login_Submit">
                        <div class="alert-input">
                            <!--<input class="form-border user-name" name="username" type="text" placeholder="您的姓名">
                            <p class="prompt_name"></p>-->
                            <input type="text" class="form-border user-num" name="phone" v-model="phone" @blur="checkPhone" placeholder="请输入11位手机号">
                            <div class="err">{{phoneErr}}</div>
                            <p class="prompt_num"></p>
                            <input type="password" placeholder="请输入登录密码" class="form-border user-pass" v-model="password" @blur="checkPassword" autocomplete name="pwd">
                            <div class="err">{{passwordErr}}</div>
                            <p class="prompt_pass"></p>
                            <div class="form-yzm form-border">
                                <input class="yzm-write" type="text" v-model="code"  @keyup="checkCode" placeholder="输入短信验证码">
                                <input class="yzm-send" type="button" v-bind:value="yzmText"  id="yzmBtn" @click="requestSsmCode">
                            </div>
                            <div class="err">{{codeErr}}</div>
                            <p class="prompt_yan"></p>
                        </div>
                        <div class="alert-input-btn">
                            <input type="button" @click="userLogin" class="login-submit" value="登录">
                        </div>
                    </form>

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
    name: "LoginView",
    components: {
        // eslint-disable-next-line vue/no-unused-components
        ProjectHeader,
        // eslint-disable-next-line vue/no-unused-components
        ProjectFoot
    },
    data(){
        return{
            platInfo:{sumBidMoney:0.00,registerUsers:0,historyAvgRate:0.00},
            phone:'',
            phoneErr:'',
            password:'',
            passwordErr:'',
            code:'',
            codeErr:'',
            flag:true,//保证计时器单独执行
            yzmText:'获取验证码'
        }
    },
    mounted() {
        if(this.$route.query.err==3000){
            layx.msg("请先登录！",{dialogIcon: 'warn',position:'tc',autodestroy:1500});
        }
        //向服务器发请求,获取数据，更新资源
        doGet("/v1/plat/info").then(resp=>{
            if (resp) {
                this.platInfo=resp.data.date
            }
        })
    },
    methods:{
        checkPhone(){
            if(this.phone==''||this.phone==undefined){
                this.phoneErr='请输入手机号';
            }else if(!/^1[1-9]\d{9}$/.test(this.phone)){
                this.phoneErr='手机号格式不正确';
            }else {
                this.phoneErr = '';
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
            if(this.phoneErr==''&&this.flag){
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
                //向服务器发送请求
                doGet('/v1/sms/code/login',{phone:this.phone}).then(resp=>{
                    if (resp&& (resp.data.code==7||resp.data.code==1006)) {
                        layx.msg(resp.data.msg,{dialogIcon: 'success',position:'tc',autodestroy:800});
                    }else{
                        layx.msg(resp.data.msg,{dialogIcon: 'warn',position:'tc',autodestroy:800});
                    }
                })
            }
        },
        userLogin(){
            this.checkPhone();
            this.checkPassword();
            this.checkCode();
            if (this.phoneErr == '' && this.passwordErr == '' && this.codeErr == '') {
                let newPassword=md5(this.password);
                let param={
                    phone:this.phone,
                    pwd:newPassword,
                    scode:this.code
                }
                doPost('/v1/user/login',param).then(resp=>{
                    if (resp&&resp.data.code==7) {
                        //存储数据到localStorage中只能存字符串
                        window.localStorage.setItem("token",resp.data.accessToken);
                        window.localStorage.setItem("userInfo",JSON.stringify(resp.data.date))
                        if(resp.data.date.name==''){
                            this.$router.push(
                                //实名认证
                                {
                                    path:'/page/user/realName'
                                }
                            )
                        }else {
                            this.$router.push(
                                //用户中心
                                {
                                    path:'/page/user/userCenter'
                                }
                            )
                        }
                    }else {
                        layx.msg(resp.data.msg,{dialogIcon: 'warn',position:'tc',autodestroy:800});
                    }
                })
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