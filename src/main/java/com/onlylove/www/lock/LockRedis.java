package com.onlylove.www.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

/**
 * JUC
 */
public class LockRedis {


    private JedisPool jedisPool;

    private String redislockKey = "redis_key";

    //setnx命令    redis中 key时报证唯一   set可以覆盖key的value  返回ok

    /**
     * 两个超时时间含义
     * 1。获取所得超时间   获取超时， 直接放弃
     * 2。获取之后超时时间  获取成功之后，对应的key 有对应有效期
     */

    public String getRedisLock(Long acquireTimeout, Long timeOut) {
        //1建立redis连接
        //2定义redis对应的value 值（uuid）释放锁
        //3redis 分布式锁 有两个超时时间问题。
        //4。在获取锁之后的超时时间
        //5,使用循环机制保证重复进行尝试获取锁（乐观锁）
        //使用setnx命令插入对应的redislocKey,如果返回1成功获取锁
        Jedis getConn = null;

        try {
            getConn = jedisPool.getResource();
            String identifierValue = UUID.randomUUID().toString();

            int expireLock = (int) (timeOut / 1000); //以秒为单位

            long endTime = System.currentTimeMillis() + acquireTimeout;

            while (System.currentTimeMillis() < endTime) {
                if (getConn.setnx(redislockKey, identifierValue) == 1) {
                    //设置对应key的有效期
                    getConn.expire(redislockKey, expireLock);
                    return identifierValue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (getConn != null) {
                getConn.close();
            }
        }
        return null;
    }

    //如果直接用conn.del(redislockKey);保证对应的是自己的创建的redislockKey 删除自己的锁
    public void unRedisLock(String identifierValue) {
        Jedis unConn = null;
        //释放锁有两种方式
        try {
            unConn = jedisPool.getResource();
            if (unConn.get(redislockKey).equals(identifierValue)) {
                unConn.del(redislockKey);
                System.out.println(Thread.currentThread().getName() + ",释放锁成功" + identifierValue + "正常执行");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (unConn != null) {
                unConn.close();
            }
        }
    }

    public LockRedis(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

}
