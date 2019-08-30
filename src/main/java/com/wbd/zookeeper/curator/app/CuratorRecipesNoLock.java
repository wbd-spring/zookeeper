package com.wbd.zookeeper.curator.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * 不 采用分布式锁
 * 并发情况下， 利用时间戳，生成的流水号有重复，解决这个问题， 需要利用分布式锁
 * @author jwh
 *
 */
public class CuratorRecipesNoLock {

	
	public static void main(String[] args) {
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		for(int i=0;i<20;i++) {
			
			new Thread(new Runnable() {
				
				public void run() {
				
					try {
						countDownLatch.await();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
					String orderNo = sdf.format(new Date());
					System.out.println("订单号="+orderNo);
				}
			}).start();
			
			countDownLatch.countDown();
			
		}
		
		

	}

}
