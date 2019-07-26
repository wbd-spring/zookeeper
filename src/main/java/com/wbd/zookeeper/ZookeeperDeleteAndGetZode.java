package com.wbd.zookeeper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback.Children2Callback;
import org.apache.zookeeper.AsyncCallback.VoidCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ZookeeperDeleteAndGetZode implements Watcher {
	private static CountDownLatch cdl = new CountDownLatch(1);
	private static ZooKeeper zookeeper = null;
	private static Stat stat = new Stat();

	public static void main(String[] args) {
		try {
			zookeeper = new ZooKeeper("192.168.1.141:2181", 5000, new ZookeeperDeleteAndGetZode());

			cdl.await();
			// ��ѯ�ڵ�
			// byte[] b = zookeeper.getData("/zgh0000000013", true, stat);

			// ɾ���ڵ�
			// zookeeper.delete("/zgh0000000017", 1, new DeleteZnodeCallback(), "delete");

			// System.out.println(new String(b));

			// ��ȡһ���ڵ��������ӽڵ�,ͬ���ӿ�
			List<String> childrenList = zookeeper.getChildren("/a", true);

			System.out.println(childrenList);

			// ��ȡһ���ڵ��������ӽڵ�,�첽�ӿ�
			 zookeeper.getChildren("/a",true,new getChildrenCallback(),"�첽��ȡ�ڵ���Ϣ");
			Thread.sleep(Integer.MAX_VALUE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void process(WatchedEvent event) {
		System.out.println(event);
		if (event.getState() == KeeperState.SyncConnected) {

			if (EventType.None == event.getType() && event.getPath() == null) {
				cdl.countDown();
			}
			if (event.getType() == EventType.NodeDataChanged) {
				try {
					System.out.println(new String(zookeeper.getData(event.getPath(), true, stat)));
				} catch (KeeperException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (event.getType() == EventType.NodeChildrenChanged) {
				try {
					System.out.println("�ӽڵ���֪ͨ" + new String(zookeeper.getData(event.getPath(), true, stat)));
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

/**
 * ɾ���ڵ�ص���
 * 
 * @author jwh
 *
 */
class DeleteZnodeCallback implements VoidCallback {

	public void processResult(int rc, String path, Object ctx) {
		System.out.println("rc==" + rc + ": path==" + path + ": ctx==" + ctx);

	}
	
}

//�첽��ȡ�ӽڵ���Ϣ
class getChildrenCallback implements Children2Callback{

	public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
		
		System.out.println("��ȡ"+path+"�ӽڵ������Ϣ"+"������="+rc+"����Ϣ="+children+"stat"+stat);
		
	}
	
}
