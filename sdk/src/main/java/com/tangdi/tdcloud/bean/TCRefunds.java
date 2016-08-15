package com.tangdi.tdcloud.bean;

import java.util.List;


/**
 * 批量退款订单查询结果类
 * 
 * @author deng_wk
 * @Date 2016-06-29
 */
public class TCRefunds {

	private  Integer total;
	private List<TCRefund> orders;
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public List<TCRefund> getOrders() {
		return orders;
	}
	public void setOrders(List<TCRefund> orders) {
		this.orders = orders;
	}
	
    
}
