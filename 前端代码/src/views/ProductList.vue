<template>
  <div>
      <ProjectHeader></ProjectHeader>
      <div class="content clearfix">
          <!--排行榜-->
          <ul class="rank-list">
              <li v-for="(item,ind) in rank" :key="item.phone">
                  <img src="@/assets/image/list-rank1.png" alt="" v-if="ind==0">
                  <img src="@/assets/image/list-rank2.png" alt="" v-else-if="ind==1">
                  <img src="@/assets/image/list-rank3.png" alt="" v-else>
                  <p class="rank-list-phone">{{item.phone}}</p>
                  <span>{{item.money}}元</span>
              </li>
          </ul>
          <!--产品列表-->
          <ul class="preferred-select clearfix">
              <li v-for="(product) in productList" :key="product.id">
                  <h3 class="preferred-select-title">
                      <span>{{product.productName}}</span>
                      <img src="@/assets/image/1-bg1.jpg" alt="">
                  </h3>
                  <div class="preferred-select-number">
                      <p><b>{{product.rate}}</b>%</p>
                      <span>历史年化收益率</span>
                  </div>
                  <div class="preferred-select-date">
                      <div>
                          <span>投资周期</span>
                          <p v-if="product.productType==0"><b>{{product.cycle}}</b>天</p>
                          <p v-else><b>{{product.cycle}}</b>个月</p>
                      </div>
                      <div>
                          <span>剩余可投资金额</span>
                          <p><b>{{product.leftProductMoney}}</b>元</p>
                      </div>
                  </div>
                  <p class="preferred-select-txt">
                      {{product.productDesc}}。
                  </p>
                  <a href="javascript:void(0)" @click="golink('/page/product/detail',{productId:product.id})" class="preferred-select-btn">立即投资</a>
              </li>
          </ul>

          <!--分页-->
          <div class="page_box">
              <ul class="pagination">
                  <li class="disabled"><a href="javascript:void (0)" @click="first()">首页</a></li>
                  <li><a href="javascript:void (0)" @click="pre()">上一页</a></li>
                  <li class="active"><span>{{pageInfo.pageNo}}</span></li>
                  <li><a a href="javascript:void (0)" @click="next()">下一页</a></li>
                  <li><a href="javascript:void (0)" @click="last()">尾页</a></li>
                  <li class="totalPages"><span>共{{pageInfo.totalPage}}页</span></li>
              </ul>
          </div>

      </div>
      <ProjectFoot></ProjectFoot>
  </div>
</template>

<script>
import ProjectHeader from "@/components/common/ProjectHeader.vue";
import ProjectFoot from "@/components/common/ProjectFoot.vue";
import {doGet} from "@/api/httpRequest";
import layx from "vue-layx";

let productType=0;
export default {
    watch: {
        '$route': function () {
            location.reload()
        }
    },
    name: "ProductList",
    components:{
        // eslint-disable-next-line vue/no-unused-components
        ProjectHeader,
        // eslint-disable-next-line vue/no-unused-components
        ProjectFoot
    },
    data(){
        return{
            rank:[    {
                phone: "***********",
                money: 0
            }],
            productList:[{
                id: 0,
                productName: "",
                rate: 0,
                cycle: 0,
                releaseTime: 0,
                productType: 0,
                productNo: "",
                productMoney: 0,
                leftProductMoney: 0,
                bidMinLimit: 0,
                bidMaxLimit: 0,
                productStatus: 0,
                productFullTime: 0,
                productDesc: "无"}],
            pageInfo:{
                pageNo: 1,
                pageSize: 0,
                totalPage: 1,
                totalRecord: 0
            }
        }
    },
    mounted() {
        window.scrollTo(0, 0)
        productType=this.$route.query.pType;
        this.initPage(productType,1,9);
        //获取排行榜
        doGet("/v1/invest/rank").then(resp=>{
            if (resp) {
                this.rank=resp.data.list;
            }
        })
    },
    methods:{
        initPage(productType,pageNo,pageSize){

            //获取产品列表数据
            doGet("v1/product/list",{pType:productType,pageNo:pageNo,pageSize:pageSize}).then(resp=>{
                if (resp) {
                    this.productList=resp.data.list;
                    this.pageInfo=resp.data.pageInfo;
                }
            })
        },
        first(){
            if (this.pageInfo.pageNo == 1) {
                layx.msg("已经为第一页数据了",{dialogIcon: 'warn',position:'tc',autodestroy:300})
            }else {
                this.initPage(productType,1,9)
                window.scrollTo(0, 0)
            }
        },
        last(){
            if (this.pageInfo.pageNo == this.pageInfo.totalPage) {
                layx.msg("已经为最后一页数据了",{dialogIcon: 'warn',position:'tc',autodestroy:300})
            }else {
                this.initPage(productType,this.pageInfo.totalPage,9)
                window.scrollTo(0, 0)
            }
        },
        pre(){
            if (this.pageInfo.pageNo <= 1) {
                layx.msg("已经为第一页数据了",{dialogIcon: 'warn',position:'tc',autodestroy:300})
            }else {
                this.initPage(productType,this.pageInfo.pageNo-1,9);
                window.scrollTo(0, 0)
            }
        },
        next(){
            if (this.pageInfo.pageNo >= this.pageInfo.totalPage) {
                layx.msg("已经为最后一页数据了",{dialogIcon: 'warn',position:'tc',autodestroy:300})
            }else {
                this.initPage(productType,this.pageInfo.pageNo+1,9);
                window.scrollTo(0, 0)
            }
        },
        golink(url,params) {
            this.$router.push({
                path: url,
                query: params
            })
        }
    }
}
</script>

<style scoped>

</style>