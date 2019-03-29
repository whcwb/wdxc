package com.ldz.wechat.module.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ldz.util.bean.ApiResponse;
import com.ldz.wechat.module.model.ClJsy;
import com.ldz.wechat.module.service.ClJsyService;

import java.util.Map;

@RestController
@RequestMapping("put/jsy")
public class ClJsyCtrl {

	@Autowired
	private ClJsyService jsyService;

	@RequestMapping("getInfo")
	public ApiResponse<Map<String,Object>> getInfo(){
		return jsyService.getInfo();
	}

	@PostMapping("/getjsy")
	public ApiResponse<Map<String,Object>> findJsy(String sjh, String xm) {
		return jsyService.findJsy(sjh, xm);
	}
	@PostMapping("/update")
	public ApiResponse<String> updatejsy(ClJsy jsy){
		
		 return jsyService.updatejsy(jsy);
	}

}
