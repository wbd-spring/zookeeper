package com.wbd.zookeeper.curator.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * �� ���÷ֲ�ʽ��
 * ��������£� ����ʱ��������ɵ���ˮ�����ظ������������⣬ ��Ҫ���÷ֲ�ʽ��
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
					System.out.println("������="+orderNo);
				}
			}).start();
			
			countDownLatch.countDown();
			
		}
		
		

	}

}
