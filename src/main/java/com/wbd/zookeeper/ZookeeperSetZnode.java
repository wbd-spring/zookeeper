package com.wbd.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 更新节点数据， 需要提供版本号，采用CAS 比较与交换的思想， 在更新前先查看值是否为期望的值， 如果是 就更新， 如果不是，不更新， 利用版本号的特性，
 * 每次更新之前对比一下版本号， 如果一致就更新， 如果不一致， 更新失败 说明有其他的线程更新了， 必须重新获取最新版本号， 再次提交更新请求
 * 
 * @author jwh
 *
 */
public class ZookeeperSetZnode implements Watcher {

	private static CountDownLatch countDownLatch = new CountDownLatch(1);
     private static ZooKeeper zookeeper ;
	public static void main(String[] args) {
		try {
			 zookeeper = new ZooKeeper("192.168.1.139:2181", 5000, new ZookeeperSetZnode());
			System.out.println("zookeeper before status:" + zookeeper.getState());
			countDownLatch.await();
			System.out.println("zookeeper after status:" + zookeeper.getState());
			//设置节点内容
//			Stat stat = zookeeper.setData("/a", "aaa".getBytes(), -1);
//			System.out.println(stat.getVersion());
//			Stat stat2 = zookeeper.setData("/a", "bbb".getBytes(), stat.getVersion());
//			System.out.println(stat2.getVersion());
//			Stat stat3= zookeeper.setData("/a", "cc".getBytes(), stat2.getVersion());
//			System.out.println(stat3.getVersion());
			
			//查看节点是否存在
			Stat stat = zookeeper.exists("/a", true);
			System.out.println(stat.getVersion());
			Thread.sleep(Integer.MAX_VALUE);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void process(WatchedEvent event) {
		System.out.println(event);

		if (event.getState() == KeeperState.SyncConnected) {

			if (EventType.None == event.getType() && event.getPath() == null) {
				countDownLatch.countDown();

			}else if(EventType.NodeDeleted==event.getType()) {
				System.out.println("node"+event.getPath()+"delete..");
				try {
					Stat stat = zookeeper.exists("/a", true);
				} catch (KeeperException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else if(EventType.NodeDataChanged==event.getType()) {
				System.out.println("node"+event.getPath()+"change..");
				try {
					Stat stat = zookeeper.exists("/a", true);
				} catch (KeeperException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}

	}

}
