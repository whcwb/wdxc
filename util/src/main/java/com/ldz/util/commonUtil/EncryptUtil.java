package com.ldz.util.commonUtil;

/**
 * @author chenwei
 * @copyright
 * @category
 * @since 2017/11/4
 */
public class EncryptUtil {
    public static String encryptUserPwd(String origin){
        try {
            return Des.encrypt(origin);
        } catch (Exception e) {
            throw new RuntimeException("密码加密失败",e);
        }
    }
}
