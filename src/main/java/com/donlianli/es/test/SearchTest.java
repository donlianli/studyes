package com.donlianli.es.test;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.donlianli.es.util.ESUtils;
/**
 * 简单的搜索示例
 * @author donlianli
 */
public class SearchTest {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Client client = ESUtils.getClient();
		/**
		 * queryString搜索传入的字符串是有一定的语法的，这个语法基本和lucene的query string
		 * 一样。可以查看elasticsearch，也可以查看lucene。
		 * es地址见http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/query-dsl-query-string-query.html
		 * 最简单的查询就是下面，相当于like '%sys%'
		 */
		QueryBuilder query = QueryBuilders.queryString("systemName:*sys*");
		SearchResponse response = client.prepareSearch("test")
				.setTypes("log")
				//设置查询条件,
		        .setQuery(query)
		        .setFrom(0).setSize(60)
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
