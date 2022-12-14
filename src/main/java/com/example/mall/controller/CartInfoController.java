package com.example.mall.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mall.constant.ResultMessage;
import com.example.mall.entity.ProductInfoEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.mall.entity.CartInfoEntity;
import com.example.mall.service.CartInfoService;
import com.example.mall.vo.R;



/**
 *
 *
 * @author ouyang
 * @email wanbzoy@163.com
 */
@Api(tags = "购物车管理")
@RestController
@RequestMapping("/cartInfo")
public class CartInfoController {
    @Autowired
    private CartInfoService cartInfoService;

    /**
     * 列表
     */
    @ApiOperation("分页查询")
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        Page<CartInfoEntity> page = cartInfoService.getPage(params);

        return R.ok().setData(page);
    }

    @ApiOperation("根据用户Id获取该用户的购物车信息")
    @GetMapping("/getCateInfoByUserId/{userId}")
    public R list(@PathVariable("userId") String userId){
        if (StrUtil.isBlank(userId)){
            return R.error(ResultMessage.NO_PARAMETERS);
        }
        List<ProductInfoEntity> list = cartInfoService.getProductByUserId(userId);

        return R.ok().setData(list);
    }


/*    *//**
     * 根据id查询信息
     *//*
    @ApiOperation("根据id查询信息")
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
		CartInfoEntity cartInfo = cartInfoService.getById(id);

        return R.ok().setData(cartInfo);
    }*/

    /**
     * 保存
     */
    @ApiOperation("保存")
    @PostMapping("/save")
    public R save(@RequestBody CartInfoEntity cartInfo){
		cartInfoService.save(cartInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @ApiOperation("根据商品id修改商品数量")
    @PutMapping("/update")
    public R update(@RequestBody CartInfoEntity cartInfo){
		cartInfoService.updateByUserId(cartInfo);

        return R.ok();
    }
    @ApiOperation("在购物车修改商品的数量")
    @PutMapping("/updateInCart")
    public R updateInCart(@RequestBody CartInfoEntity cartInfo){
        cartInfoService.updateInCartByUserId(cartInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation("根据商品id删除购物车加购商品")
    @DeleteMapping("/delete")
    public R delete(@RequestBody CartInfoEntity cartInfo){
		cartInfoService.removeByProductIds(cartInfo);

        return R.ok();
    }

    @ApiOperation("根据用户id和商品id获取该用户在购物车中拥有的该商品的数量")
    @GetMapping("/count/{userId}/{productId}")
    public R haveProductByUserAndProductId(@PathVariable("userId")String userId,
                                           @PathVariable("productId")String productId){
        QueryWrapper<CartInfoEntity> queryWrapper = new QueryWrapper<CartInfoEntity>().eq("user_id", userId).eq("product_id", productId);
        long count = cartInfoService.count(queryWrapper);
        return count>0?R.error():R.ok();
    }

}
