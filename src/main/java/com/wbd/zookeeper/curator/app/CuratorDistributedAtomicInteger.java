package com.wbd.zookeeper.curator.app;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

/**
 * 分布式锁计数器功能， 应用在统计系统的在线人数
 * 
 * @author jwh
 *
 */
public class CuratorDistributedAtomicInteger {

	static String path = "/a/distribute";

	private static CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.1.141")
			.connectionTimeoutMs(6000).retryPolicy(new ExponentialBackoffRetry(1000, 5)).build();

	public static void main(String[] args) {

		client.start();

		DistributedAtomicInteger dai = new DistributedAtomicInteger(client, path, new RetryNTimes(4, 3000));

		try {
		AtomicValue<Integer> av = 	dai.add(5);
		System.out.println(av.succeeded());

		System.out.println(av.postValue());
		System.out.println(av.preValue());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
