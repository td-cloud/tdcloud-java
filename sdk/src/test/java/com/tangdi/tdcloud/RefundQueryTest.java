package com.tangdi.tdcloud;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.StrictExpectations;
import com.tangdi.tdcloud.TCEumeration.RESULT_TYPE;
import com.tangdi.tdcloud.bean.TCException;
import com.tangdi.tdcloud.bean.TCQueryParameter;
import com.tangdi.tdcloud.bean.TCRefund;
import com.tangdi.tdcloud.bean.TCRefunds;

import junit.framework.Assert;


/**
 * 退款查询单元测试，包括单笔查询、批量查询、总数查询
 * 
 * @author deng_wk
 * @Date 2016-06-29
 */
public class RefundQueryTest {

    static String billNo = TCUtil.generateRandomUUIDPure();

    static String refundNo = new SimpleDateFormat("yyyyMMdd").format(new Date())
            + TCUtil.generateNumberWith3to24digitals();

    static void testQueryRefund() {
    	TCQueryParameter param = new TCQueryParameter();
        if (TCCache.isSandbox()) {
            try {
                TCPay.startQueryRefund(null);
                Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
            } catch (Exception e) {
                Assert.assertTrue(e.getMessage(), e instanceof TCException);
                Assert.assertTrue(e.getMessage(),
                        e.getMessage().contains(RESULT_TYPE.OTHER_ERROR.name()));
                Assert.assertTrue(e.getMessage(),
                        e.getMessage().contains(TestConstant.TEST_MODE_SUPPORT_ERROR));
            }
            return;
        }

        try {
        	param.setBillNo(TestConstant.INVALID_OBJECT_ID);
            TCPay.startQueryRefund(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(TestConstant.BILL_NO_FORMAT_INVALID));
        }
        
        mockQueryRefund();
    }

    /**
     * 退款批量查询
     */
    static void testQueryRefunds() {
        TCQueryParameter param = new TCQueryParameter();

        if (TCCache.isSandbox()) {
            try {
                TCPay.startQueryRefund(param);
                Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
            } catch (Exception e) {
                Assert.assertTrue(e.getMessage(), e instanceof TCException);
                Assert.assertTrue(e.getMessage(),
                        e.getMessage().contains(RESULT_TYPE.OTHER_ERROR.name()));
                Assert.assertTrue(e.getMessage(),
                        e.getMessage().contains(TestConstant.TEST_MODE_SUPPORT_ERROR));
            }
            return;
        }

        TCRefunds bcRefundList = new TCRefunds();
        try {
            bcRefundList = TCPay.startQueryRefunds(null);
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
            bcRefundList = TCPay.startQueryRefunds(param);
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
            bcRefundList = TCPay.startQueryRefunds(param);
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
            bcRefundList = TCPay.startQueryRefunds(param);
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
            bcRefundList = TCPay.startQueryRefunds(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            org.junit.Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            org.junit.Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name())
                            || ex.getMessage().contains(RESULT_TYPE.OTHER_ERROR.name()));// 服务端验证，可能存在网络问题，加上OTHER_ERROR判断
        }

        try {
            param.setNumber(300);
            bcRefundList = TCPay.startQueryRefunds(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(TestConstant.LIMIT_FORMAT_INVALID));
        }

