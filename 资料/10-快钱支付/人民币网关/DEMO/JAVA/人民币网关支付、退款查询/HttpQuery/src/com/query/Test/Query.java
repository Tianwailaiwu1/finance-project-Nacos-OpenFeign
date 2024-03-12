package com.query.Test;


import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.alibaba.fastjson.JSON;
import com.query.Util.Pkipair;

public class Query{
	public static Log logger = LogFactory.getLog(Query.class);
	public static void main(String[] args) {
		gateWayInitQuery();
	}
	/**
	 * 支付查询demo
	 */
	public static void gateWayInitQuery(){
		Map<String, Object> request = new HashMap<String, Object>();
		//固定值：1代表UTF-8; 
		String inputCharset = "1";
		//固定值：v2.0 必填
		String version = "v2.0";
		//1代表Md5，2 代表PKI加密方式  必填
		String signType = "2";
		//人民币账号 membcode+01  必填
		String merchantAcctId = "1001292117101";//1001293267101（XIXMFISFG7RGDKQN） 1001217486601(5B5EQDQH2X7ERM9A)
		//固定值：0 按商户订单号单笔查询，1 按交易结束时间批量查询必填
		String queryType = "0";
		//固定值：1	代表简单查询 必填
		String queryMode = "1";
		//数字串，格式为：年[4 位]月[2 位]日[2 位]时[2 位]分[2 位]秒[2位]，例如：20071117020101
		String startTime = "20201117000000";//20200525000000
		////数字串，格式为：年[4 位]月[2 位]日[2 位]时[2 位]分[2 位]秒[2位]，例如：20071117020101
		String endTime = "20201119190000";	//	20200527180000
		String requestPage = "";
		String orderId = "W02012011000000112";	// 20200526180806TS
		String key = "D4TSSG89AX2A596A";//XIXMFISFG7RGDKQN
		
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
		
		String message="";
		message = appendParam(message,"inputCharset",inputCharset);
		message = appendParam(message,"version",version);
		message = appendParam(message,"signType",signType);
		message = appendParam(message,"merchantAcctId",merchantAcctId);
		message = appendParam(message,"queryType",queryType);
		message = appendParam(message,"queryMode",queryMode);
		message = appendParam(message,"startTime",startTime);
		message = appendParam(message,"endTime",endTime);
		message = appendParam(message,"requestPage",requestPage);
		message = appendParam(message,"orderId",orderId);
		message = appendParam(message,"key",key);
		
		System.out.println("参与加签字段===" + message);
		
		Pkipair pki = new Pkipair();
		String sign = pki.signMsg(message);
		
		System.out.println("签名串signMsg===" + sign);
		
		request.put("signMsg", sign);
		
		System.out.println("请求json串===" + JSON.toJSONString(request));
		
		//sandbox提交地址
		String reqUrl = "https://sandbox.99bill.com/gatewayapi/gatewayOrderQuery.do";
		
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
