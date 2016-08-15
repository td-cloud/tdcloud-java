package com.tangdi.tdcloud;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.StrictExpectations;

import org.junit.Assert;
import org.junit.Test;

import com.tangdi.tdcloud.TCEumeration.RESULT_TYPE;
import com.tangdi.tdcloud.bean.TCException;
import com.tangdi.tdcloud.bean.TCOrder;
import com.tangdi.tdcloud.bean.TCOrders;
import com.tangdi.tdcloud.bean.TCQueryParameter;


/**
 * 订单查询单元测试，包括单笔查询，批量查询、总数查询
 * 
 * @author deng_wk
 * @Date 2016-06-29
 */
public class BillQueryTest {

	static String billNo = TCUtil.generateRandomUUIDPure();

    /**
     * 测试批量查询
     */
	
    static void testQueryPays() {
    	
        TCQueryParameter param = new TCQueryParameter();
        TCOrders orders = new TCOrders();
        try {
            orders = TCPay.startQueryPays(null);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(TestConstant.QUERY_PARAM_EMPTY));
        }

        try {
            param.setBillNo(TestConstant.BILL_NO_WITH_SPECIAL_CHARACTER);
            orders = TCPay.startQueryPays(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(TestConstant.BILL_NO_FORMAT_INVALID));
        }

        try {
            param.setBillNo(billNo.substring(0, 7));
            orders = TCPay.startQueryPays(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(TestConstant.BILL_NO_FORMAT_INVALID));
        }

        try {
            param.setBillNo(billNo + "A");
            orders = TCPay.startQueryPays(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(TestConstant.BILL_NO_FORMAT_INVALID));
        }
        param.setBillNo(billNo);

