package com.onlylove.www.lock;

/**
 * Unit test for simple App.
 */
public class LockTest {
    /**
     * Rigorous Test :-)
     */

    public static void main(String[] args) {
        LockService lockService = new LockService();
        for (int i = 0; i < 100; i++) {
            new JedisThread(lockService) {
                @Override
                public void run() {
                    lockService.seckill();
                }
            }.start();
        }

    }
}
