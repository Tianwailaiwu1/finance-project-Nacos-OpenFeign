<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@page import="com.tuikuan.MD5Util;"%>


<%
	//商户编号，线上的话改成你们自己的商户编号的，发到商户的注册快钱账户邮箱的
	String merchant_id = "10012140356";  
	//退款接口版本号 目前固定为此值
	String version = "bill_drawback_api_1";
	//操作类型
	String command_type = "001";
	//原商户订单号
	String orderid = "KQ20190215192556";
	//退款金额，整数或小数，小数位为2位   以人民币元为单位
	String amount = "0.01";
	//退款提交时间 数字串，一共14位 格式为：年[4 位]月[2 位]日[2 位]时[2 位]分[2 位]秒[2位]
	String postdate = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
	//退款流水号  字符串
	String txOrder = "TS"+new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());		
	//加密所需的key值，线上的话发到商户快钱账户邮箱里
	String merchant_key= "H77G94MDSJC7R76H";
	//生成加密签名串
	String macVal = "";
	macVal = appendParam(macVal, "merchant_id", merchant_id);
	macVal = appendParam(macVal, "version", version);
	macVal = appendParam(macVal, "command_type", command_type);                                                  
	macVal = appendParam(macVal, "orderid", orderid);
	macVal = appendParam(macVal, "amount", amount);
	macVal = appendParam(macVal, "postdate",postdate);
	macVal = appendParam(macVal, "txOrder", txOrder);
	macVal = appendParam(macVal,"merchant_key",merchant_key);   
	String mac=MD5Util.md5Hex(macVal.getBytes("utf-8")).toUpperCase();
%>

<%!public String appendParam(String returns, String paramId, String paramValue) {
		if (returns != "") {
			if (paramValue != "") {

				returns +=  paramId + "=" + paramValue;
			}

		} else {

			if (paramValue != "") {
				returns = paramId + "=" + paramValue;
			}
		}

		return returns;
	}%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>

	</head>

	<body>
		<div align="center">
			<table>
				<tr>
					<td id="merchant_id">
						merchant_id[商户编号]:
					</td>
					<td>
						<input type="text" name="merchant_id" value="<%=merchant_id %>">
					</td>
				</tr>
				<tr>
					<td id="version">
						version[退款接口版本号]:
					</td>
					<td>
						<input type="text" name="version" value="<%=version %>" />
					</td>
				</tr>
				<tr>
					<td id="command_type">
						command_type[操作类型]:
					</td>
					<td>
						<input name="command_type" type="text" value="<%=command_type %>">
					</td>
				</tr>
				<tr>
					<td id="txOrder">
						txOrder[退款流水号]:
					</td>
					<td>
						<input name="txOrder" type="text" value="<%=txOrder %>">
					</td>
				</tr>
				<tr>
					<td id="amount">
						amount[退款金额]:
					</td>
					<td>
						<input name="amount" type="text" value="<%=amount %>">
					</td>
				</tr>
				<tr>
					<td id="postdate">
						postdate[退款提交时间]:
					</td>
					<td>
						<input name="postdate" type="text" value="<%=postdate %>">
					</td>
				</tr>
				<tr>
					<td id="orderid">
						orderid[原商户订单号]:
					</td>
					<td>
						<input name="orderid" type="text" value="<%=orderid %>">
					</td>
				</tr>
				<tr>
					<td id="payeeidsrc">
						payeeidsrc[原交易收款账户]:
					</td>
					<td>
						<input name="payeeidsrc" type="text" value="">
					</td>
				</tr>
				<tr>
					<td id="mac">
						mac:
					</td>
					<td>
						<input type="text" name=mac value="<%=mac%>" />
					</td>
				</tr>
			</table>
		</div>
		<div align="center" style="font-weight: bold;">
			<form name="kqPay"
				action="https://sandbox.99bill.com/webapp/receiveDrawbackAction.do"
				method="get">
				<input type="hidden" name="merchant_id" value="<%=merchant_id%>" />
				<input type="hidden" name="version" value="<%=version%>" />
				<input type="hidden" name="command_type" value="<%=command_type%>" />
				<input type="hidden" name="txOrder" value="<%=txOrder%>" />
				<input type="hidden" name="amount" value="<%=amount%>" />
				<input type="hidden" name="postdate" value="<%=postdate%>" />
				<input type="hidden" name="orderid" value="<%=orderid%>" />
				<input type="hidden" name="mac" value="<%=mac%>" />
				<input type="submit" name="submit" value="提交到快钱">
			</form>
		</div>
	</body>
</html>
