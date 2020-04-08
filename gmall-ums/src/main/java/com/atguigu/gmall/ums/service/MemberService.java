package com.atguigu.gmall.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.ums.entity.MemberEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 会员
 *
 * @author buxiangyang
 * @email lxf@atguigu.com
 * @date 2020-04-05 13:20:56
 */
public interface MemberService extends IService<MemberEntity> {

    PageVo queryPage(QueryCondition params);

    Boolean checkData(String data, Integer type);

    Boolean register(MemberEntity memberEntity, String code);

    MemberEntity queryUser(String username, String password);

    void sendVerifyCode(String mobile);
}

