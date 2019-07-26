package com.wbd.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;

/**
 * 创建节点
 * 1.创建的接口有2种方式， 同步与异步
 * @author jwh
 *
 */
public class ZookeeperCreateZnode implements Watcher{

	private static CountDownLatch countDownLatch = new CountDownLatch(1);
	
	public static void main(String[] args) {
		
		try {
			ZooKeeper zookeeper = new ZooKeeper("192.168.1.141:2181",2000,new ZookeeperCreateZnode());
			countDownLatch.await();
			System.out.println(zookeeper.getState());
			//1.同步创建节点的方式
			String path = zookeeper.create("/zk-book", "abcdef".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
			System.out.println("创建成功="+path);
         
			//2.异步创建节点的方式，需要实现StringCallbacke接口，当服务端创建完毕，zookeeper客户端回自动调用这个方法，进行相关逻辑处理
		 zookeeper.create("/zgh", "zhuguanghe".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL, new IStringCallback(), "I am zs");
			
			Thread.sleep(Integer.MAX_VALUE);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			e.printStackTrace();
		}

	}

	public void process(WatchedEvent event) {
	
		System.out.println("event=="+event);
		if(event.getState()==KeeperState.SyncConnected) {
			countDownLatch.countDown();
		}
		
	}

}
	class IStringCallback implements StringCallback{

		/**
		 * rc,
		 * rc=resultcode  服务端响应码，
		 * 0=ok
		 * -4=客户端与服务端断开连接
		 * -110=指定节点以及存在
		 * -112=会话已经过期
		 * 
		 * path，
		 * 接口调用是传入api的数据节点路径参数值
		 * ctx,接口调用时传入api的ctx值
		 * name，节点的完整名称
		 * 
		 */
		public void processResult(int rc, String path, Object ctx, String name) {
			
			System.out.println("响应码："+rc+"\t"+"路径="+path+"\t"+"传入的上下文信息="+ctx+"\t"+"节点完整路径="+name);
			
		}
		
	}

