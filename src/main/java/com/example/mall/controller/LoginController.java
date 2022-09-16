package com.example.mall.controller;

import cn.hutool.core.util.StrUtil;
import com.example.mall.constant.ResultMessage;
import com.example.mall.entity.UserInfoEntity;
import com.example.mall.service.MessageSendService;
import com.example.mall.service.UserInfoService;
import com.example.mall.vo.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/login")
public class LoginController {
	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private MessageSendService messageSendService;

	@ApiOperation("注册获取验证码")
	@GetMapping("/getMsCode")
	public R getMessageCode(String email) {
		if (StrUtil.isBlank(email)) {
			return R.error(ResultMessage.MISSING_PARAMETERS);
		} else {
			//TODO 发送验证码业务
			int code = (int) ((Math.random() * 9 + 1) * 100000);
			String codeNum = String.valueOf(code);
			messageSendService.sendMsg(email,codeNum);
			return R.ok();
		}
	}


	@ApiOperation("注册")
	@PostMapping("/register")
	public R register(@RequestBody UserInfoEntity user) {
		boolean success = userInfoService.addUser(user);
		if (success) {
			return R.ok();
		} else {
			return R.error(ResultMessage.ADD_USER_ERROR);
		}
	}

	@PostMapping
	public R login(@RequestBody UserInfoEntity user) {
		boolean success = userInfoService.index(user);
		if (success){
			return R.ok();
		}else {
			return R.error(ResultMessage.LOGIN_ERROR);
		}
	}
}
