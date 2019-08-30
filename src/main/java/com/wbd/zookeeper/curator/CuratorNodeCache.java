package com.wbd.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * curator 节点监听
 * @author jwh
 *
 */
public class CuratorNodeCache {
	static String path = "/a/node";

	private static CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.1.141")
			.connectionTimeoutMs(6000).retryPolicy(new ExponentialBackoffRetry(1000, 5)).build();

	public static void main(String[] args) {
		
		client.start();
		
		final NodeCache nodeCache = new NodeCache(client,path,false);
		
		try {
			nodeCache.start();
			nodeCache.getListenable().addListener(new NodeCacheListener() {
				
				public void nodeChanged() throws Exception {
					System.out.println("节点数据更新"+new String(nodeCache.getCurrentData().getData()));
				}
			});
			Thread.sleep(Integer.MAX_VALUE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}
