package com.query.Util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;




public class Pkipair {
	
	
	public String signMsg( String signMsg) {

		String base64 = "";
		try {
			// 密钥仓库
			KeyStore ks = KeyStore.getInstance("PKCS12");

			// 读取密钥仓库
//			FileInputStream ksfis = new FileInputStream("e:/tester-rsa.pfx");
			
			// 读取密钥仓库（相对路径）
			String file = Pkipair.class.getResource("10012921171.pfx").getPath().replaceAll("%20", " ");//10012932671.pfx
			System.out.println("读取证书路径==="+file);
			
			FileInputStream ksfis = new FileInputStream(file);//10012174866.pfx 123456  test111 SHA256withRSA
			
			BufferedInputStream ksbufin = new BufferedInputStream(ksfis);

			char[] keyPwd = "123456".toCharArray();
			//char[] keyPwd = "YaoJiaNiLOVE999Year".toCharArray();
			ks.load(ksbufin, keyPwd);
			// 从密钥仓库得到私钥
			PrivateKey priK = (PrivateKey) ks.getKey("20190801.3300000002925831", keyPwd);//10012932671.pfx的别名：test
			Signature signature = Signature.getInstance("SHA256withRSA");
			signature.initSign(priK);
			signature.update(signMsg.getBytes("utf-8"));
			sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
			base64 = encoder.encode(signature.sign());
			
		} catch(FileNotFoundException e){
			System.out.println("文件找不到");
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	//	System.out.println("test = "+base64);
		return base64;
	}
	public boolean enCodeByCer( String val, String msg) {
		boolean flag = false;
		try {
			//获得文件(绝对路径)
			//InputStream inStream = new FileInputStream("e:/99bill[1].cert.rsa.20140803.cer");
			
			//获得文件(相对路径)
			String file = Pkipair.class.getResource("CFCA_sandbox.cer").toURI().getPath();
			System.out.println(file);
			FileInputStream inStream = new FileInputStream(file);
			
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) cf.generateCertificate(inStream);
			//获得公钥
			PublicKey pk = cert.getPublicKey();
			//签名
			Signature signature = Signature.getInstance("SHA256withRSA");
			signature.initVerify(pk);
			signature.update(val.getBytes());
			//解码
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			System.out.println(new String(decoder.decodeBuffer(msg)));
			flag = signature.verify(decoder.decodeBuffer(msg));
			System.out.println(flag);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("no");
		} 
		return flag;
	}
	public static void main(String[] args) {
		 Pkipair pki = new Pkipair();
		 //子订单
	//	 String SignMsgVal ="orderId=QC202003261457470155&orderAmount=1215000&orderTime=20200326145747&dealTime=20200326145926&payResult=10&payType=15&payAmount=1215000&fee=0&dealId=104400600&key=XIXMFISFG7RGDKQN";
	//	 String signMsg ="bfqEspyFLjLjgVxEETGxe+tNRwAMCToRh7JwCObVz15xotJEnRSAJkaLgnEK6vVCwi5z2Tot6702IRBdW2mGoCaROtYRJtiY+zXmXcn6LQZVUupIjhM5LanNgidwwZ1Sj7hbzq2DhUWrytBz9ikF0aNIo2J6tBebXozWtjg9b60/7700iJWcyZNOjYhjOZbqKIhX5yTUg6a4pJqqfYxSqkA1EmV2Vc8PH/x+2eNvY/84JQ4mfanSt0G36SLKaw88i84BTzVd04aKEetTJP1v1h7fHV4Z1PyEZMJ9kyxWHUL3N2pEzZQF6idI1ZKVz4Eu2+USI+tCSXif69EpgEkFuA==";
	     //总订单(按订单查询)
	//	 String SignMsgVal ="version=v2.0&signType=2&merchantAcctId=1001293267101&key=XIXMFISFG7RGDKQN";
	//	 String signMsg ="R9cbynTYNrhfp2/at1ZdSYn/dAg0jebxhZOOk5QHRllHlgGJch//8ZAMS2rmDTGIn2Fs6dAQ+FB+WVkZKr0PChqmvUWRHS+iQvn/Q9CNof5BpIzInrDfTjoXla9FpTe+zpy4T7+aMtaQoDVOmTgCiY0WLFGD3YxZNsHYcWqmncQgImiYbQtUyn7Dv/w+KSnhFsi/ojLHQS57lMjNE/IXKir4niKcXPK2bGvAiTA0WuE64NXMIQ5AN2BXYz3v5eG1tfaNatf3jISHwGeuXBT6rNxa68PQ0SHEU8TYywQukUg9LO15YhdONsCo3FJMHDt0sNGCFGY5XGSOGWaEcOTtew==";
		//总订单(按时间段查询)
		 String SignMsgVal ="version=v2.0&signType=2&merchantAcctId=1001293267101&currentPage=1&pageCount=1&pageSize=8&recordCount=8&key=XIXMFISFG7RGDKQN";
		 String signMsg ="oatCdsdUrUGqOR5JEXcHCrXEReECMapB/aJQ/Z3ss3mK1vuS+2XR2QrLaSQW1V7yDzo2UIxEqDy2kb9PVvOKK/nlUeS9pIE2IVv43n5+UNxUI9fPwhsyPGsmFxn2ncsSigW/jnyaA4BviMvI13xxbmK7v5SCuCcY7BWeK7XuGvkmGTImihvURcA04D9AAHR/Fec8bez5L7c2zOcYEAAOr+OY/IWW0k59z6Z6CbNc/SItg4eNWiALe95yab3YCdGy9/K5xPd12VCwtKNe1vckMPGV+IPUxjRy8UUdgmKbeLrTOlabLv50xyecE+fE8A+CtUFbR4TRVgwbWTYA1Q7mCA==";
		 boolean flag = pki.enCodeByCer(SignMsgVal,signMsg);
    	System.out.println("flag ==="+flag);
	}
}
