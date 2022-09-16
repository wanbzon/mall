package com.example.mall.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mall.constant.SelectArg;
import com.example.mall.exception.MyException;
import com.example.mall.vo.UserInfoQuery;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.mall.dao.UserInfoDao;
import com.example.mall.entity.UserInfoEntity;
import com.example.mall.service.UserInfoService;
import org.springframework.util.StringUtils;


@Service("userInfoService")
public class UserInfoServiceImpl extends ServiceImpl<UserInfoDao, UserInfoEntity> implements UserInfoService {

    @Override
    public Page<UserInfoEntity> getPage(Map<String, Object> params) {
		Integer current = params.get("current") == null ? 1 : new Integer(params.get("current").toString());
		Page<UserInfoEntity> page=new Page<UserInfoEntity>(current, SelectArg.PAGESIZE);
		QueryWrapper<UserInfoEntity> wrapper = new QueryWrapper<>();
		Page<UserInfoEntity> UserInfoPage = this.baseMapper.selectPage(page, wrapper);
		return UserInfoPage;
    }

	@Override
	public Page<UserInfoEntity> getPage(Integer current, UserInfoQuery userInfoQuery) {
		Page<UserInfoEntity> userInfoEntityPage = new Page<>(current, SelectArg.PAGESIZE);
		QueryWrapper<UserInfoEntity> userInfoEntityQueryWrapper = new QueryWrapper<>();
		userInfoEntityQueryWrapper.orderByDesc("update_time");
		if (userInfoQuery != null){
			String userName = userInfoQuery.getUserName();
			Integer role = userInfoQuery.getRole();
			String startTime = userInfoQuery.getStartTime();
			String endTime = userInfoQuery.getEndTime();
			if(StringUtils.hasText(userName))
				userInfoEntityQueryWrapper.like("username",userName);
			if(!StringUtils.isEmpty(role))
				userInfoEntityQueryWrapper.eq("role",role);
			if(StringUtils.hasText(startTime))
				userInfoEntityQueryWrapper.ge("create_time",startTime);
			if(StringUtils.hasText(endTime))
				userInfoEntityQueryWrapper.le("create_time",endTime);
		}
		this.page(userInfoEntityPage,userInfoEntityQueryWrapper);

		if (userInfoEntityPage.getRecords().size()>0){
			return userInfoEntityPage;
		}else {
			return null;
		}
	}

}
