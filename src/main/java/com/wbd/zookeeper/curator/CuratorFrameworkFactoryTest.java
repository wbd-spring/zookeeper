package com.wbd.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * curator api �ͻ���
 * 
 * zookeeper�й涨���з�Ҷ�ӽڵ����Ϊ�־ýڵ㡣
 * 
 * 
 * @author jwh
 *
 */
public class CuratorFrameworkFactoryTest {

	public static void main(String[] args) {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		//
		// CuratorFramework client =
		// CuratorFrameworkFactory.newClient("192.168.1.141:2181", retryPolicy);
		//
		// client.start();

		// fluent���
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.1.141:2181")
				.sessionTimeoutMs(6000).retryPolicy(retryPolicy).build();

		client.start();
		try {
			// ����һ���ڵ�
			// client.create().forPath("/a/a", "123".getBytes());

			// ����һ���ڵ㣬 �����Զ��ݹ鴴�����ڵ�
			client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/a/p/c","abcdef".getBytes());
		
		  //ɾ���ڵ㣬
			//client.delete().deletingChildrenIfNeeded().forPath("/a/p");
			
			//��ȡ�ڵ�����
		System.out.println(	client.getData().forPath("/a"));
		
		//��ȡһ���ڵ���������ݣ� ͬ�»�ȡ���ýڵ��stat
		Stat stat = new Stat();
		client.getData().storingStatIn(stat).forPath("/a/p");
		System.out.println(stat.getVersion());
		
		
		
		//�ڵ����ݸ���
		client.setData().forPath("/a/p", "abcdef".getBytes());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			Thread.sleep(Integer.MAX_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
