<template>
  <div>
     <project-header></project-header>
      <div><div class="login-content">
          <div class="login-flex">
              <div class="login-left"></div>
              <!---->
              <div class="login-box">
                  <h3 class="login-title">实名认证</h3>
                  <form action="" id="renZ_Submit">
                      <div class="alert-input">
                          <input type="text" class="form-border user-name" name="username" v-model="name" @keyup="checkName" placeholder="请输入您的真实姓名">
                          <div class="err">{{nameErr}}</div>
                          <p class="prompt_name"></p>
                          <input type="text" class="form-border user-sfz" name="sfz" placeholder="请输入15位或18位身份证号" v-model="idCard" @keyup="checkIdCard">
                          <div class="err">{{idCardErr}}</div>
                          <p class="prompt_sfz"></p>
                          <input type="text" class="form-border user-num" name="phone" v-bind:value="phone" readonly>
                          <p class="prompt_num"></p>
                          <div class="form-yzm form-border">
                              <input class="yzm-write" type="text" v-model="code"  @keyup="checkCode" placeholder="输入短信验证码">
                              <input class="yzm-send" type="button" v-bind:value="yzmText"  id="yzmBtn" @click="requestSsmCode">
                          </div>
                          <div class="err">{{codeErr}}</div>
                          <p class="prompt_yan"></p>
                      </div>
                      <div class="alert-input-btn">
                          <input type="button" @click="requestRealName" class="login-submit" value="认证">
                      </div>
                  </form>
                  <div class="login-skip">
                      暂不认证？ <a href="javascript:void (0);" @click="golink('/page/user/userCenter')">跳过</a>
                  </div>
              </div>

          </div>
      </div></div>
      <project-foot></project-foot>
  </div>
</template>

<script>
import ProjectHeader from "@/components/common/ProjectHeader.vue";
import ProjectFoot from "@/components/common/ProjectFoot.vue";
import {doGet, doPostJson} from "@/api/httpRequest";
import layx from "vue-layx";

export default {
    name: "RealNameView",
    components:{
        // eslint-disable-next-line vue/no-unused-components
        ProjectHeader,
        // eslint-disable-next-line vue/no-unused-components
        ProjectFoot
    },
    data(){
        return{
            phone:'',
            name:'',
            nameErr:'',
            code:'',
            codeErr:'',
            idCard:'',
            idCardErr:'',
            flag:true,//保证计时器单独执行
            yzmText:'获取验证码'
        }
    },
    mounted() {
        //获取localStorage中的用户数据
        let userInfo=window.localStorage.getItem("userInfo")
        if (userInfo) {
            this.phone=JSON.parse(userInfo).phone;
        }else{
            this.$router.push(
                //实名认证
                {
                    path:'/page/user/login'
                }
            )

        }
    },
    methods:{
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
            if(this.flag){
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
                doGet('/v1/sms/code/realname',{phone:this.phone}).then(resp=>{
                    if (resp&& (resp.data.code==7||resp.data.code==1006)) {
                        layx.msg(resp.data.msg,{dialogIcon: 'success',position:'tc',autodestroy:800});
                    }else{
                        layx.msg(resp.data.msg,{dialogIcon: 'warn',position:'tc',autodestroy:800});
                    }
                })
            }
        },
        checkName(){
            if(this.name==null||this.name==''||this.name==undefined){
                this.nameErr='请输入姓名';
            }else if(!/^[\u4e00-\u9fa5]{0,}$/.test(this.name)){
                this.nameErr='名字必须为中文'
            }else if(this.name.length<2){
                this.nameErr='请输入完整姓名';
            }else {
                this.nameErr='';
            }
        },
        checkIdCard(){
            if(this.idCard==''||this.idCard==null||this.idCard==undefined){
                this.idCardErr="请输入身份证号";
            }else if(!/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(this.idCard)){
                this.idCardErr="请输入正确的身份证号";
            }else {
                this.idCardErr='';
            }
        },
        requestRealName(){
            this.checkName();
            this.checkCode();
            this.checkIdCard();
            if(this.nameErr==''&&this.codeErr==''&&this.idCardErr==''){
                let params={
                    idCard:this.idCard,
                    phone:this.phone,
                    realName:this.name,
                    scode:this.code
                }
                doPostJson("/v1/user/realName",params).then(resp=>{
                    if (resp &&resp.data.code==7) {
                        this.$router.push({
                            path:"/page/user/userCenter"
                        })
                    }else {
                        layx.msg(resp.data.msg,{dialogIcon: 'warn',position:'tc',autodestroy:800});
                    }
                })
            }
        },
        golink(url,params){
            this.$router.push({
                path:url,
                query:params
            })
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