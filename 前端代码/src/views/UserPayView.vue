<template>
    <ProjectHeader></ProjectHeader>
  <div><!--充值-->
      <div class="user-pay-box clearfix">
          <div class="user-pay-left fl">
              <p class="user-pay-title1">我的账户</p>
              <div class="user-pay-list">
                  <a href="javascript:;" class="user-pay-list-on">充值</a>
              </div>
          </div>
          <div class="user-pay-right fl">
              <p class="user-pay-title2">第三方支付平台</p>
              <div class="user-pay-form">
                  <img src="@/assets/image/kq.png" alt="">
                  <form action="http://localhost:9000/pay/kq/rece/recharge" id="money_submit" target="_blank" >
                      <p class="user-pay-form-ts">请输入金额</p>
                      <input type="text"  placeholder="元" name="money" class="number-money"  value="0.01">
                      <input type="hidden" name="uid" v-bind:value="uid">
                      <input type="submit"  value="支付" class="submit-btn">
                  </form>
              </div>
              <div class="user-pay-sm">
                  <h3>温馨提示</h3>
                  <p>1.该接口有快钱公司提供。</p>
                  <p>2.点击支付可以用测试账号进行测试,选择银行为北京银行，并且输入可以输入测试银行账号6214 6800 3838 7096测试</p>
                  <p>3.姓名可以任意写，也可以用测试姓名张峰,身份证账号也可以随便写，也可以用测试身份证110101199105149839</p>
                  <p>4.验证码需要填真实手机号接收验证码，然后就可以进行测试了</p>
              </div>
          </div>

      </div></div>
  <project-foot></project-foot>
</template>

<script>
import ProjectHeader from "@/components/common/ProjectHeader.vue";
import ProjectFoot from "@/components/common/ProjectFoot.vue";
import {doGet} from "@/api/httpRequest";

export default {
    name: "UserPayView",
    components:{
        // eslint-disable-next-line vue/no-unused-components
        ProjectHeader,
        // eslint-disable-next-line vue/no-unused-components
        ProjectFoot
    },
    data(){
        return{
            uid:0
        }
    },
    mounted() {
        doGet("/v1/user/judge/login").then(resp=>{
            if (resp&&resp.data.code==7) {
                let userinfo=window.localStorage.getItem("userInfo")
                if (userinfo) {
                    this.uid=JSON.parse(userinfo).uid;
                }
            }else {
                window.location.href='/page/user/login?err=3000';
            }
        })
    }

}
</script>

<style scoped>

</style>