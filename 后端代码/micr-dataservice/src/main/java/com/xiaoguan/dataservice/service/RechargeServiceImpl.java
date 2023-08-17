package com.xiaoguan.dataservice.service;

import com.xiaoguan.api.model.FinanceAccount;
import com.xiaoguan.api.model.RechargeRecord;
import com.xiaoguan.api.service.RechargeService;
import com.xiaoguan.common.constant.YLBConstant;
import com.xiaoguan.common.util.CommonUtil;
import com.xiaoguan.dataservice.mapper.FinanceAccountMapper;
import com.xiaoguan.dataservice.mapper.RechargeRecordMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
@DubboService(interfaceClass = RechargeService.class,version = "1.0")
public class RechargeServiceImpl implements RechargeService {
    @Resource
    private RechargeRecordMapper rechargeRecordMapper;
    @Resource
    private FinanceAccountMapper financeAccountMapper;
    //根据userId查询充值服务
    @Override
    public List<RechargeRecord> queryRechargeByUid(Integer uid, Integer pageNo, Integer pageSize) {
        List<RechargeRecord> rechargeRecords=null;
        if(uid!=null&&uid>0){
            pageNo= CommonUtil.defaultPageNo(pageNo);
            pageSize=CommonUtil.defaultPageSize(pageSize);
            int offset=(pageNo-1)*pageSize;
            rechargeRecords=rechargeRecordMapper.selectByUid(uid,offset,pageSize);
        }
        return rechargeRecords;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized int handleKQNotify(String orderId, String payAmount, String payResult) {
        int result=0;//订单不存在
        int rows=0;
        //查询订单
        RechargeRecord rechargeRecord=rechargeRecordMapper.selectByRechargeNo(orderId);
        if(rechargeRecord!=null){
            if(rechargeRecord.getRechargeStatus()== YLBConstant.RECHARGE_STATUS_RECHARGING){
                //判断金额是否一致
                String fen=rechargeRecord.getRechargeMoney().multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString();
                if(fen.equals(payAmount)){
                    if("10".equals(payResult)){
                        rows=financeAccountMapper.updateAvailableMoneyByRecharge(rechargeRecord.getUid(),rechargeRecord.getRechargeMoney());
                        if(rows!=1){
                            throw new RuntimeException("充值更新账户失败");
                        }
                        rechargeRecord.setRechargeStatus(YLBConstant.RECHARGE_STATUS_SUCCESS);
                        int i = rechargeRecordMapper.updateByPrimaryKeySelective(rechargeRecord);
                        if(i!=1){
                            throw new RuntimeException("更新充值状态失败");
                        }
                        result=1;//表示成功
                    }else {
                        rechargeRecord.setRechargeStatus(YLBConstant.RECHARGE_STATUS_FAILD);
                        int i = rechargeRecordMapper.updateByPrimaryKeySelective(rechargeRecord);
                        if(i!=1){
                            throw new RuntimeException("更新充值状态失败");
                        }
                        result=4;//表示充值不成功
                    }
                }else {
                    rechargeRecord.setRechargeStatus(YLBConstant.RECHARGE_STATUS_FAILD);
                    int i = rechargeRecordMapper.updateByPrimaryKeySelective(rechargeRecord);
                    if(i!=1){
                        throw new RuntimeException("更新充值状态失败");
                    }
                    if(!"0".equals(payAmount)){
                        System.out.println("[警告]金额不一致！");
                    }
                    result=3;//金额不一致
                }
            }else {
                result=2;//订单已经处理过了
            }
        }
        return  result;
    }

    //添加充值记录
    @Override
    public int addRechargeRecode(RechargeRecord rechargeRecord) {
        int count=0;
        if(rechargeRecord!=null){
             count = rechargeRecordMapper.insertSelective(rechargeRecord);
        }
        return count;
    }

    @Override
    public Integer queryRechargeNumsByUid(Integer uid) {
        Integer count=0;
        if(uid!=null&&uid>0){
           count=rechargeRecordMapper.selectCountByUid(uid);
        }
        return count;
    }
}
