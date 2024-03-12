package com.query.Test;


import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.alibaba.fastjson.JSON;
import com.query.Util.Pkipair;

public class Queryback{
	public static Log logger = LogFactory.getLog(Queryback.class);
	public static void main(String[] args) {
		gateWayInitQuery();
	}
	/**
	 * �˿��ѯdemo
	 */
	public static void gateWayInitQuery(){
		Map<String, Object> request = new HashMap<String, Object>();


		String version = "v2.0";

		String signType = "2";

		String merchantAcctId = "1001292117101";//1001293267101 1001214035601 1001217486601 (5B5EQDQH2X7ERM9A)

		String startDate = "20201116";//20200525

		String endDate = "20201118";//20200527
		
		String orderId = "";
		 
		String requestPage = "1";
		
		String rOrderId = "KQ20201117164134";//20200526180806TS
		String seqId = "";
		String extra_output_column = "";		
		String key = "D4TSSG89AX2A596A";//2671：XIXMFISFG7RGDKQN:10012174866：5B5EQDQH2X7ERM9A
		
		request.put("version", version);
		request.put("signType", signType);
		request.put("merchantAcctId", merchantAcctId);
		request.put("startDate", startDate);
		request.put("endDate", endDate);
		request.put("orderId", orderId);
		request.put("requestPage", requestPage);
		request.put("rOrderId", rOrderId);		
		request.put("seqId", seqId);
		request.put("extra_output_column", extra_output_column);
				
		
		
		String message="";
		message = appendParam(message,"version",version);
		message = appendParam(message,"signType",signType);
		message = appendParam(message,"merchantAcctId",merchantAcctId);
		message = appendParam(message,"startDate",startDate);
		message = appendParam(message,"endDate",endDate);
		message = appendParam(message,"orderId",orderId);
		message = appendParam(message,"requestPage",requestPage);
		message = appendParam(message,"rOrderId",rOrderId);
		message = appendParam(message,"seqId",seqId);
			
		message = appendParam(message,"key",key);
		
		System.out.println("加签明文===" + message);
		
		Pkipair pki = new Pkipair();
		String sign = pki.signMsg(message);
		
		System.out.println("签名ignMsg===" + sign);
		
		request.put("signMsg", sign);
		
		System.out.println("请求json===" + JSON.toJSONString(request));
		
		//sandbox�ύ��ַ
		String reqUrl = "https://sandbox.99bill.com/gatewayapi/gatewayRefundQuery.do";
		
		String response = "";
		
		try {

			response = HttpUtil.doPostJsonRequestByHttps(JSON.toJSONString(request), reqUrl, 3000, 8000);

			Map<String,Object> m = new HashMap<String, Object>();
			m = JSON.parseObject(response, Map.class);
			System.out.println("响应报文==="+response);
			
			
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
