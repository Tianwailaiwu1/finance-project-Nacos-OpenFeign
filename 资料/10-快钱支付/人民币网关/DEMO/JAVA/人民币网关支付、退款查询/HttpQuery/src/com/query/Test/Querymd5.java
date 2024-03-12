package com.query.Test;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import com.alibaba.fastjson.JSON;
import com.query.Md5.MD5Util;
import com.query.Util.Pkipair;

public class Querymd5{
	public static Log logger = LogFactory.getLog(Querymd5.class);
	public static void main(String[] args) {
		gateWayInitQuery();
	}
	
	/**
	 * 查询demo
	 */
	public static void gateWayInitQuery()
	{
		Map<String, Object> request = new HashMap<String, Object>();
		
		String inputCharset = "1";
		String version = "v2.0";
		String signType = "1";
		String merchantAcctId = "1001293267101";
		String queryType = "1";
		String queryMode = "1";
		String startTime = "20200326120000";
		String endTime = "20200326180000";		
		String requestPage = "";
		String orderId = "";	
		String key = "XIXMFISFG7RGDKQN";
		
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
		String sign=MD5Util.md5Hex(message.getBytes()).toUpperCase();
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
			System.out.println("返回参数==="+response);

			
			
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
