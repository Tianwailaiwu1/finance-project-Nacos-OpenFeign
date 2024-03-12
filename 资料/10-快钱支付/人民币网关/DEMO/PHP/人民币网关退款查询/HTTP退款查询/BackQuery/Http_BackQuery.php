<?php

header("content-type:text/html;charset=utf-8"); 

$time = date('YmdHis');#时间戳

$version="v2.0"; //版本号
$signType="2"; //2 代表PKI加密方式
$merchantAcctId="1001292117101"; //人民币账号  会员编号+01
$startDate="20200331"; //数字串，一共8 位格式为YYYYMMDD
$endDate="20200401"; //数字串，一共8 位格式为YYYYMMDD
$orderId="1229788340202000001"; //退款订单号
$requestPage="1"; //请求记录页面
$rOrderId=""; //原交易订单号
$seqId=""; //退款交易号
$extra_output_column=""; //额外输出参数
$key="D4TSSG89AX2A596A";


	function kq_ck_null($kq_va,$kq_na){if($kq_va == ""){$kq_va="";}else{return $kq_va=$kq_na.'='.$kq_va.'&';}}

	$kq_all_para=kq_ck_null($version,'version');
	$kq_all_para.=kq_ck_null($signType,"signType");
	$kq_all_para.=kq_ck_null($merchantAcctId,'merchantAcctId');
	$kq_all_para.=kq_ck_null($startDate,'startDate');
	$kq_all_para.=kq_ck_null($endDate,'endDate');
	$kq_all_para.=kq_ck_null($orderId,'orderId');
	$kq_all_para.=kq_ck_null($requestPage,'requestPage');
	$kq_all_para.=kq_ck_null($rOrderId,'rOrderId');
	$kq_all_para.=kq_ck_null($seqId,'seqId');
	$kq_all_para.=kq_ck_null($extra_output_column,'extra_output_column');	
	$kq_all_para.=kq_ck_null($key,'key');

	$kq_all_para=substr($kq_all_para,0,strlen($kq_all_para)-1);
	
	
	
		/////////////  RSA 签名计算 ///////// 开始 //


	global $pfx_path,$key_password;
    $pfx=file_get_contents("./20190801.3300000002925831.pfx"); //商户PFX证书地址
	$key_password = '123456';//证书密码
	
    openssl_pkcs12_read($pfx,$certs,$key_password);
    $privkey=$certs['pkey'];


	openssl_sign($kq_all_para, $signMsg, $privkey,OPENSSL_ALGO_SHA256);


	 $signMsg = base64_encode($signMsg);
	/////////////  RSA 签名计算 ///////// 结束 //

  $array = [
      'version' => $version,
      'signType' => $signType,
      'merchantAcctId'=> $merchantAcctId,
	  'startDate'=> $startDate,
	  'endDate'=> $endDate,
	  'orderId'=> $orderId,
	  'requestPage'=> $requestPage,
//	  'rOrderId'=> $rOrderId,
//	  'seqId'=> $seqId,
//	  'extra_output_column'=> $extra_output_column,
	  'signMsg'=> $signMsg,
  ];




$result = json_encode($array);

echo  '请求json==='.$result;

 function kq_curl($url,$str){
        //PEM路径必需绝对路径，不能是项目路径，否则会报错
        $user_agent = "Mozilla/4.0 (compatible; MSIE 5.01; Windows NT 5.0)";
        $header[] = "Content-type: application/json;charset=utf-8";
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL,$url);
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_SSL_VERIFYHOST,2);
        curl_setopt($ch, CURLOPT_USERAGENT,$user_agent);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER,true);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER,false);

        curl_setopt($ch, CURLOPT_POSTFIELDS, $str);
        $output = curl_exec($ch);
        $httpCode = curl_getinfo($ch,CURLINFO_HTTP_CODE);
        echo "<br/>"."httpCode:"."<br/>".$httpCode."<br/>";
        if($httpCode!=200){
            echo "curl_error:".curl_error($ch)."<br/>";
        }

        return $output;

    }



$url = "https://sandbox.99bill.com/gatewayapi/gatewayRefundQuery.do";//接口地址
//$url = "https://www.99bill.com/gatewayapi/gatewayRefundQuery.do";//生产地址
$json_body =$result;//需要发送的数据
$de_json = kq_curl($url, $json_body);

echo  '响应报文==='.$de_json;

$respArr = json_decode($de_json,true);

	$kq_all_para2=kq_ck_null($respArr['version'],'version');
	$kq_all_para2.=kq_ck_null($respArr['signType'],'signType');
	$kq_all_para2.=kq_ck_null($respArr['merchantAcctId'],'merchantAcctId');
	$kq_all_para2.=kq_ck_null($respArr['recordCount'],'recordCount');
	$kq_all_para2.=kq_ck_null($respArr['pageCount'],'pageCount');
	$kq_all_para2.=kq_ck_null($respArr['currentPage'],'currentPage');
	$kq_all_para2.=kq_ck_null($respArr['pageSize'],'pageSize');
	$kq_all_para2.=kq_ck_null($respArr['errCode'],'errCode');
	$kq_all_para2.=kq_ck_null($key,'key');

	$kq_all_para2=substr($kq_all_para2,0,strlen($kq_all_para2)-1);
	


//回调验签
	$fp = fopen('./CFCA_sandbox.cer', 'r'); 
	$cert = fread($fp, 8192); 
	fclose($fp); 
	$pubkeyid = openssl_get_publickey($cert); 

$ok = openssl_verify($kq_all_para2, base64_decode(urldecode($respArr['signMsg'])), $pubkeyid,OPENSSL_ALGO_SHA256); 

if($ok==1) echo "<br/><br/>验签成功！";
else echo "<br/><br/>验签失败！";
?>
<head>
<title>退款查询</title>
</head>