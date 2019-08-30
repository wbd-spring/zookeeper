package com.wbd.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * curator api 客户端
 * 
 * zookeeper中规定所有非叶子节点必须为持久节点。
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

		// fluent风格
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.1.141:2181")
				.sessionTimeoutMs(6000).retryPolicy(retryPolicy).build();

		client.start();
		try {
			// 创建一个节点
			// client.create().forPath("/a/a", "123".getBytes());

			// 创建一个节点， 并且自动递归创建父节点
			client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/a/p/c","abcdef".getBytes());
		
		  //删除节点，
			//client.delete().deletingChildrenIfNeeded().forPath("/a/p");
			
			//获取节点数据
		System.out.println(	client.getData().forPath("/a"));
		
		//读取一个节点的数据内容， 同事获取到该节点的stat
		Stat stat = new Stat();
		client.getData().storingStatIn(stat).forPath("/a/p");
		System.out.println(stat.getVersion());
		
		
		
		//节点数据更新
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
