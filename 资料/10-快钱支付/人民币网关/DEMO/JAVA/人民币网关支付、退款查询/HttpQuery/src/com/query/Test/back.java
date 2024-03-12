package com.query.Test;


import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.alibaba.fastjson.JSON;
import com.query.Util.Pkipair;

public class back{
	public static Log logger = LogFactory.getLog(back.class);
	public static void main(String[] args) {
		gateWayBack();
	}
	/**
	 * 支付查询demo
	 */
	public static void gateWayBack(){
		
		//商户编号，线上的话改成你们自己的商户编号的，发到商户的注册快钱账户邮箱的
		String merchant_id = "10012921171";  //10012138842,10012921171
		//退款接口版本号 目前固定为此值
		String version = "bill_drawback_api_1";
		//操作类型
		String command_type = "001";
		//退款流水号  字符串
		String txOrder ="TK" + new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
		//退款金额，整数或小数，小数位为2位   以人民币元为单位
		String amount = "0.1";
		//退款提交时间 数字串，一共14位 格式为：年[4 位]月[2 位]日[2 位]时[2 位]分[2 位]秒[2位]
		String postdate =new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
		//原商户订单号
		String orderid = "KQ20201117164134";//
		//原交易收款账户
		String payeeidsrc = "";//如果version为bill_drawback_api_3，则此字段不能为空。表示原交易的收款账户（快钱用户的email地址）
		//分账退款标识
		//String returnSharingFlag = "";//为空代表不是分账退款；1为分账退款；

		//分账退款明细
		//String returnData = "2^0330^0.01^ext1|2^0320^0.02^ext2";

		
		Map<String, Object> request = new HashMap<String, Object>();
		request.put("merchant_id", merchant_id);
		request.put("version", version);
		request.put("command_type", command_type);
		request.put("orderid", orderid);
		request.put("amount", amount);
		request.put("postdate", postdate);				
		request.put("txOrder", txOrder);		
	    //request.put("returnSharingFlag", returnSharingFlag);
		//request.put("returnData", returnData);
		
		String message="";
		message = appendParam(message,"merchant_id",merchant_id);
		message = appendParam(message,"version",version);
		message = appendParam(message,"command_type",command_type);
		message = appendParam(message,"orderid",orderid);
		message = appendParam(message,"amount",amount);
		message = appendParam(message,"postdate",postdate);
		message = appendParam(message,"txOrder",txOrder);
		//message = appendParam(message,"returnSharingFlag",returnSharingFlag);
		//message = appendParam(message,"returnData",returnData);
		
		System.out.println("参与加签字段===" + message);
		
		Pkipair pki = new Pkipair();
		String sign = pki.signMsg(message);
		
		System.out.println("签名串signMsg===" + sign);
		
		request.put("mac", sign);
		
		System.out.println("请求json串===" + JSON.toJSONString(request));
		
		//sandbox提交地址
		String reqUrl = "https://sandbox.99bill.com/gatewayapi/recievedrawbackPKI.do";
		
		String response = "";
		
		try {

			response = HttpUtil.doPostJsonRequestByHttps(JSON.toJSONString(request), reqUrl, 3000, 8000);

			Map<String,Object> m = new HashMap<String, Object>();
			m = JSON.parseObject(response, Map.class);
			System.out.println("返回json串==="+response);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	public  static String appendParam(String result, String paramId, String paramValue) {
		if (result != "") {
			if (paramValue != "") {

				result += "&" + paramId + "=" + paramValue;
			}

		} else {

			if (paramValue != "") {
				result = paramId + "=" + paramValue;
			}
		}

		return result;
	}

	
}
