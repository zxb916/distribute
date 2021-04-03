package com.onlylove.www.lock;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class LockService {

    private static JedisPool pool;

    private LockRedis lockRedis = new LockRedis(pool);

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(200);
        config.setMaxIdle(8);
        config.setMaxWaitMillis(1000 * 100);
        config.setTestOnBorrow(false);
        pool = new JedisPool(config, "127.0.0.1", 6379, 3000);
    }


    //秒杀
    public void seckill() {
        //获取锁等待时间  根据业务执行时间设置   timeout key过期时间防止线程死锁
        String identifierValue = lockRedis.getRedisLock(1000L, 5000L);
        if (identifierValue == null) {
            System.out.println(Thread.currentThread().getName() + ",获取锁失败，原因为获取锁超时");
            return;
        }
        System.out.println(Thread.currentThread().getName() + ",获取锁成功，锁的id:" + identifierValue + "正常执行");
        //释放锁
        lockRedis.unRedisLock(identifierValue);
    }


    public void closeJedisPool() {
        pool.close();
    }
}
