<template>

  <div>
      <ProjectHeader></ProjectHeader>
      <div class="content clearfix">
          <div class="detail-left">
              <div class="detail-left-title">{{product.productName}}（{{product.productNo}}期）</div>
              <ul class="detail-left-number">
                  <li>
                      <span>历史年化收益率</span>
                      <p><b>{{product.rate}}</b>%</p>
                      <span>历史年化收益率</span>
                  </li>
                  <li>
                      <span>募集金额（元）</span>
                      <p><b>{{product.productMoney}}</b>元</p>
                      <span v-if="product.leftProductMoney>0">募集中&nbsp;&nbsp;剩余募集金额{{product.leftProductMoney}}元</span>
                      <span v-else>已经售完！</span>
                  </li>
                  <li>
                      <span>投资周期</span>
                      <p v-if="product.productType==0"><b>{{product.cycle}}</b>天</p>
                      <p v-else><b>{{product.cycle}}</b>个月</p>
                  </li>

              </ul>
              <div class="detail-left-way">
                  <span>投资规则</span>
                  <span>个人单笔最大投资金额：<i>{{product.bidMaxLimit}}</i></span>
                  <span>个人单笔最小投资金额：<i>{{product.bidMinLimit}}</i></span>
                  <span>收益返还方式：<i>售完计息，到期自动返还本息</i></span>
              </div>
              <!--投资记录-->
              <div class="datail-record">
                  <h2 class="datail-record-title">投资记录</h2>
                  <div class="datail-record-list">
                      <table align="center" width="880" border="0" cellspacing="0" cellpadding="0">
                          <colgroup>
                              <col style="width: 72px" />
                              <col style="width: 203px" />
                              <col style="width: 251px" />
                              <col style="width: 354px" />
                          </colgroup>
                          <thead class="datail_thead">
                          <tr>
                              <th>序号</th>
                              <th>投资人</th>
                              <th>投资金额（元）</th>
                              <th>投资时间</th>
                          </tr>
                          </thead>
                          <tbody>
                          <tr v-for="(bidInfo,index) in bidInfoList" :key="bidInfo.id">
                              <td>{{index+1}}</td>
                              <td class="datail-record-phone">{{bidInfo.tuoMinPhone}}</td>
                              <td>{{bidInfo.bidMoney}}</td>
                              <td>{{bidInfo.bidTime}}</td>
                          </tr>
                          </tbody>
                      </table>
                  </div>
              </div>

          </div>
          <!--右侧-->
          <div class="detail-right">
              <div class="detail-right-title">立即投资</div>
              <div class="detail-right-mode">
                  <h3 class="detail-right-mode-title">收益方式</h3>
                  <p class="detail-right-mode-p"><span>到期还本付息</span></p>
                  <h3 class="detail-right-mode-title">我的账户可用</h3>
                  <div class="detail-right-mode-rmb">
                      <p>资金（元）：{{accountMoney}}</p>
                      <a href="javascript:void(0);" @click="golink('/page/user/login')" v-if="loginStatus===false">请登录</a>
                  </div>
                  <h3 class="detail-right-mode-title">预计本息收入为{{incomeMoney}}（元）</h3>
                  <form action="" id="number_submit">
                      <p>请在下方输入投资金额</p>
                      <input type="text" placeholder="请输入日投资金额，应为100元整倍数" v-model="investMoney" @keyup='checkInvestMoney' class="number-money" >
                      <div class="err">{{moneyErr}}</div>
                      <input type="button" value="立即投资" @click='investProject' class="submit-btn">
                  </form>


              </div>

          </div>
      </div>
      <ProjectFoot></ProjectFoot>
  </div>
</template>

<script>
import ProjectHeader from "@/components/common/ProjectHeader.vue";
import ProjectFoot from "@/components/common/ProjectFoot.vue";
import {doGet, doPost} from "@/api/httpRequest";
import layx from "vue-layx";

export default {
    name: "ProductDetail",
    components:{
        // eslint-disable-next-line vue/no-unused-components
        ProjectHeader,
        // eslint-disable-next-line vue/no-unused-components
        ProjectFoot
    },
    data(){
        return {
            product:{
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
                productDesc: "无"
            },
            bidInfoList:[
                {
                    id: 0,
                    tuoMinPhone: "***********",
                    bidTime: "",
                    bidMoney: 0.00
                }
            ],
            loginStatus:false,
            accountMoney:'******',
            investMoney:'',
            moneyErr:'',
            incomeMoney:'***'

        }
    },
    mounted() {
        this.initpage();


    },
    methods:{
        golink(url,params){
            this.$router.push({
                path:url,
                query:params
            })
        },
        checkInvestMoney(){
            if(this.investMoney===''){
                this.moneyErr='金额为空'
                this.incomeMoney='***'
            }else if(isNaN(this.investMoney)){//不是数字
                this.moneyErr='请输入正确的金额'
                this.incomeMoney='***'
            }else if(this.investMoney<this.product.bidMinLimit||this.investMoney>this.product.bidMaxLimit){
                this.moneyErr='投资金额不符合产品要求区间'
                //计算利息
                let dataRate=this.product.rate/365.0/100;
                if(this.product.productType===0){
                    this.incomeMoney=(this.investMoney*(1+this.product.cycle*dataRate)).toFixed(3);
                }else {
                    this.incomeMoney=(this.investMoney*(1+this.product.cycle*dataRate*30)).toFixed(3);
                }
            }else {
                this.moneyErr=''
                //计算利息
                let dataRate=this.product.rate/365.0/100;
                if(this.product.productType===0){
                    this.incomeMoney=(this.investMoney*(1+this.product.cycle*dataRate)).toFixed(3);
                }else {
                    this.incomeMoney=(this.investMoney*(1+this.product.cycle*dataRate*30)).toFixed(3);
                }
            }
        },
        investProject(){
            this.checkInvestMoney();
            if(this.product.id!=null&&this.product.id>0&&this.moneyErr===''){
                doPost('/v1/invest/product',{productId:this.product.id,money:this.investMoney}).then(resp=>{
                    if (resp&&resp.data.code===7) {
                        this.initpage();
                        layx.msg('投资成功',{dialogIcon: 'success',position:'tc',autodestroy:1500})
                    }else {
                        layx.msg(resp.data.msg,{dialogIcon: 'warn',position:'tc',autodestroy:1200})
                    }
                })
            }else {
                layx.msg("请检查金额是否正确！",{dialogIcon: 'warn',position:'tc',autodestroy:1200})
            }
        },
        initpage(){
            let productId=this.$route.query.productId;
            doGet("v1/productInvest/info",{productId:productId}).then(resp=>{
                if (resp) {
                    this.product=resp.data.date;
                    this.bidInfoList=resp.data.list;
                }
            })
            doGet("/v1/user/judge/login").then(resp=>{
                if (resp&&resp.data.code===7) {
                    this.loginStatus=true;
                    doGet("/v1/user/userAcountInfo").then(resp=>{
                        if (resp&&resp.data.code===7) {
                            this.accountMoney=resp.data.date.balance;
                        }
                    })
                }
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