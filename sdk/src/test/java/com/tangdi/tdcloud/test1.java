package com.tangdi.tdcloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.tangdi.tdcloud.TCEumeration.PAY_CHANNEL;
import com.tangdi.tdcloud.bean.TCException;
import com.tangdi.tdcloud.bean.TCOrder;
import com.tangdi.tdcloud.bean.TCOrders;
import com.tangdi.tdcloud.bean.TCQueryParameter;

public class test1 {
/*	protected static Map<String, Object> params = new HashMap<String, Object>();
	public static void main(String[] args) {
		 TdCloud.registerApp("748034501028216832", null, "39a7a518-9ac8-4a9e-87bc-7885f33cf180",
	                "e14ae2db-608c-4f8b-b863-c8c18953eeww");
		  TCQueryParameter param = new TCQueryParameter();
		 param.setId("1000000000002016071445300480");
		 try {
			TCOrder order =TCPay.startQueryPay(param);
			System.out.println(order.getTotalFee());
			System.out.println(order.getTitle());
			System.out.println(order.getId());
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TCOrder bcOrder = new TCOrder(PAY_CHANNEL.valueOf("ALI_WEB"), 1, "872395d321c24ab580fd9d8cede17942", "测试商品名称");
		    bcOrder.setBillTimeout(600);
		    bcOrder.setOptional(null);
		    String aliReturnUrl = "http://124.193.113.122:48080/Tdcloud-PC-Demo/page/aliReturnUrl.jsp";
		    bcOrder.setReturnUrl(aliReturnUrl);
		    try {
				TCPay.startTCPay(bcOrder);
			} catch (TCException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		 //String date1= TCUtilPrivate.transferDateFromToString("20151212121212");
		 //System.out.println(date1);
		 //System.out.println(Long.parseLong("20151212121212l"));
		 //System.out.println(Boolean.parseBoolean("true"));
		 
		 //测试订单批量查询
		 TCQueryParameter param = new TCQueryParameter();
		 //param.setBillNo("cdfe1f67dbca4a69a350f10062645cfb");
		 param.setNeedDetail(true);
		 try {
			TCOrders orders =TCPay.startQueryPays(param);
			List<TCOrder> bills = orders.getOrders();
			System.out.println(orders.getTotal());
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 params.putAll(null);
		 
	}*/
	
	
	public static void main(String[] args) {
		//2010031906272929^80^SUCCESS$jax_chuanhang@alipay.com^2088101003147483^0.01^SUCCESS
        
        //不退手续费结果返回格式：交易号^退款金额^处理结果。 
        //2010031906272929^80^SUCCESS
		String str1="2010031906272929^80^SUCCESS$jax_chuanhang@alipay.com^2088101003147483^0.01^SUCCESS";
		String Str2="2010031906272929^80^SUCCESS";
		
		String []  str = getarray(Str2);
		System.out.println(str.length);
		for (int i = 0; i < str.length; i++) {
			System.out.println(str[i]);
		}
		
	}
	
	public static String[] getarray(String str){
		if(StringUtils.isBlank(str)){
			return null;
		}
		if(str.indexOf("$")!=-1){
			return str.replace("$", "^").split("\\^");
		}else{
			return str.split("\\^");
		}
	}
}
