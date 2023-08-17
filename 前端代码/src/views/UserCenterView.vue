<template>
  <div>
      <project-header></project-header>
      <div>
          <div class="content clearfix">
              <!--个人信息-->
              <div class="user-head">
                  <div class="user-head-left fl">
                      <div class="user-head-img">
                          <img src="@/assets/image/user-head.png" alt="">
                      </div>
                  </div>
                  <div class="user-head-right fl">
                      <ul class="user-head-name fl">
                          <li><b v-if="userAccountInfo.name===''">未实名认证用户</b>
                              <b v-else>{{userAccountInfo.name}}</b></li>
                          <li>{{userAccountInfo.phone}}</li>
                          <li>最近登录：{{userAccountInfo.lastLoginTime}}</li>
                      </ul>
                      <div class="user-head-money fr">
                          <p>可用余额：<span>￥{{userAccountInfo.balance}}元</span></p>
                          <a href="javascript:void(0);" @click="golink('/page/user/userPay')" style="color: red" class="user-head-a1">充值</a>
                          <a href="javascript:void(0);" @click="golink('/page/product/list',{pType:1})"  style="color: red" class="user-head-a2">投资</a>
                      </div>
                  </div>

              </div>
              <!--记录-->
              <div class="user-record-box clearfix">
                  <div class="user-record user-record-1">
                      <h3 class="user-record-title">最近投资</h3>
                      <table align="center" width="388" border="0" cellspacing="0" cellpadding="0">
                          <thead class="datail_thead">
                          <tr>
                              <th>序号</th>
                              <th>投资产品</th>
                              <th>投资金额</th>
                              <th>投资时间</th>
                          </tr>
                          </thead>
                          <tbody>
                          <tr v-for="(item,index) in investDetail" :key="item.id">
                              <td v-if="item.id!=0">{{index+1}}</td>
                              <td v-if="item.id!=0">{{item.productName}}</td>
                              <td v-if="item.id!=0">{{item.bidMoney}}</td>
                              <td v-if="item.id!=0">{{item.bidTime}}</td>
                          </tr>
                          </tbody>
                      </table>
                      <!--无记录-->
                      <p class="user-record-no" v-if="investDetail[0].id==0">还没有投资记录，请投资：<a href="javascript:void(0);" @click="golink('/page/product/list',{pType:1})">投资</a></p>
                  </div>
                  <div class="user-record user-record-2">
                      <h3 class="user-record-title">最近充值</h3>
                      <table align="center" width="388" border="0" cellspacing="0" cellpadding="0">
                          <thead class="datail_thead">
                          <tr>
                              <th>序号</th>
                              <th>充值结果</th>
                              <th>充值金额</th>
                              <th>充值时间</th>
                          </tr>
                          </thead>
                          <tbody>
                          <tr v-for="(item,index) in rechargeRecords" :key="item.id" >
                              <td v-if="item.id!=0">{{index+1}}</td>
                              <td v-if="item.id!=0">{{item.rechargeStatusDetail}}</td>
                              <td v-if="item.id!=0">{{item.rechargeMoney}}</td>
                              <td v-if="item.id!=0">{{item.rechargeTime}}</td>
                          </tr>
                          </tbody>
                      </table>
                      <!--无记录-->
                      <p class="user-record-no" v-if="rechargeRecords[0].id==0">还没有充值记录，请充值：<a href="javascript:void(0);" @click="golink('/page/user/userPay')">充值</a></p>
                  </div>
                  <div class="user-record user-record-3">
                      <h3 class="user-record-title ">最近收益</h3>
                      <table align="center" width="388" border="0" cellspacing="0" cellpadding="0">
                          <thead class="datail_thead">
                          <tr>
                              <th>序号</th>
                              <th>项目名称</th>
                              <th>到期时间</th>
                              <th>收益金额</th>
                          </tr>
                          </thead>
                          <tbody>
                          <tr v-for="(item,index) in incomeDetail" :key="item.id">
                              <td v-if="item.id!=0">{{index+1}}</td>
                              <td v-if="item.id!=0">{{item.productName}}</td>
                              <td v-if="item.id!=0">{{item.incomeDate}}</td>
                              <td v-if="item.id!=0">{{item.incomeMoney}}</td>
                          </tr>
                          </tbody>
                      </table>
                      <!--无记录-->
                      <p class="user-record-no" v-if="incomeDetail[0].id==0">还没有收益记录</p>
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
import {doGet} from "@/api/httpRequest";

export default {
    name: "UserCenterView",
    components:{
        // eslint-disable-next-line vue/no-unused-components
        ProjectHeader,
        // eslint-disable-next-line vue/no-unused-components
        ProjectFoot
    },

    data(){
        return{
            userAccountInfo:{
                lastLoginTime: "",
                balance: 0.00,
                phone: "",
                name: "",
                headerUrl: ""
            },
            rechargeRecords: [
                    {
                        id: 0,
                        uid: 0,
                        rechargeNo: "",
                        rechargeStatus: 0,
                        rechargeStatusDetail: "",
                        rechargeMoney: 0.00,
                        rechargeTime: "",
                        rechargeDesc: "",
                        channel: ""
                    }
            ],
            investDetail:[
                {
                    id: 0,
                    prodId: 0,
                    uid: 0,
                    bidMoney: 0.00,
                    bidTime: "",
                    bidStatus: 1,
                    productName: ""
                }
            ],
            incomeDetail:[
                {
                    id: 0,
                    uid: 0,
                    prodId: 0,
                    bidId: 0,
                    bidMoney: 0.00,
                    incomeDate: "",
                    incomeMoney: 0.00,
                    incomeStatus: 1,
                    productName: ""
                }
            ]

        }
    },
    mounted() {
        doGet("/v1/user/userAcountInfo").then(resp=>{
            if (resp && resp.data.code == 7) {
                this.userAccountInfo=resp.data.date;
            }
        })
        doGet("/v1/recharge/records").then(resp=>{
            if (resp && resp.data.code == 7) {
                this.rechargeRecords=resp.data.list;
            }
        })
        doGet("/v1/invest/detail").then(resp=>{
            if (resp && resp.data.code == 7) {
                this.investDetail=resp.data.list;
            }
        })
        doGet("/v1/income/detail").then(resp=>{
            if (resp && resp.data.code == 7) {
                this.incomeDetail=resp.data.list;
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