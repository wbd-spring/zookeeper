package com.wbd.zookeeper.curator.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * ���÷ֲ�ʽ��
 * ��������£� ����ʱ��������ɵ���ˮ�����ظ������������⣬ ��Ҫ���÷ֲ�ʽ��
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
		
		//�����ֲ�ʽ������
		final InterProcessMutex lock = new InterProcessMutex(client,path);
		
		for(int i=0;i<100;i++) {
			
			new Thread(new Runnable() {
				
				public void run() {
				
					try {
						countDownLatch.await();
						lock.acquire(); //��ȡ��
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
					String orderNo = sdf.format(new Date());
					System.out.println("������="+orderNo);
					try {
						lock.release(); //�ͷ���
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
