package com.xiaoguan.dataservice.service;

import com.xiaoguan.api.model.FinanceAccount;
import com.xiaoguan.api.model.InvestInfo;
import com.xiaoguan.api.model.ProductInfo;
import com.xiaoguan.api.model.User;
import com.xiaoguan.api.pojo.BidInfoProduct;
import com.xiaoguan.api.pojo.InvestInfoPlus;
import com.xiaoguan.api.service.InvestService;
import com.xiaoguan.common.constant.RedisKey;
import com.xiaoguan.common.constant.YLBConstant;
import com.xiaoguan.common.util.CommonUtil;
import com.xiaoguan.dataservice.exception.AccountUpdateException;
import com.xiaoguan.dataservice.exception.InvestUpdateException;
import com.xiaoguan.dataservice.exception.ProductUpdateException;
import com.xiaoguan.dataservice.mapper.FinanceAccountMapper;
import com.xiaoguan.dataservice.mapper.InvestInfoMapper;
import com.xiaoguan.dataservice.mapper.ProductInfoMapper;
import com.xiaoguan.dataservice.mapper.UserMapper;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@DubboService(interfaceClass = InvestService.class,version = "1.0")
public class InvestServeImpl implements InvestService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private InvestInfoMapper investInfoMapper;
    @Resource
    private FinanceAccountMapper financeAccountMapper;
    @Resource
    private ProductInfoMapper productInfoMapper;
    @Resource
    private UserMapper userMapper;
    //根据用户信息查询投资记录
    @Override
    public List<InvestInfoPlus> queryInvestInfoByUid(Integer uid, Integer pageNo, Integer pageSize) {
        List<InvestInfoPlus> investInfoPluses=null;
        if(uid!=null&&uid>0){
            pageNo= CommonUtil.defaultPageNo(pageNo);
            pageSize=CommonUtil.defaultPageSize(pageSize);
            int offset=(pageNo-1)*pageSize;
            investInfoPluses=investInfoMapper.selectByUid(uid,offset,pageSize);
        }
        return investInfoPluses;
    }

    @Override
    public Integer queryInvestNumsByUid(Integer uid) {
        Integer count=0;
        if(uid!=null&&uid>0){
            count=investInfoMapper.selectCountByUid(uid);
        }
        return count;
    }

    /**
     * result=0未知异常
     * result=1可以正常操作
     * result=2 账户钱不够
     * result=3 产品不存在
     * result=4 超出产品剩余投资金额
     * result=5 不符合产品个人投资区间
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer investProduct(Integer uid, Integer productId, BigDecimal money) throws InvestUpdateException, ProductUpdateException, AccountUpdateException {
       Integer result=0;
        if(uid!=null&&uid>0&&productId!=null&&productId>0&&money!=null&&money.doubleValue()>0){
             //查询账户资金
            FinanceAccount financeAccount=financeAccountMapper.selectBuUidForUpdate(uid);
            if (financeAccount!=null&&financeAccount.getAvailableMoney()!=null&&
                    (financeAccount.getAvailableMoney().compareTo(money)==1||
                            financeAccount.getAvailableMoney().compareTo(money)==0)){
                //检查账户是否能购买
                ProductInfo productInfo=productInfoMapper.selectByPrimaryKeyForUpdate(productId);
                if(productInfo!=null&&productInfo.getLeftProductMoney()!=null&&productInfo.getBidMinLimit()!=null
                        &&productInfo.getBidMaxLimit()!=null){
                    if(productInfo.getProductStatus()==YLBConstant.PRODUCT_STATUS_SOLD
                            &&(productInfo.getLeftProductMoney().compareTo(money)==0
                            ||productInfo.getLeftProductMoney().compareTo(money)==1)){
                        if((productInfo.getBidMinLimit().compareTo(money)==-1||productInfo.getBidMinLimit().compareTo(money)==0)
                        &&(productInfo.getBidMaxLimit().compareTo(money)==1||productInfo.getBidMaxLimit().compareTo(money)==0)){
                            financeAccount.setAvailableMoney(financeAccount.getAvailableMoney().subtract(money));
                            int i = financeAccountMapper.updateByPrimaryKeySelective(financeAccount);
                            if(i==1){
                                productInfo.setLeftProductMoney(productInfo.getLeftProductMoney().subtract(money));
                                if(productInfo.getLeftProductMoney().compareTo(new BigDecimal("0"))==0){
                                    productInfo.setProductStatus(YLBConstant.PRODUCT_STATUS_UNSOLD);
                                    productInfo.setProductFullTime(new Date());
                                }
                                int count = productInfoMapper.updateByPrimaryKeySelective(productInfo);
                                if(count==1){
                                    //创建投资记录
                                    InvestInfo investInfo = new InvestInfo();
                                    investInfo.setBidMoney(money);
                                    investInfo.setBidStatus(YLBConstant.INVEST_SUC);
                                    investInfo.setProdId(productId);
                                    investInfo.setUid(uid);
                                    investInfo.setBidTime(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
                                    int count2 = investInfoMapper.insertSelective(investInfo);
                                    if(count2==1){
                                        result=1;
                                    } else {
                                        throw new InvestUpdateException("投资信息更新失败");
                                    }
                                } else {
                                    throw new ProductUpdateException("产品更新失败");
                                }
                            } else {
                                throw new AccountUpdateException("账户信息更新失败");
                            }
                        }else {
                            result=5;
                        }
                    }else {
                        result=4;
                    }
                }else {
                    result=3;
                }

            }else {
                result=2;
            }

        }
       return  result;
    }
    //更新投资排行榜
    @Override
    public void modifyInvestRank(Integer uid, BigDecimal money) {
        User user =null;
        if(uid!=null&&money!=null&&money.compareTo(new BigDecimal(0))==1&&uid>0){
            user=userMapper.selectByPrimaryKey(uid);
            if (user!=null) {
                //更新redis中的投资排行榜
                String key= RedisKey.KEY_INVEST_RANK;
                stringRedisTemplate.boundZSetOps(key).incrementScore(user.getPhone(),money.doubleValue());

            }
        }
    }

    //查询某个产品的投资记录
    @Override
    public List<BidInfoProduct> queryBidListByProductId(Integer proId, Integer pageNo, Integer pageSize) {
        List<BidInfoProduct> bidInfoProducts=new ArrayList<>();
        if (proId!=null&&proId>0) {
            pageNo= CommonUtil.defaultPageNo(pageNo);
            pageSize=CommonUtil.defaultPageSize(pageSize);
            Integer offset=(pageNo-1)*pageSize;
            bidInfoProducts=investInfoMapper.selectBidInfoByProductId(proId,offset,pageSize);
        }
        return bidInfoProducts;
    }

}
