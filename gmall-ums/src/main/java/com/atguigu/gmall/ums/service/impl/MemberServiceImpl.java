package com.atguigu.gmall.ums.service.impl;

import com.atguigu.core.exception.MemberExcepton;
import com.atguigu.core.utils.NumberUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.ums.dao.MemberDao;
import com.atguigu.gmall.ums.entity.MemberEntity;
import com.atguigu.gmall.ums.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String SMSCODE_PREFIX = "user:verify:";

    @Value("${smscode.rabbitmq.exchange}")
    private String EXCHANGE_NAME;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public Boolean checkData(String data, Integer type) {

        QueryWrapper<MemberEntity> queryWrapper = new QueryWrapper<>();

        switch (type) {
            case 1: queryWrapper.eq("username", data);break;
            case 2: queryWrapper.eq("mobile", data);break;
            case 3: queryWrapper.eq("email", data);break;
            default:
                return false;
        }
        return this.count(queryWrapper) == 0;

    }

    @Override
    public Boolean register(MemberEntity memberEntity, String code) {
        // 查询redis中的验证码
        String redisCode = this.redisTemplate.opsForValue().get(SMSCODE_PREFIX + memberEntity.getMobile());

        // 校验验证码
        if (!StringUtils.equals(code, redisCode)) {
            return false;
        }
        // 生成盐
        String salt = UUID.randomUUID().toString().substring(0, 6);
        memberEntity.setSalt(salt);

        // 加盐加密
        memberEntity.setPassword(DigestUtils.md5Hex(memberEntity.getPassword() + salt));

        // 新增用户
        memberEntity.setGrowth(0);
        memberEntity.setIntegration(0);
        memberEntity.setLevelId(0l);
        memberEntity.setCreateTime(new Date());
        memberEntity.setStatus(1);
        this.save(memberEntity);
        // 删除redis中的手机验证码
        this.redisTemplate.delete(SMSCODE_PREFIX + memberEntity.getMobile());
        return true;
    }

    @Override
    public void sendVerifyCode(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return;
        }
        // 生成验证码
        String code = NumberUtils.generateCode(6);
        Map<String, String> msg = new HashMap<>();
        msg.put("mobile", mobile);
        msg.put("code", code);
        // 发送消息到rabbitMQ
        this.amqpTemplate.convertAndSend(EXCHANGE_NAME,"verifycode.sms", msg);
        // 把验证码保存到redis中
        this.redisTemplate.opsForValue().set(SMSCODE_PREFIX + mobile, code, 5, TimeUnit.MINUTES);
    }

    @Override
    public MemberEntity queryUser(String username, String password) {

        // 根据用户名查询用户
        MemberEntity memberEntity = this.getOne(new QueryWrapper<MemberEntity>().eq("username", username));
        // 判断用户名是否存在
        if (memberEntity == null) {
//            return null;
            throw new MemberExcepton("用户名不存在！");
        }
        // 先对用户输入的密码加盐加密
        password = DigestUtils.md5Hex(password + memberEntity.getSalt());
        // 比较数据库中的密码和用户输入的密码是否一致
        if (!StringUtils.equals(password, memberEntity.getPassword())) {
            throw new MemberExcepton("密码错误！");
        }
        return memberEntity;
    }


}