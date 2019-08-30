package com.wbd.zookeeper.curator.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 采用分布式锁
 * 并发情况下， 利用时间戳，生成的流水号有重复，解决这个问题， 需要利用分布式锁
 * @author jwh
 *
 */
public class CuratorRecipesLock {

	static String path = "/a/lock";

	private static CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.1.141")
			.connectionTimeoutMs(6000).retryPolicy(new ExponentialBackoffRetry(1000, 5)).build();

	private static CountDownLatch countDownLatch = new CountDownLatch(2);
	
	public static void main(String[] args) {
		
		client.start();
		
		//创建分布式锁对象
		final InterProcessMutex lock = new InterProcessMutex(client,path);
		
		for(int i=0;i<100;i++) {
			
			new Thread(new Runnable() {
				
				public void run() {
				
					try {
						countDownLatch.await();
						lock.acquire(); //获取锁
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
					String orderNo = sdf.format(new Date());
					System.out.println("订单号="+orderNo);
					try {
						lock.release(); //释放锁
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
			
			countDownLatch.countDown();
			
		}
		
		

	}

}
