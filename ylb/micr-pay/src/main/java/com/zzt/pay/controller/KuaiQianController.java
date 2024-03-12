package com.zzt.pay.controller;

import com.zzt.api.model.User;
import com.zzt.pay.service.KuaiQianService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("/kq")
public class KuaiQianController {
    @Resource
    private KuaiQianService kuaiQianService;

    /**
     * 接收来自前端项目的支付充值请求
     *
     * @param uid
     * @param rechargeMoney
     * @return
     */
    @GetMapping("/receive/recharge")
    public String receiveFrontRechargeKQ(Integer uid, BigDecimal rechargeMoney, Model model) {
        //默认是错误视图页面
        String view = "err";
        //校验参数
        if (uid == null || uid <= 0 || rechargeMoney == null || rechargeMoney.doubleValue() <= 0) {
            //参数有误
            return view;
        }
        //检查传过来的uid是否是有效用户
        User user = kuaiQianService.queryUser(uid);
        if (user == null) {
            //用户信息不存在,返回错误页面
            return view;
        }
        try {
            //创建快钱支付接口需要的请求参数
            Map<String, String> data = kuaiQianService.generateFormData(uid, user.getPhone(), rechargeMoney);
            model.addAllAttributes(data);

            //创建充值记录
            kuaiQianService.addRecharge(uid, rechargeMoney, data.get("orderId"));
            //将订单号存入redis
            kuaiQianService.addOrderIdToRedis(data.get("orderId"));

            //参数正确，提交支付请求给快钱Form页面(thymeleaf)
            view = "kqForm";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 接收快钱返回给商家的支付结果,快钱以get请求方式发送给商家
     *
     * @return
     */
    @GetMapping("/receive/notify")
    @ResponseBody
    public String payResultNotify(HttpServletRequest request) {
        kuaiQianService.enCodeByCer(request);
        return "<result>1</result><redirecturl>http://localhost:8080/</redirecturl>";
    }

    /**
     * 从定时任务调用接口，查询订单
     *
     * @return
     */
    @GetMapping("/receive/query")
    @ResponseBody
    public String queryKqOrder() {
        kuaiQianService.handleQueryOrder();
        return "接收了查询的请求";
    }
}
