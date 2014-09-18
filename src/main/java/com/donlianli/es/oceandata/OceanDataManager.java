package com.donlianli.es.oceandata;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;

import com.donlianli.es.common.SearchModel;
import com.donlianli.es.util.DatabaseUtils;
import com.donlianli.es.util.ESUtils;


public class OceanDataManager {
	public static void main(String[] argvs){
		OceanDataManager manager = new OceanDataManager();
		manager.indexDataDirect();
	}

	private void indexDataDirect() {
		Connection conn = DatabaseUtils.getOracleConnection();
		String sql = "select * from ocean_data";
		PreparedStatement st = null;
		ResultSet rs = null;
		List<SearchModel> list = new ArrayList<SearchModel>();
		int count =0;
		try{
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
			System.out.println("begin index oceanData ");
			while(rs.next()){
				String oid = rs.getString("O_ID");
				String firstKey = rs.getString("FIRST_KEY");
				String secondKey = rs.getString("SEC_KEY");
				String thirdKey = rs.getString("THIRD_KEY");
				String catId = rs.getString("CAT_ID");
				String tableName = rs.getString("TAB_NAME");
				BigDecimal isShow = rs.getBigDecimal("IS_SHOW");
				BigDecimal order = rs.getBigDecimal("D_ORDER");
				Timestamp update = rs.getTimestamp("UPDATE_TIME");
				String mainDesc = rs.getString("MAIN_DESC");
				String mainTitle = rs.getString("MAIN_TITLE");
				BigDecimal clickCount = rs.getBigDecimal("CLICK_COUNT");
				list.add(new SearchModel(oid,firstKey,secondKey,thirdKey,catId,tableName,isShow,order,update,mainDesc,mainTitle,clickCount));
				if(list.size()%20000 ==0){
					count += 20000;
					onlyPrint(list);
					insertMysql(list);
//					this.bulkIndex(list);
					System.out.println(" indexCount: " + count);
					list.clear();
				}
			}
			insertMysql(list);
//			this.bulkIndex(list);
			System.out.println("index oceanData finish ");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
			if(rs!= null){
				rs.close();
			}
			if(st!= null){
				st.close();
			}
			conn.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	private void onlyPrint(List<SearchModel> list) {
		if(list!=null && list.size()>0){
			SearchModel sm = list.get(0);
			String json = ESUtils.toJson(sm);
			System.out.println(json);
		}
	}

	private void insertMysql(List<SearchModel> dataList) throws SQLException{
		Connection conn = DatabaseUtils.getMysqlConnection();
		String sql = "insert into ocean_data values(?,?,?,?,?"
				+ ",?,?,?,?,?"
				+ ",?,?,?)";
		PreparedStatement stm = conn.prepareStatement(sql);
		conn.setAutoCommit(false);
		for(SearchModel sm: dataList){
			try{
				stm.setString(1, sm.getOid());
				stm.setString(2, sm.getFirstKey());
				stm.setString(3, sm.getSecondKey());
				stm.setString(4, sm.getThirdKey());
				stm.setString(5, sm.getCatIds().get(0).toString());
				
				stm.setString(6, sm.getTableName());
				stm.setDate(7, new java.sql.Date(sm.getUpdateTime().getTime()));
				stm.setInt(8, sm.getIsShow());
				stm.setLong(9, sm.getOrder());
				stm.setString(10, sm.getTitle());
				
				stm.setString(11, sm.getMainDesc());
				stm.setString(12, sm.getTitleUrl());
				stm.setLong(13, sm.getClickCount());
			
				stm.addBatch();
			}
			catch(Exception e){
				
			}
		}
		stm.executeBatch();
		conn.commit();
		conn.close();
	}
	private void bulkIndex(List<SearchModel> dataList) {
		Client client = ESUtils.getCodeClient();
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		long b = System.currentTimeMillis();
		for(int i=0,l=dataList.size();i<l;i++){
			//业务对象
			SearchModel sm = dataList.get(i);
			String json = ESUtils.toJson(sm);
			IndexRequestBuilder indexRequest = client.prepareIndex(ESUtils.INDEX_NAME,
					ESUtils.TYPE_NAME)
	        .setSource(json).setId(sm.getOid());
			//添加到builder中
			bulkRequest.add(indexRequest);
		}
		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
		if (bulkResponse.hasFailures()) {
			System.out.println(bulkResponse.buildFailureMessage());
		}
		long useTime = System.currentTimeMillis()-b;
		System.out.println("useTime:" + useTime);
	}
	
}
