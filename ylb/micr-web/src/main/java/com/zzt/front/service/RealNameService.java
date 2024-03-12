package com.zzt.front.service;

public interface RealNameService {
    /**
     * 实名认证
     *
     * @param phone
     * @param name
     * @param idCard
     * @return
     */
    boolean handleRealName(String phone, String name, String idCard);

    /**
     * 检查该手机号是否已经实名认证
     *
     * @param phone
     * @return
     */
    boolean checkIsRealName(String phone);
}
