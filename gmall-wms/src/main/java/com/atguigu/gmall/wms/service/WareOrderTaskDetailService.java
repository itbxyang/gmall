package com.atguigu.gmall.wms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.wms.entity.WareOrderTaskDetailEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 库存工作单
 *
 * @author buxiangyang
 * @email lxf@atguigu.com
 * @date 2020-03-26 11:46:15
 */
public interface WareOrderTaskDetailService extends IService<WareOrderTaskDetailEntity> {

    PageVo queryPage(QueryCondition params);
}

