package com.donlianli.es.test;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.donlianli.es.util.ESUtils;
/**
 * 最简单的搜索示例
 * @author donlianli
 */
public class SimplestSearch {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Client client = ESUtils.getClient();
		QueryBuilder query = QueryBuilders.queryString("system");
		SearchResponse response = client.prepareSearch("test")
				.setTypes("log")
				//设置查询条件,
		        .setQuery(query)
		        .setFrom(0).setSize(10)
		        .execute()
		        .actionGet();
		/**
		 * SearchHits是SearchHit的复数形式，表示这个是一个列表
		 */
		SearchHits shs = response.getHits();
		for(SearchHit hit : shs){
			System.out.println(hit.getSourceAsString());
		}
		client.close();
	}

}
