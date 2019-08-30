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
 * ���½ڵ����ݣ� ��Ҫ�ṩ�汾�ţ�����CAS �Ƚ��뽻����˼�룬 �ڸ���ǰ�Ȳ鿴ֵ�Ƿ�Ϊ������ֵ�� ����� �͸��£� ������ǣ������£� ���ð汾�ŵ����ԣ�
 * ÿ�θ���֮ǰ�Ա�һ�°汾�ţ� ���һ�¾͸��£� �����һ�£� ����ʧ�� ˵�����������̸߳����ˣ� �������»�ȡ���°汾�ţ� �ٴ��ύ��������
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
			//���ýڵ�����
//			Stat stat = zookeeper.setData("/a", "aaa".getBytes(), -1);
//			System.out.println(stat.getVersion());
//			Stat stat2 = zookeeper.setData("/a", "bbb".getBytes(), stat.getVersion());
//			System.out.println(stat2.getVersion());
//			Stat stat3= zookeeper.setData("/a", "cc".getBytes(), stat2.getVersion());
//			System.out.println(stat3.getVersion());
			
			//�鿴�ڵ��Ƿ����
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
