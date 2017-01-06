package org.claret.utils;

import junit.framework.TestCase;

import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * @author lvyahui (lvyahui8@gmail.com,lvyahui8@126.com)
 * @since 2016/9/30 16:13
 */
public class RandomUtilsTest extends TestCase {

    public void testHexColor() throws Exception {
        System.out.println(RandomUtils.hexColor());
    }

    public void testRandomString() throws Exception {
        System.out.println(RandomUtils.randomString());
    }

    public void testCreateGuid() throws Exception {
        int base = 100;
        int seqBits = 2;
        int seqMask = ~(1 << seqBits);
        int seq = 0;
        for (int i = 0; i < 5 ; i++){
            System.out.println(Integer.toBinaryString(base << seqBits | (seq = (seq + 1) & seqMask)));
        }
        System.out.println();
        System.out.println(Long.toBinaryString(System.currentTimeMillis() << 8));
        for (int i = 0; i < 1000 ;i ++){
            System.out.println(Long.toBinaryString(RandomUtils.createGuid()));
        }
    }

    public void testCreateGuidStr() throws Exception {
        Set<String> appids = new ConcurrentSkipListSet<>();

        int thread = 1000;
        final CyclicBarrier cdl = new CyclicBarrier(thread);

        for(int i = 0; i < thread; i++){
            new Thread(() -> {
                try {
                    cdl.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                String id = RandomUtils.createGuidStr(16);
                if(!appids.contains(id)){
                    appids.add(id);
                }
                else {
                    System.err.println(id);
                }
            }).start();
        }
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}