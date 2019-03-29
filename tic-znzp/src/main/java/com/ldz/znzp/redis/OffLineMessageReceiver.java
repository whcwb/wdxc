package com.ldz.znzp.redis;

import com.ldz.util.redis.RedisTemplateUtil;
import com.ldz.znzp.model.ClZnzp;
import com.ldz.znzp.service.ZnzpService;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class OffLineMessageReceiver implements MessageListener {


    private ZnzpService znzpService;

    private RedisTemplateUtil redisTemplateUtil;

    public OffLineMessageReceiver(RedisTemplateUtil redisTemplateUtil,ZnzpService znzpService){
        this.redisTemplateUtil = redisTemplateUtil;
        this.znzpService = znzpService;
    }


    @Override
    public void onMessage(Message message, byte[] pattern) {
        val topic = redisTemplateUtil.getStringSerializer().deserialize(message.getChannel());
        val eventMessage = redisTemplateUtil.getValueSerializer().deserialize(message.getBody());
       // String topic = Arrays.toString(pattern);
        String tid = (String) eventMessage;
        if(StringUtils.equals(topic,"offline" )){
            // 离线事件 获取站牌的编号 ，更改为离线
            ClZnzp znzp = znzpService.findById(tid);
            if(znzp != null ){
                znzp.setZxZt("10");
                znzpService.update(znzp);
            }
        }

    }
}
