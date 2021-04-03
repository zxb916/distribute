package com.onlylove.www.lock;

public class JedisThread extends Thread {

    private LockService lockService;

    public JedisThread(LockService lockService) {
        this.lockService = lockService;
    }

    @Override
    public void run() {
        lockService.seckill();
    }
}
