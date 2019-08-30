package com.wbd.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.RetrySleeper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 对 子节点的监听
 * 
 * @author jwh
 *
 */
public class CuratorPathChildrenCache {

	// 创建Curator实例
	private static CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.1.141:2181")
			.retryPolicy(new ExponentialBackoffRetry(5000, 2)).build();

	public static void main(String[] args) {
		// 启动curator实例
		client.start();

		// 创建子节点监听对象
		PathChildrenCache pathChildCache = new PathChildrenCache(client, "/a/b", true);

		try {
			pathChildCache.start();
			pathChildCache.getListenable().addListener(new PathChildrenCacheListener() {

				public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {

					switch (event.getType()) {
					case CHILD_ADDED:

						System.out.println("子节点添加事件"+event.getData().getPath());
						break;

					case CHILD_UPDATED:
						System.out.println("子节点更新事件"+event.getData().getPath());
						break;

					case CHILD_REMOVED:
						System.out.println("子节点删除事件"+event.getData().getPath());
						break;

					default:
						break;
					}

				}
			});
			Thread.sleep(Integer.MAX_VALUE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
