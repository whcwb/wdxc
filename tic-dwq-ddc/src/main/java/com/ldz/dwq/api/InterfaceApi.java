package com.ldz.dwq.api;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ldz.dwq.common.bean.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/intf")
@Slf4j
public class InterfaceApi {

	/**
	 * 根据用户请求IP查询绑定用户的实时数据
	 * @param facId
	 * @param request
	 * @return
	 */
	@GetMapping(value="/queryRealData")
	public ApiResponse<List<Map<String, Object>>> queryRealData(@RequestParam(required=false, name="facId")String facId, HttpServletRequest request) {
		String ip = getIpAddr(request);		
		return null;
	}
	
	/**
	 * 根据用户请求IP查询绑定用户的历史数据
	 * 1、默认不设置时间是查询最近10分钟的数据
	 * 2、最大查询3小时内的历史数据
	 * @param facId
	 * @param startTime
	 * @param endTime
	 * @param request
	 * @return
	 */
	@GetMapping(value="/queryHistoryData")
	public ApiResponse<List<Map<String, Object>>> queryHistoryData(@RequestParam(required=false, name="facId")String facId, @RequestParam(required=false, name="startTime")String startTime, @RequestParam(required=false, name="endTime")String endTime, HttpServletRequest request) {
		String ip = getIpAddr(request);
		return null;
	}
	
	/**
	 * 通过request获取请求者IP
	 * @param request
	 * @return
	 */
	public String getIpAddr(HttpServletRequest request) {   
       String ip = request.getHeader("x-forwarded-for");   
       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
           ip = request.getHeader("Proxy-Client-IP");   
       }   
       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
           ip = request.getHeader("WL-Proxy-Client-IP");   
       }   
       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
           ip = request.getRemoteAddr();   
           if(ip.equals("127.0.0.1")){     
               //根据网卡取本机配置的IP     
               InetAddress inet=null;     
               try {     
                   inet = InetAddress.getLocalHost();     
               } catch (UnknownHostException e) {     
                   e.printStackTrace();     
               }     
               ip= inet.getHostAddress();     
           }  
       }   
       // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割  
       if(ip != null && ip.length() > 15){    
           if(ip.indexOf(",")>0){     
               ip = ip.substring(0,ip.indexOf(","));     
           }     
       }     
       return ip;   
	}
}
