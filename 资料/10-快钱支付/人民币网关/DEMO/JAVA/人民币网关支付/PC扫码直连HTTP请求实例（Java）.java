package com.bill.common;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class TestAPI {
    public static final Logger logger = LoggerFactory.getLogger(TestAPI.class);

    public static void main(String[] args) {
        String url = "https://sandbox.99bill.com/gateway/recvMerchantInfoAction.htm";
        String parm = "inputCharset=1&pageUrl=&bgUrl=http://192.168.46.250:8801/RMBPORT/receive.jsp&version=v2.0&language=1&signType=4&merchantAcctId=1001214035601&payerName=张三&payerContactType=1&payerContact=123456@qq.com&payerIdType=3&payerId=KQ33151000&payerIP=192.168.1.1&orderId=KQ20201110153636&orderAmount=1&orderTime=20201110153636&orderTimestamp=20201110153636&productName=Apple&productNum=1&productId=10000&productDesc=Apple&ext1=扩展1&ext2=扩展2&payType=28-1&bankId=&redoFlag=0&pid=&submit=提交到快钱";
        String signMsg="a8fSC0KjBXiJDqywCvZom5GAiSLNMUe24CYhLODQhCdDihUPSMV+6vVgIb9Hthlp6dS//LyZTyuo" +
        "K/o7LPlWXzjROr8WVpSGItzsJBuqLO7SD6PtaAeP1nqq8YTlOam01ObezR9yF7KPMKJ+ByvgLAF/" +
                "lEp95xeB0yLPcpHNLLm4w26NQGacKYEUOuPQDXlM9sFASn5jJM3WCMYNQFlOarFT9j53HPTh0G9T" +
                "Y0Knx+XwyedEOht9HjKZVIjOmAcSgB/srWSS7PI4WllwHjuIS6k8pHDLuYzXpwW66czlAvLtuNC2" +
                "4sU9HXFVS+fI549INrSdd0Gu5i4STYZEDSYWFw==";
        try {
            String result= postFi(url,parm,signMsg);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String postFi(String url , String parm  ,String signMsg) throws Exception {
        //http请求，TLS1.2协议
        Integer timeout = 10*000;
        RequestConfig globalConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .setConnectTimeout(timeout).setSocketTimeout(timeout)
                .setExpectContinueEnabled(true).build();
        SSLContext sslcontext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        }).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1.2" }, null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(globalConfig).setSSLSocketFactory(sslsf).build();
        CloseableHttpResponse resp = null;
        if(!StringHelp.isNullOrBlank(parm)){
            URIBuilder udsUrl =new URIBuilder(url+"?"+ parm + "&signMsg=" +java.net.URLEncoder.encode(signMsg,"utf-8"));
            String result = "";
            HttpGet httpGet = new HttpGet(udsUrl.build());
            try {
                resp = httpClient.execute(httpGet);
                String status = String.valueOf(resp.getStatusLine().getStatusCode());
                result = EntityUtils.toString(resp.getEntity(),"UTF-8");
                if( null!=result && !result.equals("") && "200".equals(status)){
                    return result;
                }else{
                    return "Request KQ Exception";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                //关闭资源
                if(resp != null){
                    try {
                        resp.close();
                    }catch (IOException ioe){
                        ioe.printStackTrace();
                    }
                }
                if(httpClient != null){
                    try{
                        httpClient.close();
                    }catch (IOException ioe){
                        ioe.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
