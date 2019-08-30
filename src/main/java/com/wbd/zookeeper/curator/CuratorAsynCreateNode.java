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
 * 在zookeeper中，所有异步通知事件都是有EventThread这个线程来处理的
 * ，该线程用于串行处理所有的事件通知，该线程大部分场景下能够保证对事件处理的顺序性
 * ，但是这也是一个弊端，一旦遇到一个复杂的处理单元，就会消耗很长时间，从而影响其他事件的处理， 因此，
 * inBackground接口中，允许用户传入一个Executor实例，这样一来就可以吧那些复杂 的事情处理放在一个专门的线程去， 如，
 * Executors,newFixedThread(2)等
 * 
 * 利用curator 的异步api创建节点， 异步处理逻辑可以交给线程池去处理， 也可以交给zookeeper默认的EventThread来处理
 * 
 * @author jwh
 *
 */
public class CuratorAsynCreateNode {

	static String path = "/a/n/c";

	private static CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.1.141")
			.connectionTimeoutMs(6000).retryPolicy(new ExponentialBackoffRetry(1000, 5)).build();

	private static CountDownLatch countDownLatch = new CountDownLatch(2);

	// 声明一个Executor实例
	static ExecutorService executorService = Executors.newFixedThreadPool(5);

	public static void main(String[] args) {

		client.start();
		System.out.println("当前线程" + Thread.currentThread().getName());

		// 此处传入Executor实例
		try {
			client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
					.inBackground(new BackgroundCallback() {

						public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {

							System.out.println("code1===" + event.getResultCode() + "==type==" + event.getType());
							System.out.println("线程1===" + Thread.currentThread().getName());
							countDownLatch.countDown();
						}
					}, executorService).forPath(path, "thread".getBytes());
			System.out.println("executor实例1");

			client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
					.inBackground(new BackgroundCallback() {

						public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {

							System.out.println("code2===" + event.getResultCode() + "==type==" + event.getType());
							System.out.println("线程2===" + Thread.currentThread().getName());
							countDownLatch.countDown();
						}
					}).forPath(path, "thread".getBytes());
			System.out.println("executor实例2");
			countDownLatch.await();
			executorService.shutdown();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
