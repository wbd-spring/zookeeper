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
 * �� �ӽڵ�ļ���
 * 
 * @author jwh
 *
 */
public class CuratorPathChildrenCache {

	// ����Curatorʵ��
	private static CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.1.141:2181")
			.retryPolicy(new ExponentialBackoffRetry(5000, 2)).build();

	public static void main(String[] args) {
		// ����curatorʵ��
		client.start();

		// �����ӽڵ��������
		PathChildrenCache pathChildCache = new PathChildrenCache(client, "/a/b", true);

		try {
			pathChildCache.start();
			pathChildCache.getListenable().addListener(new PathChildrenCacheListener() {

				public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {

					switch (event.getType()) {
					case CHILD_ADDED:

						System.out.println("�ӽڵ�����¼�"+event.getData().getPath());
						break;

					case CHILD_UPDATED:
						System.out.println("�ӽڵ�����¼�"+event.getData().getPath());
						break;

					case CHILD_REMOVED:
						System.out.println("�ӽڵ�ɾ���¼�"+event.getData().getPath());
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
