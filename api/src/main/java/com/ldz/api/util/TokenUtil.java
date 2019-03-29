package com.ldz.api.util;

import com.ldz.dao.biz.model.ClClient;
import com.ldz.util.commonUtil.Des;
import sun.security.util.AuthResources_it;

public class TokenUtil {


    /**
     * token 生成规则：
     * Des((clientId+[-]+timemills),secret)
     * @param clientId
     * @return
     */
    public static boolean validToken(String clientId,String token,String secret){
        try {
            String s = Des.decrypt1(token,secret);
            if (!s.contains("-")) return false;
            String[] ss = s.split("-");
            String clientIds = ss[0];
            if (!clientIds.equals(clientId)) return false;
            String times = ss[1];
            // 如果请求时间戳与服务器当前时间相差过大，认为是无效请求
            long requestTime = Long.parseLong(times);
            long currentTime = System.currentTimeMillis();
            long duration = 1000 * 60;
            if (requestTime - currentTime > duration)return false;
            if (currentTime - requestTime > duration)return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static void main(String[] args) throws Exception {
        String key = "11111111";
//        String s = Des.decrypt1("JIpEnrJ988RagCZP4Fe4dGmIb6ETmPbw",key);
        String s = new String(Des.encrypt("123".getBytes(),key.getBytes()));
        System.out.println(s);
        String r = new String(Des.decrypt(s.getBytes(),key.getBytes()));
        System.out.println(r);

    }

    private static String createToken(String clientId,String secret){
        try {
            return Des.encrypt(clientId+"-"+System.currentTimeMillis(),secret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
