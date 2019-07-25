package com.wbd.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
/**
 * zookeeper客户端与服务器会后的建立是一个异步的过程，构造方法在处理完客户端初始化工作后立即返回，在
 * 大多情况下并没有真正建立好一个可用的会话，
 * 当会话真正创建完毕后，zookeeper服务端会向会话对应的客户端发送一个事件通知，以告知客户端，
 * 客户端只有获取这个通知之后，才算真正建立了会话
 * CONNECTING 正在连接
 * CONNECTED表示已经连接成功
 * zookeeper 的watcher事件，当zookeeper节点有变化时，服务器会通知客户端
 * ，客户端获取到通知之后进行相关业务处理
 * @author zgh
 *
 */
public class ZookeeperApplication implements Watcher{

	private static CountDownLatch countDownLatch = new CountDownLatch(1);
	
	
	public static void main(String[] args) {
		
       //1.连接的zookeeper的服务器地址
	  //2.指客户端与服务器会话的超时时间，毫秒为单位，
		try {
			ZooKeeper zookeeper = new ZooKeeper("192.168.1.5:2181",5000,new ZookeeperApplication());
		  System.out.println(zookeeper.getState());
		  countDownLatch.await();
		  System.out.println(zookeeper.getState());
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	//该方法负责处理来自zookeeper服务器的watcher通知，收到服务端的发来的syncConnected事件之后，解除主程序上的等待阻塞
	public void process(WatchedEvent event) {
		
		System.out.println(event);
		if(KeeperState.SyncConnected==event.getState()) {
			countDownLatch.countDown();
		}
		
	}

}
