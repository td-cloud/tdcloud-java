package com.tangdi.tdcloud;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import mockit.Deencapsulation;
import mockit.Expectations;

import org.junit.Assert;

import com.tangdi.tdcloud.TCEumeration.PAY_CHANNEL;
import com.tangdi.tdcloud.TCEumeration.RESULT_TYPE;
import com.tangdi.tdcloud.bean.TCException;
import com.tangdi.tdcloud.bean.TCRefund;


/**
 * 退款单元测试
 * 
 * @author deng_wk
 * @Date 2016-06-29
 */
public class RefundTest {

    static String billNo = TCUtil.generateRandomUUIDPure();
    static String refundNo = new SimpleDateFormat("yyyyMMdd").format(new Date())
            + TCUtil.generateNumberWith3to24digitals();
    static String subject = "javasdk unit test";
    static PAY_CHANNEL channel = PAY_CHANNEL.ALI;
    static PAY_CHANNEL channelWX = PAY_CHANNEL.WX;

    static void testRefund() {

        TCRefund param = new TCRefund();
        initRefundPara(param);

        if (TCCache.isSandbox()) {
            try {
                TCPay.startTCRefund(param);
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
            TCPay.startTCRefund(null);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.REFUND_PARAM_EMPTY));
        }

        try {
            param.setBillNo(null);
            TCPay.startTCRefund(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(), e.getMessage().contains(TestConstant.BILL_NO_EMPTY));
        }
        param.setBillNo(billNo);

        try {
            param.setRefundFee(null);
            TCPay.startTCRefund(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(), e.getMessage()
                    .contains(TestConstant.REFUND_FEE_EMPTY));
        }
        param.setRefundFee(1);

        try {
            param.setRefundFee(0);
            TCPay.startTCRefund(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.REFUND_FEE_INVALID));
        }
        param.setRefundFee(1);

        try {
            param.setRefundNo(null);
            TCPay.startTCRefund(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(), e.getMessage().contains(TestConstant.REFUND_NO_EMPTY));
        }
        param.setRefundNo(refundNo);

        try {
            param.setBillNo(billNo.substring(0, 7));
            TCPay.startTCRefund(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.BILL_NO_FORMAT_INVALID));
        }
        param.setBillNo(billNo);

        try {
            param.setBillNo(billNo + "A");
            TCPay.startTCRefund(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.BILL_NO_FORMAT_INVALID));
        }
        param.setBillNo(billNo);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, -1);
        String date = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
        try {
            param.setRefundNo(date + TCUtil.generateNumberWith3to24digitals());
            TCPay.startTCRefund(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.REFUND_NO_FORMAT_INVALID));
        }
        param.setRefundNo(refundNo);

        try {
            date = new SimpleDateFormat("yyyyMMdd").format(new Date());
            param.setRefundNo(date + "000");
            TCPay.startTCRefund(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.REFUND_NO_FORMAT_INVALID));
        }
        param.setRefundNo(refundNo);

        try {
            param.setRefundNo(date + TestConstant.REFUND_NO_SERIAL_NUMBER_LESSER_THAN_3);
            TCPay.startTCRefund(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.REFUND_NO_FORMAT_INVALID));
        }
        param.setRefundNo(refundNo);

        try {
            param.setRefundNo(date + TestConstant.REFUND_NO_SERIAL_NUMBER_GREATER_THAN_24);
            TCPay.startTCRefund(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.REFUND_NO_FORMAT_INVALID));
        }
        param.setRefundNo(refundNo);

        try {
            param.setRefundNo(date + TestConstant.REFUND_NO_SERIAL_NUMBER_WITH_SPECIAL_CHARACTER);
            TCPay.startTCRefund(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.REFUND_NO_FORMAT_INVALID));
        }
        param.setRefundNo(refundNo);
        //mock网络请求
        mockRefund(param);
    }


    private static void mockRefund(TCRefund refund) {
        final Map<String, Object> returnMapWithUrl = new HashMap<String, Object>();
        returnMapWithUrl.put("id", TestConstant.MOCK_OBJECT_ID);
        returnMapWithUrl.put("result_code", 0);
        returnMapWithUrl.put("result_msg", "OK");
        returnMapWithUrl.put("err_detail", "");
        returnMapWithUrl.put("url", TestConstant.MOCK_ALI_REFUND_URL);

        final Map<String, Object> returnMapWithoutUrl = new HashMap<String, Object>();
        returnMapWithoutUrl.put("id", TestConstant.MOCK_OBJECT_ID);
        returnMapWithoutUrl.put("result_code", 0);
        returnMapWithoutUrl.put("result_msg", "OK");
        returnMapWithoutUrl.put("err_detail", "");

        new Expectations() {
            {
                Deencapsulation.invoke(TCPay.class, "doPost", withSubstring(TCUtilPrivate
                        .getkApiRefund().substring(14)), withAny(Map.class));
                returns(returnMapWithUrl, returnMapWithoutUrl);
                result = new TCException(TCErrorCode.e_999999.code(), RESULT_TYPE.CHANNEL_ERROR.name(),RESULT_TYPE.CHANNEL_ERROR.name());
            }
        };

        try {
            refund.setChannel(PAY_CHANNEL.ALI);
            TCPay.startTCRefund(refund);
            Assert.assertEquals("", TestConstant.MOCK_OBJECT_ID, refund.getId());
        } catch (TCException ex) {
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }
        refund.setAliRefundHtml(null);

        try {
            refund.setChannel(PAY_CHANNEL.WX);
            TCPay.startTCRefund(refund);
            Assert.assertEquals("", TestConstant.MOCK_OBJECT_ID, refund.getId());
            Assert.assertEquals("", null, refund.getAliRefundHtml());
        } catch (TCException ex) {
            ex.printStackTrace();
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }

        try {
            TCPay.startTCRefund(refund);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(RESULT_TYPE.CHANNEL_ERROR.name()));
        }

        try {
            TCPay.startTCRefund(refund);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(RESULT_TYPE.UNIFIED_TRANS_ERROR.name()));
        }
    }

   

    private static void initRefundPara(TCRefund refund) {
        refund.setBillNo(billNo);
        refund.setRefundNo(refundNo);
        refund.setRefundFee(1);
        refund.setTitle(subject);
    }

}
