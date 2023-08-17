<template>
    <div>
        <ProjectHeader></ProjectHeader>

      <!--banner-->
      <div class="banner-content">
          <div class="swiper-container banner-lb">
              <div class="swiper-wrapper">
                  <div class="swiper-slide">
                      <a href="javascript:void(0);" >
                          <img src="@/assets/image/bannerps.jpg" alt="">
                      </a>
                  </div>
                  <div class="swiper-slide">
                      <a href="javascript:;" >
                          <img src="@/assets/image/banner.jpg" alt="">
                      </a>
                  </div>
              </div>
          </div>
          <div class="banner-abs">
              <div class="banner-abs-box">
                  <div class="banner-abs-title">小关金融网历史年化收益率</div>
                  <b>{{platInfo.historyAvgRate}}<i>%</i></b>
                  <p>平台用户数</p>
                  <span>{{platInfo.registerUsers}}<i>位</i></span>
                  <p class="banner-abs-border">累计成交金额</p>
                  <span>{{platInfo.sumBidMoney}}<i>元</i></span>
              </div>
          </div>
      </div>
      <div class="banner-list">
          <ul>
              <li>
                  <img src="@/assets/image/banner-tag1.png" alt="">
                  <p>
                      <b>实力雄厚</b>
                      <span>亿级注册资本 ,千万技术投入</span>
                  </p>
              </li>
              <li>
                  <img src="@/assets/image/banner-tag2.png" alt="">
                  <p>
                      <b>风控严苛</b>
                      <span>30道风控工序，60项信用审核</span>
                  </p>
              </li>
              <li>
                  <img src="@/assets/image/banner-tag3.png" alt="">
                  <p>
                      <b>投资省心</b>
                      <span>资金安全风控，银行安全托管</span>
                  </p>
              </li>
          </ul>
      </div>

      <!--产品-->
      <div class="content">
          <h2 class="public-title"><span>新手宝</span> <a href="javascript:void(0)" @click="golink('/page/product/list',{pType:0})" class="public-title-more">查看更多产品>></a></h2>
          <div class="new-user" v-for="product in product0" :key="product.id">
              <div class="new-user-sm">
                  <span>{{product.bidMinLimit}}元起投</span>
                  <span>投资最高限额{{product.bidMaxLimit}}元</span>
                  <span>当日即产生收益</span>
              </div>
              <div class="new-user-number">
                  <ul>
                      <li>
                          <p><b>{{product.rate}}</b>%</p>
                          <span>历史年化收益率</span>
                      </li>
                      <li>
                          <p v-if="product.productType==0"><b>{{product.cycle}}</b>天</p>
                          <p v-else><b>{{product.cycle}}</b>个月</p>
                          <span>投资周期</span>
                      </li>
                      <li>
                          <p><b>{{product.leftProductMoney}}</b>元</p>
                          <span>剩余可投资金额</span>
                      </li>
                  </ul>
                  <a href="javascript:void(0)" @click="golink('/page/product/detail',{productId:product.id})" class="new-user-btn">立即投资</a>
              </div>
              <span class="new-tag">新用户专享</span>
          </div>//新手宝

          <h2 class="public-title"><span>优选产品</span> <a href="javascript:void(0)" @click="golink('/page/product/list',{pType:1})" class="public-title-more">查看更多产品>></a></h2>
          <ul class="preferred-select clearfix">
              <li v-for="(product,index) in product1" :key="product.id">
                  <h3 class="preferred-select-title">
                      <span>{{product.productName}}</span>
                      <img src="@/assets/image/1-bg1.jpg" alt="" v-if="index==0">
                      <img src="@/assets/image/1-bg2.jpg" alt="" v-else-if="index==1">
                      <img src="@/assets/image/1-bg3.jpg" alt="" v-else>
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
              </li>//优选产品
          </ul>

          <h2 class="public-title"><span>散标产品</span> <a href="javascript:void(0)" @click="golink('/page/product/list',{pType:2})" class="public-title-more">查看更多产品>></a></h2>
          <ul class="preferred-select clearfix">
              <li v-for="product in product2" :key="product.id">
                  <h3 class="preferred-select-title1">{{product.productName}}
                      <span>散标</span>
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
              </li>//散标

          </ul>

      </div>

      <!--说明-->
      <div class="information-box">
          <ul>
              <li>
                  <img src="@/assets/image/2-icon1.png" alt="">
                  <p>优质借款</p>
                  <span>严苛风控，多重审核</span>

              </li>
              <li>
                  <img src="@/assets/image/2-icon2.png" alt="">
                  <p>次日计息</p>
                  <span>闪电成交，谁比我快</span>
              </li>
              <li>
                  <img src="@/assets/image/2-icon3.png" alt="">
                  <p>全年无休</p>
                  <span>百万用户，一路同行</span>
              </li>
              <li>
                  <img src="@/assets/image/2-icon4.png" alt="">
                  <p>知心托付</p>
                  <span>百万用户，一路同行</span>
              </li>
              <li>
                  <img src="@/assets/image/2-icon5.png" alt="">
                  <p>技术保障</p>
                  <span>千万投入，专注研发</span>
              </li>
          </ul>
      </div>

    <ProjectFoot></ProjectFoot>

  </div>
</template>

<script>
import ProjectHeader from "@/components/common/ProjectHeader.vue"
import ProjectFoot from "@/components/common/ProjectFoot.vue"
import {doGet} from "@/api/httpRequest";
export default {
    name: "IndexView",
    components:{
        ProjectHeader,
        ProjectFoot
    },
    data(){
        return {
            platInfo:{sumBidMoney:0.00,registerUsers:0,historyAvgRate:0.00},
            product0:[{
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
            product1:[{
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
            product2:[{
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
        }
    },
    mounted() {//页面加载到浏览器
        window.scrollTo(0, 0)
        //向服务器发请求,获取数据，更新资源
        doGet("/v1/plat/info").then(resp=>{
            if (resp) {
                this.platInfo=resp.data.date
            }
        })
        doGet("/v1/product/index").then(resp=>{
            if (resp) {
                this.product0=resp.data.date.xinShouBao
                this.product1=resp.data.date.youXuan
                this.product2=resp.data.date.sanBiao
            }
        })
    },
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