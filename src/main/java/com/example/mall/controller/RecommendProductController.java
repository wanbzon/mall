package com.example.mall.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.mall.entity.RecommendProductEntity;
import com.example.mall.service.RecommendProductService;
import com.example.mall.vo.R;



/**
 *
 *
 * @author ouyang
 * @email wanbzoy@163.com
 */
@Api(tags = "商品推荐管理")
@RestController
@RequestMapping("/recommendProduct")
public class RecommendProductController {
    @Autowired
    private RecommendProductService recommendProductService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        Page<RecommendProductEntity> page = recommendProductService.getPage(params);

        return R.ok().setData(page);
    }

    /**
     * 获取size个推荐商品id  size为0或空返回所有
     * @param size
     * @return
     */
    @GetMapping("/getRecommend/{size}")
    public R list(@PathVariable("size")Long size){
        List<String> recommendProductIds = recommendProductService.getRecommendProductIds(size);

        return R.ok().setData(recommendProductIds);
    }

    /**
     * 根据id查询信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
		RecommendProductEntity recommendProduct = recommendProductService.getById(id);

        return R.ok().setData(recommendProduct);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody RecommendProductEntity recommendProduct){
		recommendProductService.save(recommendProduct);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody RecommendProductEntity recommendProduct){
		recommendProductService.updateById(recommendProduct);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody String[] ids){
		recommendProductService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
