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

		// 子节点改变的监听
		zkClient.subscribeChildChanges("/a", new IZkChildListener() {

			/**
			 * parentPath 子节点变更通知对应的父节点路径
			 *  currentChilds 子节点的相对列表，如果没有子节点，会传入null,当子节点变更时，服务器会通知客户端，节点已经变化
			 *  ，而且把当前最新的子节点列表返回给客户端
			 */
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {

				System.out.println("parentPath=" + parentPath + "currentChilds=" + currentChilds);
			}

		});
		
		//向节点写数据
	   zkClient.writeData("/a", "writerData");
	   //读取节点数据
		System.out.println(zkClient.readData("/a"));
		
		//节点数据变化，添加监听事件
		zkClient.subscribeDataChanges("/a/e", new IZkDataListener() {

			public void handleDataChange(String dataPath, Object data) throws Exception {
				
				System.out.println("数据改变+datapath=="+dataPath+"data=="+data);
			}

			public void handleDataDeleted(String dataPath) throws Exception {
				
				System.out.println("节点删除+datapath=="+dataPath);
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
