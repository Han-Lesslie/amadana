package com.amadana.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param expireTime 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean setExpire(String key,Object value,long expireTime) {
        try {
            if (expireTime >= 0) {
                redisTemplate.opsForValue().set(key,value,expireTime, TimeUnit.SECONDS);
            }else {
                redisTemplate.opsForValue().set(key,value);
            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key获取存储在redis里面的值
     * @param key
     * @return
     */
    public Object get(String key) {
        if (!StringUtils.isBlank(key) && !StringUtils.isEmpty(key)) {
            return redisTemplate.opsForValue().get(key);
        }else {
            return null;
        }
    }

    /**
     * 移除值为value的
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
