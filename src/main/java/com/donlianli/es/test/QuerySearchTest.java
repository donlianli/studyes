package com.donlianli.es.test;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.donlianli.es.util.ESUtils;

public class QuerySearchTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Client client = ESUtils.getClient();
		/**
		 * 创建查询条件，QueryBuilders相当于Hibernate的Restrictions，
		 * 而QueryBuilder则相当于一个Criteria,可以不停的增加本身对象
		 */
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		//termQuery就类似于sql中的=''。
		query.must(QueryBuilders.termQuery("systemName", "system"));
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
