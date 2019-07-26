package com.wbd.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
/**
 * zookeeper�ͻ�����������Ự�Ľ�����һ���첽�Ĺ��̣����췽���ڴ�����ͻ��˳�ʼ���������������أ���
 * �������²�û������������һ�����õĻỰ��
 * ���Ự����������Ϻ�zookeeper����˻���Ự��Ӧ�Ŀͻ��˷���һ���¼�֪ͨ���Ը�֪�ͻ��ˣ�
 * �ͻ���ֻ�л�ȡ���֪֮ͨ�󣬲������������˻Ự
 * CONNECTING ��������
 * CONNECTED��ʾ�Ѿ����ӳɹ�
 * zookeeper ��watcher�¼�����zookeeper�ڵ��б仯ʱ����������֪ͨ�ͻ���
 * ���ͻ��˻�ȡ��֪֮ͨ��������ҵ����
 * @author zgh
 *
 *����sessionid��sessionpwd�������ûỰ����ά��֮ǰ�Ự����Ч��
 */
public class ZookeeperWithSessionId_PWD implements Watcher{

	private static CountDownLatch countDownLatch = new CountDownLatch(1);
	
	
	public static void main(String[] args) {
		
       //1.���ӵ�zookeeper�ķ�������ַ
	  //2.ָ�ͻ�����������Ự�ĳ�ʱʱ�䣬����Ϊ��λ��
		try {
			ZooKeeper zookeeper = new ZooKeeper("192.168.1.141:2181",5000,new ZookeeperWithSessionId_PWD());
		  System.out.println(zookeeper.getState());
		  countDownLatch.await();
		  System.out.println(zookeeper.getState());
		  System.out.println("zookeeper ֮ǰ�Ķ���"+zookeeper);
		  long sessionId = zookeeper.getSessionId();
		  byte[] pwd = zookeeper.getSessionPasswd();
		  System.out.println(new String(pwd));
		  zookeeper = new ZooKeeper("192.168.1.141:2181",5000,new ZookeeperWithSessionId_PWD(),sessionId,pwd);
		  System.out.println("zookeeper before"+zookeeper);
		  Thread.sleep(Integer.MAX_VALUE);
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	//�÷�������������zookeeper��������watcher֪ͨ���յ�����˵ķ�����syncConnected�¼�֮�󣬽���������ϵĵȴ�����
	public void process(WatchedEvent event) {
		
		System.out.println(event);
		if(KeeperState.SyncConnected==event.getState()) {
			countDownLatch.countDown();
		}
		
	}

}
