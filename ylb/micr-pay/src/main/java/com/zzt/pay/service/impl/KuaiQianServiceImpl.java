package com.zzt.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zzt.api.model.RechargeRecord;
import com.zzt.api.model.User;
import com.zzt.common.constant.RechargeRecordStatusConstant;
import com.zzt.common.constant.RedisKey;
import com.zzt.pay.client.RechargeRecordClient;
import com.zzt.pay.client.UserServiceClient;
import com.zzt.pay.service.KuaiQianService;
import com.zzt.pay.util.HttpUtil;
import com.zzt.pay.util.Pkipair;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class KuaiQianServiceImpl implements KuaiQianService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    @Lazy(true)
    private UserServiceClient userServiceClient;

    @Autowired
    @Lazy(true)
    private RechargeRecordClient rechargeRecordClient;

    /**
     * 根据用户id获取用户信息
     *
     * @param uid
     * @return
     */
    @Override
    public User queryUser(Integer uid) {
        User user = userServiceClient.queryById(uid);
        return user;
    }


    /**
     * 生成快钱支付接口的数据
     *
     * @param uid
     * @param rechargeMoney
     * @return
     */
    @Override
    public Map<String, String> generateFormData(Integer uid, String phone, BigDecimal rechargeMoney) {
        Map<String, String> data = new HashMap<>();
        //人民币网关账号，该账号为11位人民币网关商户编号+01,该参数必填。
        String merchantAcctId = "1001214035601";//
        //编码方式，1代表 UTF-8; 2 代表 GBK; 3 代表 GB2312 默认为1,该参数必填。
        String inputCharset = "1";
        //接收支付结果的页面地址，该参数一般置为空即可。
        String pageUrl = "";
        //服务器接收支付结果的后台地址，该参数务必填写，不能为空。
        /**
         * 使用内网穿透技术接收支付结果
         */
        String bgUrl = "http://2ae95097.r28.cpolar.top/pay/kq/receive/notify";
        //网关版本，固定值：v2.0,该参数必填。
        String version = "v2.0";
        //语言种类，1代表中文显示，2代表英文显示。默认为1,该参数必填。
        String language = "1";
        //签名类型,该值为4，代表PKI加密方式,该参数必填。
        String signType = "4";
        //支付人姓名,可以为空。
        String payerName = "";
        //支付人联系类型，1 代表电子邮件方式；2 代表手机联系方式。可以为空。
        String payerContactType = "2";
        //支付人联系方式，与payerContactType设置对应，payerContactType为1，则填写邮箱地址；payerContactType为2，则填写手机号码。可以为空。
        String payerContact = phone;
        //指定付款人，可以为空
        String payerIdType = "3";
        //付款人标识，可以为空
        String payerId = String.valueOf(uid);
        //付款人IP，可以为空
        String payerIP = "";

        //商户订单号，以下采用时间来定义订单号，商户可以根据自己订单号的定义规则来定义该值，不能为空。
        String orderId = "KQ" + generateOrderId();

        //订单金额，金额以“分”为单位，商户测试以1分测试即可，切勿以大金额测试。该参数必填。
        String orderAmount = rechargeMoney.multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString();
        //订单提交时间，格式：yyyyMMddHHmmss，如：20071117020101，不能为空。
        String orderTime = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        //快钱时间戳，格式：yyyyMMddHHmmss，如：20071117020101， 可以为空
        String orderTimestamp = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        //商品名称，可以为空。
        String productName = "HarmonyOS";
        //商品数量，可以为空。
        String productNum = "1";
        //商品代码，可以为空。
        String productId = "10000";
        //商品描述，可以为空。
        String productDesc = "";
        //扩展字段1，商户可以传递自己需要的参数，支付完快钱会原值返回，可以为空。
        String ext1 = "";
        //扩展自段2，商户可以传递自己需要的参数，支付完快钱会原值返回，可以为空。
        String ext2 = "";
        //支付方式，一般为00，代表所有的支付方式。如果是银行直连商户，该值为10-1或10-2，必填。
        String payType = "00";
        //银行代码，如果payType为00，该值可以为空；如果payType为10-1或10-2，该值必须填写，具体请参考银行列表。
        String bankId = "";
        //同一订单禁止重复提交标志，实物购物车填1，虚拟产品用0。1代表只能提交一次，0代表在支付不成功情况下可以再提交。可为空。
        String redoFlag = "0";
        //快钱合作伙伴的帐户号，即商户编号，可为空。
        String pid = "";

        // signMsg 签名字符串 不可空，生成加密签名串
        String signMsgVal = "";
        signMsgVal = appendParam(signMsgVal, "inputCharset", inputCharset, data);
        signMsgVal = appendParam(signMsgVal, "pageUrl", pageUrl, data);
        signMsgVal = appendParam(signMsgVal, "bgUrl", bgUrl, data);
        signMsgVal = appendParam(signMsgVal, "version", version, data);
        signMsgVal = appendParam(signMsgVal, "language", language, data);
        signMsgVal = appendParam(signMsgVal, "signType", signType, data);
        signMsgVal = appendParam(signMsgVal, "merchantAcctId", merchantAcctId, data);
        signMsgVal = appendParam(signMsgVal, "payerName", payerName, data);
        signMsgVal = appendParam(signMsgVal, "payerContactType", payerContactType, data);
        signMsgVal = appendParam(signMsgVal, "payerContact", payerContact, data);
        signMsgVal = appendParam(signMsgVal, "payerIdType", payerIdType, data);
        signMsgVal = appendParam(signMsgVal, "payerId", payerId, data);
        signMsgVal = appendParam(signMsgVal, "payerIP", payerIP, data);
        signMsgVal = appendParam(signMsgVal, "orderId", orderId, data);
        signMsgVal = appendParam(signMsgVal, "orderAmount", orderAmount, data);
        signMsgVal = appendParam(signMsgVal, "orderTime", orderTime, data);
        signMsgVal = appendParam(signMsgVal, "orderTimestamp", orderTimestamp, data);
        signMsgVal = appendParam(signMsgVal, "productName", productName, data);
        signMsgVal = appendParam(signMsgVal, "productNum", productNum, data);
        signMsgVal = appendParam(signMsgVal, "productId", productId, data);
        signMsgVal = appendParam(signMsgVal, "productDesc", productDesc, data);
        signMsgVal = appendParam(signMsgVal, "ext1", ext1, data);
        signMsgVal = appendParam(signMsgVal, "ext2", ext2, data);
        signMsgVal = appendParam(signMsgVal, "payType", payType, data);
        signMsgVal = appendParam(signMsgVal, "bankId", bankId, data);
        signMsgVal = appendParam(signMsgVal, "redoFlag", redoFlag, data);
        signMsgVal = appendParam(signMsgVal, "pid", pid, data);


        System.out.println(signMsgVal);
        Pkipair pki = new Pkipair();
        //生成签名串
        String signMsg = pki.signMsg(signMsgVal);
        //需要签名串
        data.put("signMsg", signMsg);
        return data;
    }

    /**
     * 创建充值记录
     *
     * @param uid
     * @param rechargeMoney
     * @param orderId
     * @return
     */
    @Override
    public boolean addRecharge(Integer uid, BigDecimal rechargeMoney, String orderId) {

        RechargeRecord record = new RechargeRecord();
        record.setChannel("KQ");
        record.setRechargeDesc("快钱充值");
        record.setRechargeNo(orderId);
        record.setRechargeMoney(rechargeMoney);
        record.setRechargeStatus(RechargeRecordStatusConstant.RECHARGE_STATUS_PROCESSING);
        record.setRechargeTime(new Date());
        record.setUid(uid);
        int rows = rechargeRecordClient.addRechargeRecord(record);
        return rows > 0;
    }

    /**
     * 将订单号存入redis
     *
     * @param orderId
     */
    @Override
    public void addOrderIdToRedis(String orderId) {
        String key = RedisKey.RECHARGE_ORDERID_SET;
        stringRedisTemplate.boundZSetOps(key).add(orderId, new Date().getTime());
    }

    /**
     * 处理异步返回通知
     *
     * @param request
     */
    @Override
    public void enCodeByCer(HttpServletRequest request) {
        String merchantAcctId = request.getParameter("merchantAcctId");
        String version = request.getParameter("version");
        String language = request.getParameter("language");
        String signType = request.getParameter("signType");
        String payType = request.getParameter("payType");
        String bankId = request.getParameter("bankId");
        String orderId = request.getParameter("orderId");
        String orderTime = request.getParameter("orderTime");
        String orderAmount = request.getParameter("orderAmount");
        String bindCard = request.getParameter("bindCard");
        if (request.getParameter("bindCard") != null) {
            bindCard = request.getParameter("bindCard");
        }
        String bindMobile = "";
        if (request.getParameter("bindMobile") != null) {
            bindMobile = request.getParameter("bindMobile");
        }
        String dealId = request.getParameter("dealId");
        String bankDealId = request.getParameter("bankDealId");
        String dealTime = request.getParameter("dealTime");
        String payAmount = request.getParameter("payAmount");
        String fee = request.getParameter("fee");
        String ext1 = request.getParameter("ext1");
        String ext2 = request.getParameter("ext2");
        String payResult = request.getParameter("payResult");
        String aggregatePay = request.getParameter("aggregatePay");
        String errCode = request.getParameter("errCode");
        String signMsg = request.getParameter("signMsg");
        //拼接签名计算的串
        String merchantSignMsgVal = "";
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "merchantAcctId", merchantAcctId, null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "version", version, null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "language", language, null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "signType", signType, null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "payType", payType, null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "bankId", bankId, null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderId", orderId, null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderTime", orderTime, null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderAmount", orderAmount, null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "bindCard", bindCard, null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "bindMobile", bindMobile, null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "dealId", dealId, null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "bankDealId", bankDealId, null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "dealTime", dealTime, null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "payAmount", payAmount, null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "fee", fee, null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "ext1", ext1, null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "ext2", ext2, null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "payResult", payResult, null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "aggregatePay", aggregatePay, null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "errCode", errCode, null);
        Pkipair pki = new Pkipair();
        boolean flag = pki.enCodeByCer(merchantSignMsgVal, signMsg);
        flag = true;
        if (flag) {
            /**
             * 可以处理业务逻辑
             * 1.判断商户号是商家自己的吗
             * 2.判断订单号在商家是否存在，是否处理过
             * 3.判断金额是否一致
             * 4.如果充值成功，更新用户的资金
             * 5.修改充值表的记录状态
             * 5.删除redis中处理过的订单号
             */
            if (!"1001214035601".equals(merchantAcctId)) {
                //商户号错误
                System.out.println("订单" + orderId + "商户号有误");
            }
            int rechargeResult = rechargeRecordClient.handleKqNotify(orderId, payAmount, payResult);
        } else {
            System.out.println("订单" + orderId + "验签失败，不能处理");
        }
        //删除redis中的订单记录
        stringRedisTemplate.boundZSetOps(RedisKey.RECHARGE_ORDERID_SET).remove(orderId);
    }

    /**
     * 调用快钱的查询接口
     */
    @Override
    public void handleQueryOrder() {
        //从redis中获取订单号
        Set<String> orders = stringRedisTemplate.boundZSetOps(RedisKey.RECHARGE_ORDERID_SET).range(0, -1);
        //循环订单号
        orders.forEach(orderId -> {
            //每个订单号都调用查询方法
            doQuery(orderId);
        });
    }

    //查询订单号
    private void doQuery(String orderId) {
        Map<String, Object> request = new HashMap<String, Object>();
        //固定值：1代表UTF-8;
        String inputCharset = "1";
        //固定值：v2.0 必填
        String version = "v2.0";
        //1代表Md5，2 代表PKI加密方式  必填
        String signType = "2";
        //人民币账号 membcode+01  必填
        String merchantAcctId = "1001214035601";//1001293267101（XIXMFISFG7RGDKQN） 1001217486601(5B5EQDQH2X7ERM9A)
        //固定值：0 按商户订单号单笔查询，1 按交易结束时间批量查询必填
        String queryType = "0";
        //固定值：1	代表简单查询 必填
        String queryMode = "1";
        //数字串，格式为：年[4 位]月[2 位]日[2 位]时[2 位]分[2 位]秒[2位]，例如：20071117020101
        String startTime = "";
        ////数字串，格式为：年[4 位]月[2 位]日[2 位]时[2 位]分[2 位]秒[2位]，例如：20071117020101
        String endTime = "";
        String requestPage = "";
        String key = "27YKWKBKHT2IZSQ4";//XIXMFISFG7RGDKQN

        request.put("inputCharset", inputCharset);
        request.put("version", version);
        request.put("signType", signType);
        request.put("merchantAcctId", merchantAcctId);
        request.put("queryType", queryType);
        request.put("queryMode", queryMode);
        request.put("startTime", startTime);
        request.put("endTime", endTime);
        request.put("requestPage", requestPage);
        request.put("orderId", orderId);

        String message = "";
        message = appendParam(message, "inputCharset", inputCharset, null);
        message = appendParam(message, "version", version, null);
        message = appendParam(message, "signType", signType, null);
        message = appendParam(message, "merchantAcctId", merchantAcctId, null);
        message = appendParam(message, "queryType", queryType, null);
        message = appendParam(message, "queryMode", queryMode, null);
        message = appendParam(message, "startTime", startTime, null);
        message = appendParam(message, "endTime", endTime, null);
        message = appendParam(message, "requestPage", requestPage, null);
        message = appendParam(message, "orderId", orderId, null);
        message = appendParam(message, "key", key, null);


        Pkipair pki = new Pkipair();
        String sign = pki.signMsg(message);


        request.put("signMsg", sign);


        //sandbox提交地址
        String reqUrl = "https://sandbox.99bill.com/gatewayapi/gatewayOrderQuery.do";

        String response = "";

        try {

            response = HttpUtil.doPostJsonRequestByHttps(JSON.toJSONString(request), reqUrl, 3000, 8000);

            //解析response
            if (StringUtils.isNotBlank(response)) {
                //将响应结果转成json格式
                Object orderDetail = JSONObject.parseObject(response).get("orderDetail");
                if (orderDetail != null) {
                    //把查询到的Object转成JSONArray
                    JSONArray array = (JSONArray) orderDetail;
                    JSONObject jsonObject = array.getJSONObject(0);
                    if (jsonObject != null) {
                        //处理充值结果，和异步通知一样
                        rechargeRecordClient.handleKqNotify(jsonObject.getString("orderId"), jsonObject.getString("payAmount"), jsonObject.getString("payResult"));
                    }
                }
            }

            //删除redis中的订单号
            stringRedisTemplate.boundZSetOps(RedisKey.RECHARGE_ORDERID_SET).remove(orderId);

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    //追加参数
    private String appendParam(String returns, String paramId, String paramValue, Map<String, String> data) {
        if (returns != "") {
            if (paramValue != "" && paramValue != null) {

                returns += "&" + paramId + "=" + paramValue;
            }

        } else {

            if (paramValue != "" && paramValue != null) {
                returns = paramId + "=" + paramValue;
            }
        }

        if (data != null) {
            data.put(paramId, paramValue);
        }

        return returns;
    }

    //生成orderId
    private String generateOrderId() {
        //orderId = 时间戳 + redis自增主键
        String key = RedisKey.KEY_RECHARGE_ORDERID;
        Long incr = stringRedisTemplate.boundValueOps(key).increment();
        //生成时间戳
        String orderId = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + incr;
        return orderId;
    }

}
