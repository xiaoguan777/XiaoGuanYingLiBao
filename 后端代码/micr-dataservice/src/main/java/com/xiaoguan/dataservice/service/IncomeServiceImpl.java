package com.xiaoguan.dataservice.service;

import com.xiaoguan.api.model.*;
import com.xiaoguan.api.pojo.IncomeInfoPlus;
import com.xiaoguan.api.service.IncomeService;
import com.xiaoguan.common.constant.YLBConstant;
import com.xiaoguan.common.util.CommonUtil;
import com.xiaoguan.dataservice.exception.AccountUpdateException;
import com.xiaoguan.dataservice.exception.IncomeUpdateException;
import com.xiaoguan.dataservice.exception.ProductUpdateException;
import com.xiaoguan.dataservice.mapper.FinanceAccountMapper;
import com.xiaoguan.dataservice.mapper.IncomeInfoMapper;
import com.xiaoguan.dataservice.mapper.InvestInfoMapper;
import com.xiaoguan.dataservice.mapper.ProductInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@DubboService(interfaceClass = IncomeService.class,version = "1.0")
public class IncomeServiceImpl implements IncomeService {
    @Resource
    private IncomeInfoMapper incomeInfoMapper;
    @Resource
    private ProductInfoMapper productInfoMapper;
    @Resource
    private InvestInfoMapper investInfoMapper;
    @Resource
    private FinanceAccountMapper financeAccountMapper;
    @Override
    public List<IncomeInfoPlus> queryIncomeInfoByUid(Integer uid, Integer pageNo, Integer pageSize) {
        List<IncomeInfoPlus> incomeRecords=null;
        if(uid!=null&&uid>0){
            pageNo= CommonUtil.defaultPageNo(pageNo);
            pageSize=CommonUtil.defaultPageSize(pageSize);
            int offset=(pageNo-1)*pageSize;
            incomeRecords= incomeInfoMapper.selectByUid(uid,offset,pageSize);
        }
        return incomeRecords;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized void generateIncomePlan() throws IncomeUpdateException, ProductUpdateException {
        List<ProductInfo> productInfos= productInfoMapper.selectByStatus(YLBConstant.PRODUCT_STATUS_UNSOLD);
        BigDecimal dayRate=null;
        BigDecimal income=null;
        BigDecimal cycle=null;
        Date incomeDate=null;

        for(ProductInfo productInfo:productInfos){
            dayRate=productInfo.getRate().divide(new BigDecimal("365"),10,BigDecimal.ROUND_HALF_UP)
                    .divide(new BigDecimal("100"),10, RoundingMode.HALF_UP);
            if( productInfo.getProductType()==YLBConstant.PRODUCT_TYPE_XINSHOUBAO_0){
                cycle=new BigDecimal(productInfo.getCycle());
            }else {
                cycle=new BigDecimal(productInfo.getCycle()*30);
            }
            List<InvestInfo> investInfos= investInfoMapper.selectBidInfoByProId(productInfo.getId());
            for(InvestInfo investInfo :investInfos){
                income=investInfo.getBidMoney().multiply(dayRate).multiply(cycle);
                incomeDate= DateUtils.addDays(productInfo.getProductFullTime(),cycle.intValue());
                IncomeInfo incomeInfo =new IncomeInfo();
                incomeInfo.setUid(investInfo.getUid());
                incomeInfo.setProdId(productInfo.getId());
                incomeInfo.setBidId(investInfo.getId());
                incomeInfo.setBidMoney(investInfo.getBidMoney());
                incomeInfo.setIncomeDate(DateFormatUtils.format(incomeDate,"yyyy-MM-dd HH-mm-ss"));
                incomeInfo.setIncomeMoney(income);
                incomeInfo.setIncomeStatus(YLBConstant.INCOME_BEGIN);
                int count = incomeInfoMapper.insertSelective(incomeInfo);
                if(count!=1){
                    throw new IncomeUpdateException("收益表更新失败");
                }
            }
            productInfo.setProductStatus(YLBConstant.PRODUCT_STATUS_UNSOLD_AND_INCOME);
            int i = productInfoMapper.updateByPrimaryKeySelective(productInfo);
            if(i!=1){
                throw new ProductUpdateException("产品更新失败");
            }
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void InvokeIncomeBack() throws AccountUpdateException, IncomeUpdateException {
        Date curTime=new Date();
        List<IncomeInfo> incomeInfos= incomeInfoMapper.selectByDeadline(curTime,YLBConstant.INCOME_BEGIN);//查比当前时间早的记录并且投资收益没有返还
        for(IncomeInfo incomeInfo:incomeInfos){
            BigDecimal returnMoney=new BigDecimal("0");
            returnMoney=returnMoney.add(incomeInfo.getBidMoney()).add(incomeInfo.getIncomeMoney());
            FinanceAccount financeAccount = financeAccountMapper.selectBuUidForUpdate(incomeInfo.getUid());
            BigDecimal sumMoney = financeAccount.getAvailableMoney().add(returnMoney);
            financeAccount.setAvailableMoney(sumMoney);
            int count = financeAccountMapper.updateByPrimaryKeySelective(financeAccount);
            if (count!=1) {
                throw new AccountUpdateException("账户更新错误");
            }
            incomeInfo.setIncomeStatus(YLBConstant.INCOME_END);
            int i = incomeInfoMapper.updateByPrimaryKeySelective(incomeInfo);
            if(i!=1){
                throw new IncomeUpdateException("收益表更新错误");
            }
        }

    }

    @Override
    public Integer queryIncomeNumsByUid(Integer uid) {
        Integer count=0;
        if(uid!=null&&uid>0){
            count=incomeInfoMapper.selectCountByUid(uid);
        }
        return count;
    }
}
