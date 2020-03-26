package com.atguigu.gmall.sms.service;

import com.atguigu.gmall.sms.vo.SkuSaleVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.sms.entity.SpuBoundsEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 商品spu积分设置
 *
 * @author buxiangyang
 * @email lxf@atguigu.com
 * @date 2020-03-20 21:58:34
 */
public interface SpuBoundsService extends IService<SpuBoundsEntity> {

    PageVo queryPage(QueryCondition params);

}

