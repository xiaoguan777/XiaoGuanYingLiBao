<template>
  <!--头部-->
    <div class="public-head">
        <div class="public-head-nav">
            <div class="public-head-left">
                <h1 class="public-head-logo"><a href="javascript:;">
                    <img src="@/assets/image/logo.png" alt="">
                </a></h1>
                <ul class="public-head-list">
                    <li><a href="javascript:void(0)" @click="golink('/')">主页</a></li>
                    <li class="public-head-hover">
                        <a href="javascript:void(0);">我要投资</a>
                        <!--二级导航-->
                        <div class="two-title">
                            <a href="javascript:void(0);" @click="golink('/page/product/list',{pType:0})">新手宝产品</a>
                            <a href="javascript:void(0);" @click="golink('/page/product/list',{pType:1})">优选类产品</a>
                            <a href="javascript:void(0);" @click="golink('/page/product/list',{pType:2})">散标类产品</a>
                        </div>
                    </li>
                    <li><a href="javascript:void(0);" @click="golink('/page/user/userCenter')">用户信息</a></li>
                    <li><a href="javascript:void(0);" @click="golink('/page/user/realName')">实名认证</a></li>
                    <li><a href="javascript:void(0);" @click="golink('/page/user/userPay')">充值中心</a></li>
                </ul>
            </div>
            <div class="public-head-right">
                <a href="javascript:void(0);" @click="golink('/page/user/login')">登录</a>
                <a href="javascript:void(0);" @click="golink('/page/user/register')">注册</a>
            </div>
        </div>
    </div>
  <!--end-->
</template>

<script>
export default {
    name: "ProjectHeader",
    methods:{
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

</style>