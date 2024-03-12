package com.zzt.front.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.zzt.common.enums.RCode;
import com.zzt.common.util.JwtUtil;
import com.zzt.front.pojo.RespResult;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TokenInterceptor implements HandlerInterceptor {

    private String secret;

    public TokenInterceptor(String secret) {
        this.secret = secret;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //如果是OPTIONS，就放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        //标识是否可以处理请求
        boolean requestSend = false;

        try {
            //获取请求头中的Uid
            String headerUid = request.getHeader("uid");
            //获取token,进行验证
            String headerToken = request.getHeader("Authorization");
            if (StringUtils.isNotBlank(headerToken)) {
                String jwt = headerToken.substring(7);
                //读jwt
                JwtUtil jwtUtil = new JwtUtil(secret);
                Claims claims = jwtUtil.readJwt(jwt);

                //获取jwt中的数据,uid
                Integer jwtUid = claims.get("uid", Integer.class);
                //判断jwt中的uid与请求头中的uid是否一致
                if (headerUid.equals(String.valueOf(jwtUid))) {
                    //保持一致，则可以处理请求
                    requestSend = true;
                }
            }

            //token没有通过验证，需要给前端返回错误提示
            failToken(requestSend, response);
        } catch (Exception e) {
            requestSend = false;
            e.printStackTrace();
        }

        //token没有通过验证，需要给前端返回错误提示
        failToken(requestSend, response);

        return requestSend;
    }

    private void failToken(boolean requestSend, HttpServletResponse response) throws IOException {
        //token没有通过验证，需要给前端返回错误提示
        if (requestSend == false) {
            //返回json数据给前端
            RespResult result = RespResult.fail();
            result.setCode(RCode.TOKEN_INVALID);

            //使用HttpServletResponse输出 json
            String respJson = JSONObject.toJSONString(result);
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.print(respJson);
            out.flush();
            out.close();
        }
    }
}
