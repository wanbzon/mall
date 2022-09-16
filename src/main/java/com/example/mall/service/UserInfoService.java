package com.example.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.mall.entity.UserInfoEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

/**
 * 
 *
 * @author ouyang
 * @email wanbzoy@163.com
 */
public interface UserInfoService extends IService<UserInfoEntity> {

	Page<UserInfoEntity> getPage(Map<String, Object> params);

	boolean addUser(UserInfoEntity user);

	boolean index(UserInfoEntity user);
}

