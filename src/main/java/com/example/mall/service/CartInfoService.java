package com.example.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.mall.entity.CartInfoEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

/**
 * 
 *
 * @author ouyang
 * @email wanbzoy@163.com
 */
public interface CartInfoService extends IService<CartInfoEntity> {

	Page<CartInfoEntity> getPage(Map<String, Object> params);
}

