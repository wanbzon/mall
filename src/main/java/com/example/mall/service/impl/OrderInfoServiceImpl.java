package com.example.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mall.constant.OrderState;
import com.example.mall.constant.SelectArg;
import com.example.mall.entity.CartInfoEntity;
import com.example.mall.entity.OrderProductEntity;
import com.example.mall.entity.ProductInfoEntity;
import com.example.mall.service.CartInfoService;
import com.example.mall.service.OrderProductService;
import com.example.mall.service.ProductInfoService;
import com.example.mall.vo.OrderInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.mall.dao.OrderInfoDao;
import com.example.mall.entity.OrderInfoEntity;
import com.example.mall.service.OrderInfoService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("orderInfoService")
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoDao, OrderInfoEntity> implements OrderInfoService {

	@Autowired
	private OrderProductService orderProductService;

	@Autowired
	private ProductInfoService productInfoService;

	@Autowired
	private CartInfoService cartInfoService;

    @Override
    public Page<OrderInfoEntity> getPage(Map<String, Object> params) {
		Integer current = params.get("current") == null ? 1 : new Integer(params.get("current").toString());
		Page<OrderInfoEntity> page=new Page<OrderInfoEntity>(current, SelectArg.PAGESIZE);
		QueryWrapper<OrderInfoEntity> wrapper = new QueryWrapper<>();
		Page<OrderInfoEntity> OrderInfoPage = this.baseMapper.selectPage(page, wrapper);
		return OrderInfoPage;
    }

	/**
	 *
	 * @param orderId
	 * @return
	 */
	@Override
	public OrderInfoEntity getByOrderId(String orderId) {
		//?????????????????????????????????
		OrderInfoEntity orderInfoEntity = this.getOne(new QueryWrapper<OrderInfoEntity>()
				.eq(!StrUtil.isBlank(orderId), "order_sn", orderId));
		//?????????????????????????????????????????????
		List<OrderProductEntity> orderProdectList = orderProductService.list(new QueryWrapper<OrderProductEntity>()
				.eq(!StrUtil.isBlank(orderInfoEntity.getOrderSn()), "order_sn", orderInfoEntity.getOrderSn()));
		List<String> productIds = orderProdectList.stream().map(item -> {
			return item.getProductId();
		}).collect(Collectors.toList());
		//??????????????????
		List<ProductInfoEntity> productInfoEntities = productInfoService.listByIds(productIds);
		for (ProductInfoEntity productInfoEntity : productInfoEntities) {
			productInfoEntity.setNumber(getOrderProductNumber(productInfoEntity.getId(),orderProdectList));
		}
		//???????????????????????????
		orderInfoEntity.setProductList(productInfoEntities);
		return orderInfoEntity;
	}

	@Override
	public List<OrderInfoEntity> getProductsByUserId(String id) {
		return this.list(new QueryWrapper<OrderInfoEntity>().eq(!StrUtil.isBlank(id),"user_id",id));
	}

	@Transactional
	@Override
	public void saveOrder(List<OrderInfoVo> orderInfoList) {
		//???????????????
		String uuid = IdWorker.get32UUID();
		OrderInfoEntity orderInfoEntity = new OrderInfoEntity();
		//????????????id
		String userId = orderInfoList.get(0).getUserId();
		orderInfoEntity.setUserId(userId);
		Double total = new Double(0d);
		orderInfoEntity.setOrderSn(uuid);
		for (OrderInfoVo orderInfoVo : orderInfoList) {
			//?????????????????????????????????????????????????????????????????????
			OrderProductEntity orderProductEntity = new OrderProductEntity();
			orderProductEntity.setOrderSn(uuid);
			orderProductEntity.setNumber(orderInfoVo.getProductNumber());
			orderProductEntity.setProductId(orderInfoVo.getProductId());
			ProductInfoEntity productInfo = productInfoService.getById(orderInfoVo.getProductId());
			if (productInfo!=null){
				//??????????????????
				total+=productInfo.getPrice()*orderInfoVo.getProductNumber();
			}
			orderProductService.save(orderProductEntity);
			CartInfoEntity cartInfoEntity = new CartInfoEntity();
			cartInfoEntity.setUserId(userId);
			cartInfoEntity.setProductId(orderInfoVo.getProductId());
			cartInfoService.removeByProductIds(cartInfoEntity);
		}
		//????????????????????????
		orderInfoEntity.setAddressId(orderInfoList.get(0).getAddressId());
		orderInfoEntity.setCreateTime(new Date());
		//??????????????????
		Double freightAmount= new Random().nextDouble()*100;
		orderInfoEntity.setFreightAmount(freightAmount);
		orderInfoEntity.setTotalAmount(total);
		orderInfoEntity.setPayAmount(total+freightAmount);
		//???????????????????????????
		orderInfoEntity.setStatus(OrderState.PAID.getCode());
		//????????????????????????
		this.save(orderInfoEntity);
	}

	@Override
	public void removeByOrderIds(String[] orderIds) {
		this.remove(new QueryWrapper<OrderInfoEntity>().in("order_sn",orderIds));
	}

	public Page<OrderInfoEntity> getPage(Integer current, OrderInfoEntity orderInfoEntity) {
		Page<OrderInfoEntity> orderInfoEntityPage = new Page<>(current, SelectArg.PAGESIZE);
		// ??????????????????????????????
		if(orderInfoEntity!=null){
			QueryWrapper<OrderInfoEntity> queryWrapper = new QueryWrapper<>();
			// ?????????
			String orderSn = orderInfoEntity.getOrderSn();
			// ????????????
			Integer status = orderInfoEntity.getStatus();
			// ??????id
			String userId = orderInfoEntity.getUserId();
			if (StringUtils.hasText(orderSn))
				queryWrapper.like("order_sn",orderSn);
			if (StringUtils.hasText(userId))
				queryWrapper.eq("user_id",userId);
			if (!StringUtils.isEmpty(status))
				queryWrapper.eq("status",status);
			return this.page(orderInfoEntityPage,queryWrapper);
		}else {
			return this.page(orderInfoEntityPage);
		}
	}

	private Integer getOrderProductNumber(String id, List<OrderProductEntity> orderProdectList) {
		for (OrderProductEntity orderProductEntity : orderProdectList) {
			if (orderProductEntity.getProductId().equals(id)){
				return orderProductEntity.getNumber();
			}
		}
		return 0;
	}
}
