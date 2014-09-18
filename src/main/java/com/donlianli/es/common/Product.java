package com.donlianli.es.common;

import java.io.Serializable;
/**
 * 商品模型
 * @author donlianli@126.com
 * 2014年9月14日
 */
public class Product  implements Serializable{
	private static final long serialVersionUID = -6780498052340800675L;
	//主键
	private long prodId;
	//商品名称
	private String prodName;
	//商品描述
	private String prodDesc;
	//所属分类
	private long catId;
	public Product(long prodId,String prodName,String prodDesc,long catId){
		this.prodId = prodId;
		this.prodName = prodName;
		this.prodDesc = prodDesc;
		this.catId = catId;
	}
	public Product(){
		
	}
	public long getProdId() {
		return prodId;
	}
	public void setProdId(long prodId) {
		this.prodId = prodId;
	}
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public String getProdDesc() {
		return prodDesc;
	}
	public void setProdDesc(String prodDesc) {
		this.prodDesc = prodDesc;
	}
	public long getCatId() {
		return catId;
	}
	public void setCatId(long catId) {
		this.catId = catId;
	}
	
}
