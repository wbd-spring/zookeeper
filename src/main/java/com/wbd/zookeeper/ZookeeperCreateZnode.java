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
 * �����ڵ�
 * 1.�����Ľӿ���2�ַ�ʽ�� ͬ�����첽
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
			//1.ͬ�������ڵ�ķ�ʽ
			String path = zookeeper.create("/zk-book", "abcdef".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
			System.out.println("�����ɹ�="+path);
         
			//2.�첽�����ڵ�ķ�ʽ����Ҫʵ��StringCallbacke�ӿڣ�������˴�����ϣ�zookeeper�ͻ��˻��Զ����������������������߼�����
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
		 * rc=resultcode  �������Ӧ�룬
		 * 0=ok
		 * -4=�ͻ��������˶Ͽ�����
		 * -110=ָ���ڵ��Լ�����
		 * -112=�Ự�Ѿ�����
		 * 
		 * path��
		 * �ӿڵ����Ǵ���api�����ݽڵ�·������ֵ
		 * ctx,�ӿڵ���ʱ����api��ctxֵ
		 * name���ڵ����������
		 * 
		 */
		public void processResult(int rc, String path, Object ctx, String name) {
			
			System.out.println("��Ӧ�룺"+rc+"\t"+"·��="+path+"\t"+"�������������Ϣ="+ctx+"\t"+"�ڵ�����·��="+name);
			
		}
		
	}

