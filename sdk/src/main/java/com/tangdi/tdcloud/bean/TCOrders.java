package com.tangdi.tdcloud.bean;

import java.util.List;


/**
 * 批量订单查询结果接收类
 * 
 * @author deng_wk
 * @Date 2016-06-29
 */
public class TCOrders {

	private  Integer total;
	private List<TCOrder> orders;
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public List<TCOrder> getOrders() {
		return orders;
	}
	public void setOrders(List<TCOrder> orders) {
		this.orders = orders;
	}
	
}
