package com.atguigu.gmall.ums.dao;

import com.atguigu.gmall.ums.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author buxiangyang
 * @email lxf@atguigu.com
 * @date 2020-04-05 13:20:56
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
