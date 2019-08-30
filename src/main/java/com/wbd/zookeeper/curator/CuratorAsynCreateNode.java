package com.wbd.zookeeper.curator;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * ��zookeeper�У������첽֪ͨ�¼�������EventThread����߳��������
 * �����߳����ڴ��д������е��¼�֪ͨ�����̴߳󲿷ֳ������ܹ���֤���¼������˳����
 * ��������Ҳ��һ���׶ˣ�һ������һ�����ӵĴ���Ԫ���ͻ����ĺܳ�ʱ�䣬�Ӷ�Ӱ�������¼��Ĵ��� ��ˣ�
 * inBackground�ӿ��У������û�����һ��Executorʵ��������һ���Ϳ��԰���Щ���� �����鴦�����һ��ר�ŵ��߳�ȥ�� �磬
 * Executors,newFixedThread(2)��
 * 
 * ����curator ���첽api�����ڵ㣬 �첽�����߼����Խ����̳߳�ȥ���� Ҳ���Խ���zookeeperĬ�ϵ�EventThread������
 * 
 * @author jwh
 *
 */
public class CuratorAsynCreateNode {

	static String path = "/a/n/c";

	private static CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.1.141")
			.connectionTimeoutMs(6000).retryPolicy(new ExponentialBackoffRetry(1000, 5)).build();

	private static CountDownLatch countDownLatch = new CountDownLatch(2);

	// ����һ��Executorʵ��
	static ExecutorService executorService = Executors.newFixedThreadPool(5);

	public static void main(String[] args) {

		client.start();
		System.out.println("��ǰ�߳�" + Thread.currentThread().getName());

		// �˴�����Executorʵ��
		try {
			client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
					.inBackground(new BackgroundCallback() {

						public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {

							System.out.println("code1===" + event.getResultCode() + "==type==" + event.getType());
							System.out.println("�߳�1===" + Thread.currentThread().getName());
							countDownLatch.countDown();
						}
					}, executorService).forPath(path, "thread".getBytes());
			System.out.println("executorʵ��1");

			client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
					.inBackground(new BackgroundCallback() {

						public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {

							System.out.println("code2===" + event.getResultCode() + "==type==" + event.getType());
							System.out.println("�߳�2===" + Thread.currentThread().getName());
							countDownLatch.countDown();
						}
					}).forPath(path, "thread".getBytes());
			System.out.println("executorʵ��2");
			countDownLatch.await();
			executorService.shutdown();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
