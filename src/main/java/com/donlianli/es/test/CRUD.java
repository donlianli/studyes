package com.donlianli.es.test;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;

import com.donlianli.es.util.ESUtils;

public class CRUD {
	public static void main(String[] args) {
		add();
		delete();
		get();
		update();
	}

	private static void add() {
		Client client = ESUtils.getClient();
		IndexResponse indexResponse = client.prepareIndex().setIndex(ESUtils.getIndexName())
				.setType(ESUtils.getTypeName())
				.setSource("{\"prodId\":1,\"prodName\":\"ipad5\",\"prodDesc\":\"比你想的更强大\",\"catId\":1}")
				.setId("1")
				.execute()
				.actionGet();
		System.out.println("添加成功,isCreated="+indexResponse.isCreated());	
		ESUtils.closeClient(client);
	}
	private static void get() {
		Client client = ESUtils.getClient();
		GetResponse getResponse = client.prepareGet().setIndex(ESUtils.getIndexName())
				.setType(ESUtils.getTypeName())
				.setId("1")
				.execute()
				.actionGet();
		System.out.println("get="+getResponse.getSourceAsString());	
		ESUtils.closeClient(client);
	}
	private static void delete() {
		Client client = ESUtils.getClient();
		DeleteResponse delResponse = client.prepareDelete().setIndex(ESUtils.getIndexName())
				.setType(ESUtils.getTypeName())
				.setId("1")
				.execute()
				.actionGet();
		System.out.println("del is found="+delResponse.isFound());
		ESUtils.closeClient(client);
	}

	private static void update() {
		Client client = ESUtils.getClient();
		GetResponse getResponse = client.prepareGet().setIndex(ESUtils.getIndexName())
				.setType(ESUtils.getTypeName())
				.setId("1")
				.execute()
				.actionGet();
		System.out.println("berfore update version="+getResponse.getVersion());
		
		UpdateResponse updateResponse = client.prepareUpdate().setIndex(ESUtils.getIndexName())
				.setType(ESUtils.getTypeName())
				//当更新的内容不存在时，如何处理
				.setDoc("{\"prodId\":1,\"prodName\":\"ipad5\",\"prodDesc\":\"比你想的更强大\",\"catId\":1}")
				.setId("1")
				.execute()
				.actionGet();
		
		System.out.println("更新成功，isCreated="+updateResponse.isCreated());	
		getResponse = client.prepareGet().setIndex(ESUtils.getIndexName())
				.setType(ESUtils.getTypeName())
				.setId("1")
				.execute()
				.actionGet();
		System.out.println("get version="+getResponse.getVersion());
		ESUtils.closeClient(client);
	}
}
