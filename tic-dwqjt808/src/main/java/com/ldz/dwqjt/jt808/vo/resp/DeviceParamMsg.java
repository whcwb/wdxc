package com.ldz.dwqjt.jt808.vo.resp;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class DeviceParamMsg {
//    @Value("${DeviceParam.ip}")
    private String ip = "2#www.168car.net";
//    @Value("${DeviceParam.port}")
    private String port = "9196" ;

    private int paramNum = 2;

    private int replyFlowId;

    private IpParams ipParams= new IpParams();

    private PortParams portParams = new PortParams();
    @Getter
    public class IpParams{

        private int msgId = 0x0017;

        private int paramLen = ip.length();

        private String param = ip;

    }

    @Getter
   public class PortParams{


        private int msgId = 0x0018;

        private int paramLen = port.length();

        private int param = Integer.parseInt(port);
    }




}
