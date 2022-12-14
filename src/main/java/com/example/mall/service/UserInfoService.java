package com.example.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.mall.entity.UserInfoEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mall.vo.UserInfoQuery;
import com.example.mall.vo.UserLoginVo;
import com.example.mall.vo.UserPasswordVo;
import com.example.mall.vo.UserRegisterVo;

import java.util.Map;

/**
 *
 *
 * @author ouyang
 * @email wanbzoy@163.com
 */
public interface UserInfoService extends IService<UserInfoEntity> {

	Page<UserInfoEntity> getPage(Map<String, Object> params);

    Page<UserInfoEntity> getPage(Integer current, UserInfoQuery userInfoQuery);

	boolean addUser(UserRegisterVo user);

	String login(UserLoginVo userLoginVo);

    String updatePassword(UserPasswordVo userPasswordVo);
}