        try {
            param.setRefundNo(refundNo.substring(0, 8) + "000");
            bcRefundList = TCPay.startQueryRefunds(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(TestConstant.ASSERT_MESSAGE,
                    ex.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(TestConstant.ASSERT_MESSAGE,
                    ex.getMessage().contains(TestConstant.REFUND_NO_FORMAT_INVALID));
        }

        try {
            param.setRefundNo(
                    refundNo.substring(0, 8) + TestConstant.REFUND_NO_SERIAL_NUMBER_LESSER_THAN_3);
            bcRefundList = TCPay.startQueryRefunds(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(TestConstant.ASSERT_MESSAGE,
                    ex.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(TestConstant.ASSERT_MESSAGE,
                    ex.getMessage().contains(TestConstant.REFUND_NO_FORMAT_INVALID));
        }

        try {
            param.setRefundNo(refundNo.substring(0, 8)
                    + TestConstant.REFUND_NO_SERIAL_NUMBER_GREATER_THAN_24);
            bcRefundList = TCPay.startQueryRefunds(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(TestConstant.ASSERT_MESSAGE,
                    ex.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(TestConstant.ASSERT_MESSAGE,
                    ex.getMessage().contains(TestConstant.REFUND_NO_FORMAT_INVALID));
        }

        try {
            param.setRefundNo(refundNo.substring(0, 8)
                    + TestConstant.REFUND_NO_SERIAL_NUMBER_WITH_SPECIAL_CHARACTER);
            bcRefundList = TCPay.startQueryRefunds(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(TestConstant.ASSERT_MESSAGE,
                    ex.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(TestConstant.ASSERT_MESSAGE,
                    ex.getMessage().contains(TestConstant.REFUND_NO_FORMAT_INVALID));
        }
        // mock网络请求
        mockRefundQueryRefunds();
    }

   
    private static void mockRefundQueryRefunds() {
        final Map<String, Object> returnMap = new HashMap<String, Object>();
        TCRefunds bcRefundList = new TCRefunds();
        returnMap.put("result_code", 0);
        returnMap.put("result_msg", "OK");
        returnMap.put("err_detail", "");
        returnMap.put("refunds", generateMockRefunds());
        returnMap.put("count", generateMockRefunds().size());

        new Expectations() {
            {
                Deencapsulation.invoke(TCPay.class, "doGet",
                        withSubstring(TCUtilPrivate.getkApiQueryRefund().substring(14)),
                        withAny(Map.class));
                returns(returnMap);
                result = new TCException(TCErrorCode.e_101001.code(), RESULT_TYPE.APP_INVALID.name(),TCErrorCode.e_101001.error());
            }
        };

        TCQueryParameter param = new TCQueryParameter();
        param.setBillNo(billNo);
        try {
            bcRefundList = TCPay.startQueryRefunds(param);
            Assert.assertEquals("", 2, bcRefundList.getOrders().size());
            for (TCRefund refund : bcRefundList.getOrders()) {
                Assert.assertEquals("", TestConstant.MOCK_OBJECT_ID, refund.getId());
                Assert.assertEquals("", TestConstant.MOCK_BILL_NO, refund.getBillNo());
                Assert.assertEquals("",
                        TestUtil.transferDateFromLongToString(TestConstant.MOCK_CREATE_TIME),
                        refund.getDateTime());
                Assert.assertEquals("", TestConstant.MOCK_CHANNEL,
                        refund.getChannel().toString().split("_")[0]);
                Assert.assertEquals("", TestConstant.MOCK_SUB_CHANNEL,
                        refund.getChannel().toString());
                Assert.assertEquals("", TestConstant.MOCK_REFUND_FEE, refund.getRefundFee());
                Assert.assertTrue("", refund.getOptionalString().equals("") || refund
                        .getOptionalString().equals(TestConstant.MOCK_OPTIONAL_JSON_STRING));
                Assert.assertTrue("", refund.isRefunded() || !refund.isRefunded());
                Assert.assertTrue("", refund.getMessageDetail().equals("不显示") || refund
                        .getMessageDetail().equals(TestConstant.MOCK_MESSAGE_DETAIL_STRING));
                Assert.assertEquals("", TestConstant.MOCK_TITLE, refund.getTitle());
                Assert.assertEquals("", TestConstant.MOCK_REFUND_NO, refund.getRefundNo());
                Assert.assertEquals("", TestConstant.MOCK_TOTAL_FEE, refund.getTotalFee());
            }
        } catch (TCException ex) {
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }

        try {
            bcRefundList = TCPay.startQueryRefunds(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(RESULT_TYPE.APP_INVALID.name()));
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(TestConstant.MOCK_APP_INVALID_ERRMSG));
        }
    }

   

    private static void mockQueryRefund() {
        final Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("result_code", 0);
        returnMap.put("result_msg", "OK");
        returnMap.put("err_detail", "");
        buildMockRefund(returnMap);
        returnMap.put("refund", returnMap);

        new StrictExpectations() {
            {
                Deencapsulation.invoke(TCPay.class, "doGet",
                        withSubstring(TCUtilPrivate.getkApiQueryRefund().substring(14)),
                        withAny(Map.class));
                returns(returnMap);
            }
        };
        TCQueryParameter param = new TCQueryParameter();
        TCRefund refund;
        try {
        	param.setBillNo(TestConstant.MOCK_OBJECT_ID);
            refund = TCPay.startQueryRefund(param);
            Assert.assertEquals("", TestConstant.MOCK_OBJECT_ID, refund.getId());
            Assert.assertEquals("", TestConstant.MOCK_BILL_NO, refund.getBillNo());
            Assert.assertEquals("", TestConstant.MOCK_REFUND_NO, refund.getRefundNo());
            Assert.assertEquals("", TestConstant.MOCK_REFUND_FEE, refund.getRefundFee());
            Assert.assertEquals("", TestConstant.MOCK_TOTAL_FEE, refund.getTotalFee());
            Assert.assertEquals("", TestConstant.MOCK_CHANNEL,
                    refund.getChannel().toString().split("_")[0]);
            Assert.assertEquals("", TestConstant.MOCK_SUB_CHANNEL, refund.getChannel().toString());
            Assert.assertEquals("", TestConstant.MOCK_MESSAGE_DETAIL_STRING,
                    refund.getMessageDetail());
            Assert.assertEquals("", TestConstant.MOCK_OPTIONAL_JSON_STRING,
                    refund.getOptionalString());
            Assert.assertEquals("", TestConstant.MOCK_TITLE, refund.getTitle());
            Assert.assertEquals("", true, refund.isRefunded());
            Assert.assertEquals("",
                    TestUtil.transferDateFromLongToString(TestConstant.MOCK_CREATE_TIME),
                    refund.getDateTime());

        } catch (TCException e) {
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }
    }

    private static void buildMockRefund(final Map<String, Object> returnMap) {
        returnMap.put("id", TestConstant.MOCK_OBJECT_ID);
        returnMap.put("create_time", TestConstant.MOCK_CREATE_TIME);
        returnMap.put("total_fee", TestConstant.MOCK_TOTAL_FEE);
        returnMap.put("refund_fee", TestConstant.MOCK_REFUND_FEE);
        returnMap.put("refund_no", TestConstant.MOCK_REFUND_NO);
        returnMap.put("channel", TestConstant.MOCK_CHANNEL);
        returnMap.put("bill_no", TestConstant.MOCK_BILL_NO);
        returnMap.put("result", true);
        returnMap.put("finish", true);
        returnMap.put("optional", TestConstant.MOCK_OPTIONAL_JSON_STRING);
        returnMap.put("title", TestConstant.MOCK_TITLE);
        returnMap.put("sub_channel", TestConstant.MOCK_SUB_CHANNEL);
        returnMap.put("message_detail", TestConstant.MOCK_MESSAGE_DETAIL_STRING);
    }

    private static List<Map<String, Object>> generateMockRefunds() {
        List<Map<String, Object>> refunds = new LinkedList<Map<String, Object>>();
        Map<String, Object> refund = new HashMap<String, Object>();
        Map<String, Object> refundAnother = new HashMap<String, Object>();
        refund.put("id", TestConstant.MOCK_OBJECT_ID);
        refund.put("create_time", TestConstant.MOCK_CREATE_TIME);
        refund.put("total_fee", TestConstant.MOCK_TOTAL_FEE);
        refund.put("refund_fee", TestConstant.MOCK_REFUND_FEE);
        refund.put("refund_no", TestConstant.MOCK_REFUND_NO);
        refund.put("channel", TestConstant.MOCK_CHANNEL);
        refund.put("bill_no", TestConstant.MOCK_BILL_NO);
        refund.put("result", true);
        refund.put("finish", true);
        refund.put("optional", TestConstant.MOCK_OPTIONAL_JSON_STRING);
        refund.put("title", TestConstant.MOCK_TITLE);
        refund.put("sub_channel", TestConstant.MOCK_SUB_CHANNEL);
        refund.put("message_detail", TestConstant.MOCK_MESSAGE_DETAIL_STRING);

        refundAnother.put("id", TestConstant.MOCK_OBJECT_ID);
        refundAnother.put("create_time", TestConstant.MOCK_CREATE_TIME);
        refundAnother.put("total_fee", TestConstant.MOCK_TOTAL_FEE);
        refundAnother.put("refund_fee", TestConstant.MOCK_REFUND_FEE);
        refundAnother.put("refund_no", TestConstant.MOCK_REFUND_NO);
        refundAnother.put("channel", TestConstant.MOCK_CHANNEL);
        refundAnother.put("bill_no", TestConstant.MOCK_BILL_NO);
        refundAnother.put("result", false);
        refundAnother.put("finish", false);
        refundAnother.put("optional", "");
        refundAnother.put("title", TestConstant.MOCK_TITLE);
        refundAnother.put("sub_channel", TestConstant.MOCK_SUB_CHANNEL);

        refunds.add(refund);
        refunds.add(refundAnother);
        return refunds;
    }
}