        try {
            param.setPages(-1);
            orders = TCPay.startQueryPays(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name())
                            || ex.getMessage().contains(RESULT_TYPE.OTHER_ERROR.name()));// 服务端验证，可能存在网络问题或者响应错误问题，加上OTHER_ERROR判断
        }

        try {
            param.setNumber(300);
            orders = TCPay.startQueryPays(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(TestConstant.LIMIT_FORMAT_INVALID));
        }

        // mock网络请求
        if (TCCache.isSandbox()) {
            mockSandboxQueryPays();
            return;
        }
        mockQueryPays();
    }
      /**
       * 单笔查询
       */
    static void testQueryPay() {
    	TCQueryParameter param = new TCQueryParameter();
        try {
            TCPay.startQueryPay(null);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(TestConstant.QUERY_PARAM_EMPTY));
        }

        try {
        	param.setBillNo(TestConstant.BILL_NO_WITH_SPECIAL_CHARACTER);
            TCPay.startQueryPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(TestConstant.BILL_NO_FORMAT_INVALID));
        }
        // mock网络请求
        if (TCCache.isSandbox()) {
            mockSandboxQueryPay();
            return;
        }

        mockQueryPay();
    }

    

     /**
      * 批量查询支付订单
      */
    private static void mockQueryPays() {
        final Map<String, Object> returnMap = new HashMap<String, Object>();
        TCOrders orders = new TCOrders();
        returnMap.put("result_code", 0);
        returnMap.put("result_msg", "OK");
        returnMap.put("err_detail", "");
        returnMap.put("bills", generateMockBills());
        returnMap.put("count", generateMockBills().size());

        new Expectations() {
            {
                Deencapsulation.invoke(TCPay.class, "doGet",
                        withSubstring(TCUtilPrivate.getkApiQueryPays().substring(14)),
                        withAny(Map.class));
                returns(returnMap);
                result = new TCException(TCErrorCode.e_101001.code(), RESULT_TYPE.APP_INVALID.name(),TCErrorCode.e_101001.error());
            }
        };

        TCQueryParameter param = new TCQueryParameter();
        param.setBillNo(billNo);
        try {
            orders = TCPay.startQueryPays(param);
            Assert.assertEquals("", 2, orders.getOrders().size());
            for (TCOrder order : orders.getOrders()) {
                Assert.assertEquals("", TestConstant.MOCK_OBJECT_ID, order.getId());
                Assert.assertEquals("", TestConstant.MOCK_BILL_NO, order.getBillNo());
                Assert.assertEquals("",
                        TestUtil.transferDateFromLongToString(TestConstant.MOCK_CREATE_TIME),
                        order.getDateTime());
                Assert.assertEquals("", TestConstant.MOCK_CHANNEL,
                        order.getChannel().toString().split("_")[0]);
                Assert.assertEquals("", TestConstant.MOCK_SUB_CHANNEL,
                        order.getChannel().toString());
                Assert.assertEquals("", TestConstant.MOCK_PAY_RESULT, order.isResult());
                Assert.assertTrue("", order.getOptionalString().equals("") || order
                        .getOptionalString().equals(TestConstant.MOCK_OPTIONAL_JSON_STRING));
                Assert.assertEquals("", true, order.isRevertResult());
                Assert.assertEquals("", true, order.isRefundResult());
                Assert.assertTrue("", order.getMessageDetail().equals("不显示") || order
                        .getMessageDetail().equals(TestConstant.MOCK_MESSAGE_DETAIL_STRING));
                Assert.assertEquals("", TestConstant.MOCK_TITLE, order.getTitle());
                Assert.assertEquals("", TestConstant.MOCK_TOTAL_FEE, order.getTotalFee());
                Assert.assertTrue("", order.getChannelTradeNo().equals(TestConstant.MOCK_TRADE_NO)
                        || order.getChannelTradeNo().equals(""));
            }
        } catch (TCException ex) {
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }

        try {
            orders = TCPay.startQueryPays(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(RESULT_TYPE.APP_INVALID.name()));
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(TestConstant.MOCK_APP_INVALID_ERRMSG));
        }
    }

    /**
     * 沙箱模式测试批量支付订单查询
     */
    private static void mockSandboxQueryPays() {
        final Map<String, Object> returnMap = new HashMap<String, Object>();
        TCOrders orders = new TCOrders();
        returnMap.put("result_code", 0);
        returnMap.put("result_msg", "OK");
        returnMap.put("err_detail", "");
        returnMap.put("bills", generateMockBills());
        returnMap.put("count", generateMockBills().size());

        new Expectations() {
            {
                Deencapsulation.invoke(TCPay.class, "doGet",
                        withSubstring(TCUtilPrivate.getkApiSandboxQueryPays().substring(14)),
                        withAny(Map.class));
                returns(returnMap);
                result = new TCException(TCErrorCode.e_101001.code(), RESULT_TYPE.APP_INVALID.name(),TCErrorCode.e_101001.error());
            }
        };

        TCQueryParameter param = new TCQueryParameter();
        param.setBillNo(billNo);
        try {
            orders = TCPay.startQueryPays(param);
            Assert.assertEquals("", 2, orders.getOrders().size());
            for (TCOrder order : orders.getOrders()) {
                Assert.assertEquals("", TestConstant.MOCK_OBJECT_ID, order.getId());
                Assert.assertEquals("", TestConstant.MOCK_BILL_NO, order.getBillNo());
                Assert.assertEquals("",
                        TestUtil.transferDateFromLongToString(TestConstant.MOCK_CREATE_TIME),
                        order.getDateTime());
                Assert.assertEquals("", TestConstant.MOCK_CHANNEL,
                        order.getChannel().toString().split("_")[0]);
                Assert.assertEquals("", TestConstant.MOCK_SUB_CHANNEL,
                        order.getChannel().toString());
                Assert.assertEquals("", TestConstant.MOCK_PAY_RESULT, order.isResult());
                Assert.assertTrue("", order.getOptionalString().equals("") || order
                        .getOptionalString().equals(TestConstant.MOCK_OPTIONAL_JSON_STRING));
                Assert.assertEquals("", true, order.isRevertResult());
                Assert.assertEquals("", true, order.isRefundResult());
                Assert.assertTrue("", order.getMessageDetail().equals("不显示") || order
                        .getMessageDetail().equals(TestConstant.MOCK_MESSAGE_DETAIL_STRING));
                Assert.assertEquals("", TestConstant.MOCK_TITLE, order.getTitle());
                Assert.assertEquals("", TestConstant.MOCK_TOTAL_FEE, order.getTotalFee());
                Assert.assertTrue("", order.getChannelTradeNo().equals(TestConstant.MOCK_TRADE_NO)
                        || order.getChannelTradeNo().equals(""));
            }
        } catch (TCException ex) {
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }

        try {
            orders = TCPay.startQueryPays(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(RESULT_TYPE.APP_INVALID.name()));
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(TestConstant.MOCK_APP_INVALID_ERRMSG));
        }
    }

    private static void mockQueryPay() {
        final Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("result_code", 0);
        returnMap.put("result_msg", "OK");
        returnMap.put("err_detail", "");
        Map<String, Object> payMap = new HashMap<String, Object>();
        buildMockBill(payMap);
        returnMap.put("pay", payMap);

        new Expectations() {
            {
                Deencapsulation.invoke(TCPay.class, "doGet",
                        withSubstring(TCUtilPrivate.getkApiQueryPay().substring(14)),
                        withAny(Map.class));
                returns(returnMap);
            }
        };
        TCOrder order;
        TCQueryParameter param = new TCQueryParameter();
        param.setBillNo(billNo);
        try {
            order = TCPay.startQueryPay(param);
            Assert.assertEquals("", TestConstant.MOCK_BILL_NO, order.getBillNo());
            Assert.assertEquals("", TestConstant.MOCK_TRADE_NO, order.getChannelTradeNo());
            Assert.assertEquals("", TestConstant.MOCK_OBJECT_ID, order.getId());
            Assert.assertEquals("", TestConstant.MOCK_PAY_RESULT, order.isResult());
            Assert.assertEquals("",
                    TestUtil.transferDateFromLongToString(TestConstant.MOCK_CREATE_TIME),
                    order.getDateTime());
            Assert.assertEquals("", TestConstant.MOCK_TOTAL_FEE, order.getTotalFee());
            Assert.assertEquals("", TestConstant.MOCK_CHANNEL,
                    order.getChannel().toString().split("_")[0]);
            Assert.assertEquals("", TestConstant.MOCK_SUB_CHANNEL, order.getChannel().toString());
            Assert.assertEquals("", TestConstant.MOCK_OPTIONAL_JSON_STRING,
                    order.getOptionalString());
            Assert.assertEquals("", true, order.isRevertResult());
            Assert.assertEquals("", true, order.isRefundResult());
            Assert.assertEquals("", TestConstant.MOCK_MESSAGE_DETAIL_STRING,
                    order.getMessageDetail());
            Assert.assertEquals("", TestConstant.MOCK_TITLE, order.getTitle());
        } catch (TCException e) {
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }

        new StrictExpectations() {
            {
                Deencapsulation.invoke(TCPay.class, "doGet",
                        withSubstring(TCUtilPrivate.getkApiQueryPay().substring(14)),
                        withAny(Map.class));
                result = new TCException(TCErrorCode.e_101001.code(), RESULT_TYPE.APP_INVALID.name(),TCErrorCode.e_101001.error());
            }
        };

        try {
            order = TCPay.startQueryPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(RESULT_TYPE.APP_INVALID.name()));
        }
    }

    private static void mockSandboxQueryPay() {
        final Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("result_code", 0);
        returnMap.put("result_msg", "OK");
        returnMap.put("err_detail", "");
        Map<String, Object> payMap = new HashMap<String, Object>();
        buildMockBill(payMap);
        returnMap.put("pay", payMap);

        new Expectations() {
            {
                Deencapsulation.invoke(TCPay.class, "doGet",
                        withSubstring(TCUtilPrivate.getkApiSandboxQueryPay().substring(14)),
                        withAny(Map.class));
                returns(returnMap);
            }
        };
        TCQueryParameter param = new TCQueryParameter();
        param.setBillNo(billNo);
        TCOrder order;
        try {
            order = TCPay.startQueryPay(param);
            Assert.assertEquals("", TestConstant.MOCK_BILL_NO, order.getBillNo());
            Assert.assertEquals("", TestConstant.MOCK_TRADE_NO, order.getChannelTradeNo());
            Assert.assertEquals("", TestConstant.MOCK_OBJECT_ID, order.getId());
            Assert.assertEquals("", TestConstant.MOCK_PAY_RESULT, order.isResult());
            Assert.assertEquals("",
                    TestUtil.transferDateFromLongToString(TestConstant.MOCK_CREATE_TIME),
                    order.getDateTime());
            Assert.assertEquals("", TestConstant.MOCK_TOTAL_FEE, order.getTotalFee());
            Assert.assertEquals("", TestConstant.MOCK_CHANNEL,
                    order.getChannel().toString().split("_")[0]);
            Assert.assertEquals("", TestConstant.MOCK_SUB_CHANNEL, order.getChannel().toString());
            Assert.assertEquals("", TestConstant.MOCK_OPTIONAL_JSON_STRING,
                    order.getOptionalString());
            Assert.assertEquals("", true, order.isRevertResult());
            Assert.assertEquals("", true, order.isRefundResult());
            Assert.assertEquals("", TestConstant.MOCK_MESSAGE_DETAIL_STRING,
                    order.getMessageDetail());
            Assert.assertEquals("", TestConstant.MOCK_TITLE, order.getTitle());
        } catch (TCException e) {
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }
        new StrictExpectations() {
            {
                Deencapsulation.invoke(TCPay.class, "doGet",
                        withSubstring(TCUtilPrivate.getkApiSandboxQueryPay().substring(14)),
                        withAny(Map.class));
                result = new TCException(TCErrorCode.e_101001.code(), RESULT_TYPE.APP_INVALID.name(),TCErrorCode.e_101001.error());
            }
        };

        try {
            order = TCPay.startQueryPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(RESULT_TYPE.APP_INVALID.name()));
        }
    }


    private static List<Map<String, Object>> generateMockBills() {
        List<Map<String, Object>> bills = new LinkedList<Map<String, Object>>();
        Map<String, Object> bill = new HashMap<String, Object>();
        Map<String, Object> billAnother = new HashMap<String, Object>();
        buildMockBill(bill);

        buildMockBillAnother(billAnother);

        bills.add(bill);
        bills.add(billAnother);
        return bills;
    }

    private static void buildMockBillAnother(Map<String, Object> billAnother) {
        billAnother.put("id", TestConstant.MOCK_OBJECT_ID);
        billAnother.put("bill_no", TestConstant.MOCK_BILL_NO);
        billAnother.put("total_fee", TestConstant.MOCK_TOTAL_FEE);
        billAnother.put("trade_no", "");
        billAnother.put("channel", TestConstant.MOCK_CHANNEL);
        billAnother.put("sub_channel", TestConstant.MOCK_SUB_CHANNEL);
        billAnother.put("title", TestConstant.MOCK_TITLE);
        billAnother.put("spay_result", TestConstant.MOCK_PAY_RESULT);
        billAnother.put("create_time", TestConstant.MOCK_CREATE_TIME);
        billAnother.put("optional", "");
        billAnother.put("revert_result", true);
        billAnother.put("refund_result", true);
    }

    private static void buildMockBill(Map<String, Object> bill) {
        bill.put("id", TestConstant.MOCK_OBJECT_ID);
        bill.put("bill_no", TestConstant.MOCK_BILL_NO);
        bill.put("total_fee", TestConstant.MOCK_TOTAL_FEE);
        bill.put("trade_no", TestConstant.MOCK_TRADE_NO);
        bill.put("channel", TestConstant.MOCK_CHANNEL);
        bill.put("sub_channel", TestConstant.MOCK_SUB_CHANNEL);
        bill.put("title", TestConstant.MOCK_TITLE);
        bill.put("spay_result", TestConstant.MOCK_PAY_RESULT);
        bill.put("create_time", TestConstant.MOCK_CREATE_TIME);
        bill.put("optional", TestConstant.MOCK_OPTIONAL_JSON_STRING);
        bill.put("message_detail", TestConstant.MOCK_MESSAGE_DETAIL_STRING);
        bill.put("revert_result", true);
        bill.put("refund_result", true);
    }
}
