package com.example.mall.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 *
 * @author ouyang
 * @email wanbzoy@163.com
 * @date 2022-09-15 14:39:53
 */
@Data
@TableName("user_info")
@ApiModel(value = "UserAddressEntity对象", description = "用户地址")
public class UserInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty("id")
	private String id;
	/**
	 * 用户名
	 */
	@ApiModelProperty("用户名")
	private String username;
	/**
	 * 密码
	 */
	@ApiModelProperty("密码")
	private String password;
	/**
	 * 电话
	 */
	@ApiModelProperty("电话")
	private String mobile;
	/**
	 * 邮箱
	 */
	@ApiModelProperty("邮箱")
	private String email;
	/**
	 * 性别0男，1女
	 */
	@ApiModelProperty("性别0男，1女")
	private Integer gender;
	/**
	 * 0管理员，1商家，2普通用户
	 */
	@ApiModelProperty("0管理员，1商家，2普通用户")
	private Integer role;
	/**
	 * 注册时间
	 */
	@ApiModelProperty("注册时间")
	private Date createTime;
	/**
	 * 修改时间
	 */
	@ApiModelProperty("修改时间")
	private Date updateTime;
	/**
	 *
	 */
	@TableLogic
	@ApiModelProperty("逻辑删除")
	private Integer isDelete;

}
