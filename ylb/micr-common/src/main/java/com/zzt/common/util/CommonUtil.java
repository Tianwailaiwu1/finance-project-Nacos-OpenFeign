package com.zzt.common.util;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.regex.Pattern;

public class CommonUtil {
    /**
     * 处理pageNo
     */
    public static int defaultPageNo(Integer pageNo) {
        int pNo = pageNo;
        if (pageNo == null || pageNo < 1) {
            pNo = 1;
        }
        return pNo;
    }

    /**
     * 处理pageSize
     */
    public static int defaultPageSize(Integer pageSize) {
        int pSize = pageSize;
        if (pageSize == null || pageSize < 1) {
            pSize = 1;
        }
        return pSize;
    }

    /**
     * 手机号脱敏处理
     */
    public static String desensitizePhone(String phone) {
        String result = "***********";
        if (phone != null && phone.trim().length() == 11) {
            result = phone.substring(0, 3) + "******" + phone.substring(9, 11);
        }
        return result;
    }

    /**
     * 手机号格式验证  true:格式正确   false:格式错误
     */
    public static Boolean checkPhone(String phone) {
        boolean flag = false;
        if (phone != null) {
            flag = Pattern.matches("^1[1-9]\\d{9}$", phone);
        }
        return flag;
    }

    /**
     * 密码格式验证   true:格式正确   false:格式错误
     */
    public static Boolean checkPwd(String pwd) {
        boolean flag = false;
        if (pwd != null && pwd.length() >= 6 && pwd.length() <= 20) {
            flag = Pattern.matches("^[0-9a-zA-Z]{6,20}$", pwd);
        }
        return flag;
    }

    /**
     * 密码md5加密与加盐
     */
    public static String hashPassword(String password, String salt) {
        String hashedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update((password + salt).getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }

    /**
     * 生成随机的盐值
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * 身份证号格式验证     true:格式正确   false:格式错误
     */
    public static Boolean checkIdCard(String idCard) {
        boolean flag = false;
        if (idCard != null) {
            flag = Pattern.matches("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)", idCard);
        }
        return flag;
    }


    /**
     * 比较BigDecimal  n1>=n2 ture 反之false
     */
    public static Boolean ge(BigDecimal n1, BigDecimal n2) {
        //检验n1,n2是否为空
        if (n1 == null || n2 == null) {
            //抛出异常
            throw new RuntimeException("BigDecimal为空");
        }
        return n1.compareTo(n2) >= 0;
    }
}
