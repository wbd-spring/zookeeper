package com.wbd.zookeeper.zkclient;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

public class ZkClientDemo {

	public static void main(String[] args) {

		ZkClient zkClient = new ZkClient("192.168.1.141:2181");

		System.out.println("zkclient session established");
		zkClient.createPersistent("/zgh/efg1", true);

		zkClient.deleteRecursive("/zgh");

		List<String> children = zkClient.getChildren("/a");
		

		System.out.println(children);

		// �ӽڵ�ı�ļ���
		zkClient.subscribeChildChanges("/a", new IZkChildListener() {

			/**
			 * parentPath �ӽڵ���֪ͨ��Ӧ�ĸ��ڵ�·��
			 *  currentChilds �ӽڵ������б����û���ӽڵ㣬�ᴫ��null,���ӽڵ���ʱ����������֪ͨ�ͻ��ˣ��ڵ��Ѿ��仯
			 *  �����Ұѵ�ǰ���µ��ӽڵ��б��ظ��ͻ���
			 */
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {

				System.out.println("parentPath=" + parentPath + "currentChilds=" + currentChilds);
			}

		});
		
		//��ڵ�д����
	   zkClient.writeData("/a", "writerData");
	   //��ȡ�ڵ�����
		System.out.println(zkClient.readData("/a"));
		
		//�ڵ����ݱ仯����Ӽ����¼�
		zkClient.subscribeDataChanges("/a/e", new IZkDataListener() {

			public void handleDataChange(String dataPath, Object data) throws Exception {
				
				System.out.println("���ݸı�+datapath=="+dataPath+"data=="+data);
			}

			public void handleDataDeleted(String dataPath) throws Exception {
				
				System.out.println("�ڵ�ɾ��+datapath=="+dataPath);
			}
			
		} );
		
		 zkClient.writeData("/a/e", "goods change");
		try {
			Thread.sleep(Integer.MAX_VALUE);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
