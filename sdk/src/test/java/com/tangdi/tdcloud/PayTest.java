package com.tangdi.tdcloud;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import mockit.Deencapsulation;
import mockit.Expectations;

import org.junit.Assert;

import com.tangdi.tdcloud.TCEumeration.PAY_CHANNEL;
import com.tangdi.tdcloud.TCEumeration.QR_PAY_MODE;
import com.tangdi.tdcloud.TCEumeration.RESULT_TYPE;
import com.tangdi.tdcloud.bean.TCException;
import com.tangdi.tdcloud.bean.TCOrder;


/**
 * TdCloud 支付单元测试
 * 
 * @author deng_wk
 * @Date 2016-06-29
 */
public class PayTest {

    static String billNo = TCUtil.generateRandomUUIDPure();
    static String refundNo = new SimpleDateFormat("yyyyMMdd").format(new Date())
            + TCUtil.generateNumberWith3to24digitals();
    static String subject = "javasdk unit test";
    static String returnUrl = "http://return.url.beecloud.cn";
    static PAY_CHANNEL channel = PAY_CHANNEL.ALI_WEB;
    static String cardNo = TestConstant.YEE_NOBANKCARD_NO;
    static String cardPwd = TestConstant.YEE_NOBANKCARD_PWD;
    static String frqid = TestConstant.YEE_NOBANKCARD_FRQID;
    static String openId = TestConstant.WXJSAPI_OPEN_ID;
    static QR_PAY_MODE qrPayMode = QR_PAY_MODE.MODE_BRIEF_FRONT;
    static String identityId = TestConstant.YEE_WAP_IDENTITY_ID;

    static void testPay() {

        TCOrder param = new TCOrder();
        initPayParam(param);

        /*-------------------------start pay param test------------------------*/
        try {
            TCPay.startTCPay(null);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
        	System.out.println("---------》"+e.getMessage());
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.PAY_PARAM_EMPTY));
        }

        try {
            param.setChannel(null);
            TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(), e.getMessage().contains(TestConstant.CHANNEL_EMPTY));
        }
        param.setChannel(channel);

        try {
            param.setTotalFee(null);
            TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.TOTAL_FEE_EMPTY));
        }
        param.setTotalFee(1);

        try {
            param.setBillNo(null);
            TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(), e.getMessage().contains(TestConstant.BILL_NO_EMPTY));
        }
        param.setBillNo(billNo);
        //
        try {
            param.setTitle(null);
            TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(), e.getMessage().contains(TestConstant.TITLE_EMPTY));
        }
        param.setTitle(subject);

        try {
            param.setTitle(TestConstant.TITLE_WITH_CHARACTER＿GREATER_THAN_32);
            TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.TITLE_FORMAT_INVALID));
        }
        param.setTitle(subject);

        try {
            param.setTitle(TestConstant.MOCK_TITLE_16_CHINESE_CHARACTER);
            TCPay.startTCPay(param);
        } catch (TCException ex) {
            Assert.assertTrue("", !ex.getMessage().contains(TestConstant.TITLE_FORMAT_INVALID));
        }
        param.setTitle(subject);

        try {
            param.setBillNo(TestConstant.BILL_NO_WITH_SPECIAL_CHARACTER);
            TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.BILL_NO_FORMAT_INVALID));
        }
        param.setBillNo(billNo);
        //

        try {
            param.setBillNo(billNo.substring(0, 7));
            TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.BILL_NO_FORMAT_INVALID));
        }
        param.setBillNo(billNo);
        //
        try {
            param.setBillNo(billNo + "A");
            TCPay.startTCPay(param);
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
            param.setBillTimeout(0);
            TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.BILL_TIME_OUT_ZERO));
        }
        param.setBillTimeout(TestConstant.billTimeOut);
        /*-------------------------end pay param test------------------------*/

        /*------------------------------start return url empty test-------------------*/
        String returnUrl = param.getReturnUrl();
        try {
            param.setReturnUrl(null);
            param.setChannel(PAY_CHANNEL.ALI_WEB);
            TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.RETURN_URL_EMPTY));
        }

        try {
            param.setReturnUrl(null);
            param.setChannel(PAY_CHANNEL.ALI_QRCODE);
            TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.RETURN_URL_EMPTY));
        }

        try {
            param.setReturnUrl(null);
            param.setChannel(PAY_CHANNEL.UN_WEB);
            TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.RETURN_URL_EMPTY));
        }

        try {
            param.setReturnUrl(null);
            param.setChannel(PAY_CHANNEL.JD_WAP);
            TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.RETURN_URL_EMPTY));
        }

        try {
            param.setReturnUrl(null);
            param.setChannel(PAY_CHANNEL.JD_WEB);
            TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.RETURN_URL_EMPTY));
        }
        param.setReturnUrl(returnUrl);
        /*----------------------------------end return url empty test-------------------*/

        /*----------------------------------start mandatory param for each channe------------------*/
        try {
            param.setOpenId(null);
            param.setChannel(PAY_CHANNEL.WX_JSAPI);
            TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(), e.getMessage().contains(TestConstant.OPENID_EMPTY));
        }
        param.setOpenId(openId);

        try {
            param.setIdentityId(null);
            param.setChannel(PAY_CHANNEL.YEE_WAP);
            TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(), e.getMessage().contains(TestConstant.IDENTITY_ID_EMPTY));
        }

        try {
            param.setIdentityId(TestConstant.YEE_WAP_IDENTITY_ID_MORE_THAN_50);
            param.setChannel(PAY_CHANNEL.YEE_WAP);
            TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(), e.getMessage().contains(TestConstant.IDENTITY_ID_INVALID));
        }

        try {
            param.setIdentityId(TestConstant.YEE_WAP_IDENTITY_ID_WITH_SPECIAL_CHARACTER);
            param.setChannel(PAY_CHANNEL.YEE_WAP);
            TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(), e.getMessage().contains(TestConstant.IDENTITY_ID_INVALID));
        }
        param.setIdentityId(TestConstant.YEE_WAP_IDENTITY_ID);


        try {
            param.setChannel(PAY_CHANNEL.ALI_QRCODE);
            param.setQrPayMode(null);
            TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.QR_PAY_MODE_EMPTY));
        }
        param.setQrPayMode(qrPayMode);
        //
        try {
            param.setCardNo(null);
            param.setChannel(PAY_CHANNEL.YEE_NOBANKCARD);
            TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.YEE_NOBANCARD_FACTOR_EMPTY));
        }
        param.setCardNo(cardNo);

        try {
            param.setCardPwd(null);
            param.setChannel(PAY_CHANNEL.YEE_NOBANKCARD);
            TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.YEE_NOBANCARD_FACTOR_EMPTY));
        }
        param.setCardPwd(cardPwd);

        String frqid = param.getFrqid();
        try {
            param.setFrqid(null);
            param.setChannel(PAY_CHANNEL.YEE_NOBANKCARD);
            TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.YEE_NOBANCARD_FACTOR_EMPTY));
        }
        param.setFrqid(frqid);

        TCEumeration.GATEWAY_BANK gatewayBank = param.getGatewayBank();
        try {
            param.setGatewayBank(null);
            param.setChannel(PAY_CHANNEL.TC_GATEWAY);
            TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), e instanceof TCException);
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(RESULT_TYPE.PARAM_INVALID.name()));
            Assert.assertTrue(e.getMessage(),
                    e.getMessage().contains(TestConstant.GATEWAY_BANK_EMPTY));
        }
        param.setGatewayBank(gatewayBank);


        if (TCCache.isSandbox()) {

            try {
                param.setChannel(PAY_CHANNEL.WX_JSAPI);
                TCPay.startTCPay(param);
                Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
            } catch (Exception e) {
                Assert.assertTrue(e.getMessage(), e instanceof TCException);
                Assert.assertTrue(e.getMessage(),
                        e.getMessage().contains(RESULT_TYPE.OTHER_ERROR.name()));
                Assert.assertTrue(e.getMessage(),
                        e.getMessage().contains(TestConstant.TEST_MODE_SUPPORT_ERROR));
            }

            mockSandboxWxNativePay(param);

            mockSandboxUrlAndHtmlPay(param);

            mockSandboxHtmlPay(param);

            mockSandboxYeeNoBankCard(param);

            return;
        }
        /*--------------------------------------end mandatory param test---------------------*/

        /*--------------------------------------start mock network request and reponse handle-------------*/
        mockWxNativePay(param);

        mockUrlAndHtmlPay(param);

        mockHtmlPay(param);

        mockWxJsapi(param);
        /*--------------------------------------end mock network request and reponse handle-------------*/
    }

    private static void mockSandboxYeeNoBankCard(TCOrder param) {
        final Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("result_code", 0);
        returnMap.put("result_msg", "OK");
        returnMap.put("err_detail", "");

        new Expectations() {
            {
                Deencapsulation.invoke(TCPay.class, "doPost",
                        withSubstring(TCUtilPrivate.getkSandboxApiPay().substring(14)),
                        withAny(Map.class));
                returns(returnMap);

                Deencapsulation.invoke(TCPay.class, "doGet",
                        withSubstring(TCUtilPrivate.getkApiSandboxNotify().substring(14)),
                        withAny(Map.class));
                returns(returnMap);
            }
        };

        param.setChannel(PAY_CHANNEL.YEE_NOBANKCARD);
        param.setCardNo(cardNo);
        param.setFrqid(frqid);
        param.setCardPwd(cardPwd);
        try {
            TCPay.startTCPay(param);
        } catch (TCException e) {
            e.printStackTrace();
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }
    }

    private static void mockSandboxHtmlPay(TCOrder param) {
        final Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("id", TestConstant.MOCK_OBJECT_ID);
        returnMap.put("url", TestConstant.MOCK_SANDBOX_PAY_URL);

        new Expectations() {
            {
                Deencapsulation.invoke(TCPay.class, "doPost",
                        withSubstring(TCUtilPrivate.getkSandboxApiPay().substring(14)),
                        withAny(Map.class));
                returns(returnMap, returnMap, returnMap, returnMap, returnMap);
                times = 5;
                result = new TCException(TCErrorCode.e_101001.code(), RESULT_TYPE.APP_INVALID.name(),TCErrorCode.e_101001.error());
            }
        };
        TCOrder order;
        try {
            param.setChannel(PAY_CHANNEL.UN_WEB);
            order = TCPay.startTCPay(param);
            Assert.assertEquals("",
                    TestUtil.generateSandboxHtmlWithUrl(TestConstant.MOCK_SANDBOX_PAY_URL),
                    order.getHtml());
            Assert.assertEquals("", TestConstant.MOCK_OBJECT_ID, order.getId());
        } catch (TCException e) {
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }

        try {
            param.setChannel(PAY_CHANNEL.JD_WAP);
            order = TCPay.startTCPay(param);
            Assert.assertEquals("",
                    TestUtil.generateSandboxHtmlWithUrl(TestConstant.MOCK_SANDBOX_PAY_URL),
                    order.getHtml());
            Assert.assertEquals("", TestConstant.MOCK_OBJECT_ID, order.getId());
        } catch (TCException e) {
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }

        try {
            param.setChannel(PAY_CHANNEL.JD_WEB);
            order = TCPay.startTCPay(param);
            Assert.assertEquals("",
                    TestUtil.generateSandboxHtmlWithUrl(TestConstant.MOCK_SANDBOX_PAY_URL),
                    order.getHtml());
            Assert.assertEquals("", TestConstant.MOCK_OBJECT_ID, order.getId());
        } catch (TCException e) {
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }

        try {
            param.setChannel(PAY_CHANNEL.KUAIQIAN_WEB);
            order = TCPay.startTCPay(param);
            Assert.assertEquals("",
                    TestUtil.generateSandboxHtmlWithUrl(TestConstant.MOCK_SANDBOX_PAY_URL),
                    order.getHtml());
            Assert.assertEquals("", TestConstant.MOCK_OBJECT_ID, order.getId());
        } catch (TCException e) {
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }

        try {
            param.setChannel(PAY_CHANNEL.KUAIQIAN_WAP);
            order = TCPay.startTCPay(param);
            Assert.assertEquals("",
                    TestUtil.generateSandboxHtmlWithUrl(TestConstant.MOCK_SANDBOX_PAY_URL),
                    order.getHtml());
            Assert.assertEquals("", TestConstant.MOCK_OBJECT_ID, order.getId());
        } catch (TCException e) {
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }

        try {
            order = TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(RESULT_TYPE.APP_INVALID.name()));
        }
    }

    private static void mockSandboxUrlAndHtmlPay(TCOrder param) {
        final Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("id", TestConstant.MOCK_OBJECT_ID);
        returnMap.put("url", TestConstant.MOCK_SANDBOX_PAY_URL);

        new Expectations() {
            {
                Deencapsulation.invoke(TCPay.class, "doPost",
                        withSubstring(TCUtilPrivate.getkSandboxApiPay().substring(14)),
                        withAny(Map.class));
                returns(returnMap, returnMap, returnMap);
                result = new TCException(TCErrorCode.e_999999.code(), RESULT_TYPE.RUNTIME_ERROR.name(),RESULT_TYPE.RUNTIME_ERROR.name());
            }
        };

        TCOrder order;
        param.setChannel(PAY_CHANNEL.ALI_WEB);
        try {
            order = TCPay.startTCPay(param);
            Assert.assertEquals("", TestConstant.MOCK_SANDBOX_PAY_URL, order.getUrl());
            Assert.assertEquals("", TestUtil.generateSandboxHtmlWithUrl(order.getUrl()),
                    order.getHtml());
        } catch (TCException e) {
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }

        param.setChannel(PAY_CHANNEL.ALI_WAP);
        try {
            order = TCPay.startTCPay(param);
            Assert.assertEquals("", TestConstant.MOCK_SANDBOX_PAY_URL, order.getUrl());
            Assert.assertEquals("", TestUtil.generateSandboxHtmlWithUrl(order.getUrl()),
                    order.getHtml());
        } catch (TCException e) {
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }

        param.setChannel(PAY_CHANNEL.ALI_QRCODE);
        try {
            order = TCPay.startTCPay(param);
            Assert.assertEquals("", TestConstant.MOCK_SANDBOX_PAY_URL, order.getUrl());
            Assert.assertEquals("", TestUtil.generateSandboxHtmlWithUrl(order.getUrl()),
                    order.getHtml());
        } catch (TCException e) {
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }

        try {
            order = TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(RESULT_TYPE.RUNTIME_ERROR.name()));
        }
    }

    private static void mockSandboxWxNativePay(TCOrder param) {
        final Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("id", TestConstant.MOCK_OBJECT_ID);
        returnMap.put("url", TestConstant.MOCK_SANDBOX_PAY_URL);
        returnMap.put("result_code", 0);
        returnMap.put("result_msg", "OK");
        returnMap.put("err_detail", "");

        new Expectations() {
            {
                Deencapsulation.invoke(TCPay.class, "doPost",
                        withSubstring(TCUtilPrivate.getkSandboxApiPay().substring(14)),
                        withAny(Map.class));
                returns(returnMap);
            }
        };
        TCOrder order;
        param.setChannel(PAY_CHANNEL.WX_NATIVE);
        try {
            order = TCPay.startTCPay(param);
            Assert.assertEquals("", TestConstant.MOCK_SANDBOX_PAY_URL, order.getCodeUrl());
            Assert.assertEquals("", TestConstant.MOCK_OBJECT_ID, order.getId());
        } catch (TCException e) {
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }
    }

    private static void mockWxNativePay(TCOrder param) {
        final Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("id", TestConstant.MOCK_OBJECT_ID);
        returnMap.put("code_url", TestConstant.MOCK_CODE_URL);
        returnMap.put("result_code", 0);
        returnMap.put("result_msg", "OK");
        returnMap.put("err_detail", "");

        new Expectations(TCPay.class) {
            {
                Deencapsulation.invoke(TCPay.class, "doPost",
                        withSubstring(TCUtilPrivate.getkApiPay().substring(14)),
                        withAny(Map.class));
                returns(returnMap);
                result = new TCException(TCErrorCode.e_999999.code(), RESULT_TYPE.PAY_FACTOR_NOT_SET.name(),RESULT_TYPE.PAY_FACTOR_NOT_SET.name());
            }
        };
        TCOrder order;
        param.setChannel(PAY_CHANNEL.WX_NATIVE);
        try {
            order = TCPay.startTCPay(param);
            Assert.assertEquals("", TestConstant.MOCK_CODE_URL, order.getCodeUrl());
            Assert.assertEquals("", TestConstant.MOCK_OBJECT_ID, order.getId());
        } catch (TCException e) {
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }

        try {
            order = TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(RESULT_TYPE.PAY_FACTOR_NOT_SET.name()));
        }
    }

    private static void mockUrlAndHtmlPay(TCOrder param) {
        final Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("id", TestConstant.MOCK_OBJECT_ID);
        returnMap.put("url", TestConstant.MOCK_PAY_URL);
        returnMap.put("html", TestConstant.MOCK_PAY_HTML);

        new Expectations() {
            {
                Deencapsulation.invoke(TCPay.class, "doPost",
                        withSubstring(TCUtilPrivate.getkApiPay().substring(14)),
                        withAny(Map.class));
                returns(returnMap, returnMap, returnMap);
                result = new TCException(TCErrorCode.e_999999.code(), RESULT_TYPE.RUNTIME_ERROR.name(),RESULT_TYPE.RUNTIME_ERROR.name());
            }
        };

        TCOrder order;
        param.setChannel(PAY_CHANNEL.ALI_WEB);
        try {
            order = TCPay.startTCPay(param);
            Assert.assertEquals("", TestConstant.MOCK_PAY_URL, order.getUrl());
            Assert.assertEquals("", TestConstant.MOCK_PAY_HTML, order.getHtml());
        } catch (TCException e) {
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }

        param.setChannel(PAY_CHANNEL.ALI_WAP);
        try {
            order = TCPay.startTCPay(param);
            Assert.assertEquals("", TestConstant.MOCK_PAY_URL, order.getUrl());
            Assert.assertEquals("", TestConstant.MOCK_PAY_HTML, order.getHtml());
        } catch (TCException e) {
            e.printStackTrace();
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }

        param.setChannel(PAY_CHANNEL.ALI_QRCODE);
        try {
            order = TCPay.startTCPay(param);
            Assert.assertEquals("", TestConstant.MOCK_PAY_URL, order.getUrl());
            Assert.assertEquals("", TestConstant.MOCK_PAY_HTML, order.getHtml());
        } catch (TCException e) {
            e.printStackTrace();
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }

        try {
            order = TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(RESULT_TYPE.RUNTIME_ERROR.name()));
        }
    }

    private static void mockHtmlPay(TCOrder param) {
        final Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("id", TestConstant.MOCK_OBJECT_ID);
        returnMap.put("html", TestConstant.MOCK_PAY_HTML);

        new Expectations() {
            {
                Deencapsulation.invoke(TCPay.class, "doPost",
                        withSubstring(TCUtilPrivate.getkApiPay().substring(14)),
                        withAny(Map.class));
                returns(returnMap, returnMap, returnMap, returnMap, returnMap);
                times = 5;
                result = new TCException(TCErrorCode.e_999999.code(), RESULT_TYPE.APP_INVALID.name(),RESULT_TYPE.APP_INVALID.name());
            }
        };
        TCOrder order;
        try {
            param.setChannel(PAY_CHANNEL.UN_WEB);
            order = TCPay.startTCPay(param);
            Assert.assertEquals("", TestConstant.MOCK_PAY_HTML, order.getHtml());
            Assert.assertEquals("", TestConstant.MOCK_OBJECT_ID, order.getId());
        } catch (TCException e) {
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }

        try {
            param.setChannel(PAY_CHANNEL.JD_WAP);
            order = TCPay.startTCPay(param);
            Assert.assertEquals("", TestConstant.MOCK_PAY_HTML, order.getHtml());
            Assert.assertEquals("", TestConstant.MOCK_OBJECT_ID, order.getId());
        } catch (TCException e) {
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }

        try {
            param.setChannel(PAY_CHANNEL.JD_WEB);
            order = TCPay.startTCPay(param);
            Assert.assertEquals("", TestConstant.MOCK_PAY_HTML, order.getHtml());
            Assert.assertEquals("", TestConstant.MOCK_OBJECT_ID, order.getId());
        } catch (TCException e) {
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }

        try {
            param.setChannel(PAY_CHANNEL.KUAIQIAN_WEB);
            order = TCPay.startTCPay(param);
            Assert.assertEquals("", TestConstant.MOCK_PAY_HTML, order.getHtml());
            Assert.assertEquals("", TestConstant.MOCK_OBJECT_ID, order.getId());
        } catch (TCException e) {
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }

        try {
            param.setChannel(PAY_CHANNEL.KUAIQIAN_WAP);
            order = TCPay.startTCPay(param);
            Assert.assertEquals("", TestConstant.MOCK_PAY_HTML, order.getHtml());
            Assert.assertEquals("", TestConstant.MOCK_OBJECT_ID, order.getId());
        } catch (TCException e) {
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }

        try {
            order = TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(RESULT_TYPE.APP_INVALID.name()));
        }
    }

    private static void mockWxJsapi(TCOrder param) {
        final Map<String, Object> returnMap = new HashMap<String, Object>();

        returnMap.put("id", TestConstant.MOCK_OBJECT_ID);
        returnMap.put("result_code", 0);
        returnMap.put("result_msg", "OK");
        returnMap.put("err_detail", "");
        returnMap.put("app_id", TestConstant.MOCK_WXJSAPI_APPID);
        returnMap.put("timestamp", TestConstant.MOCK_WXJSAP_TIMESTAMP);
        returnMap.put("package", TestConstant.MOCK_WXJSAPI_PACKAGE);
        returnMap.put("nonce_str", TestConstant.MOCK_WXJSAPI_NONCESTR);
        returnMap.put("pay_sign", TestConstant.MOCK_WXJSAPI_PAYSIGN);
        returnMap.put("sign_type", TestConstant.MOCK_WXJSAP_SIGNTYPE);

        new Expectations() {
            {
                Deencapsulation.invoke(TCPay.class, "doPost",
                        withSubstring(TCUtilPrivate.getkApiPay().substring(14)),
                        withAny(Map.class));
                returns(returnMap);
                result = new TCException(TCErrorCode.e_999999.code(), RESULT_TYPE.CHANNEL_INVALID.name(),RESULT_TYPE.CHANNEL_INVALID.name());
            }
        };
        TCOrder order;
        param.setChannel(PAY_CHANNEL.WX_JSAPI);
        try {
            order = TCPay.startTCPay(param);
            Assert.assertEquals("", TestConstant.MOCK_WXJSAPI_APPID,
                    order.getWxJSAPIMap().get("appId"));
            Assert.assertEquals("", TestConstant.MOCK_WXJSAPI_NONCESTR,
                    order.getWxJSAPIMap().get("nonceStr"));
            Assert.assertEquals("", TestConstant.MOCK_WXJSAP_TIMESTAMP,
                    order.getWxJSAPIMap().get("timeStamp"));
            Assert.assertEquals("", TestConstant.MOCK_WXJSAPI_PAYSIGN,
                    order.getWxJSAPIMap().get("paySign"));
            Assert.assertEquals("", TestConstant.MOCK_WXJSAP_SIGNTYPE,
                    order.getWxJSAPIMap().get("signType"));
            Assert.assertEquals("", TestConstant.MOCK_WXJSAPI_PACKAGE,
                    order.getWxJSAPIMap().get("package"));
            Assert.assertEquals("", TestConstant.MOCK_OBJECT_ID, order.getId());
        } catch (TCException e) {
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_THROWN);
        }

        try {
            order = TCPay.startTCPay(param);
            Assert.fail(TestConstant.ASSERT_MESSAGE_TCEXCEPTION_NOT_THROWN);
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage(), ex instanceof TCException);
            Assert.assertTrue(ex.getMessage(),
                    ex.getMessage().contains(RESULT_TYPE.CHANNEL_INVALID.name()));
        }
    }

    private static void initPayParam(TCOrder param) {
        param.setBillNo(billNo);
        param.setTitle(subject);
        param.setReturnUrl(returnUrl);
        param.setChannel(channel);
        param.setOpenId(openId);
        param.setQrPayMode(qrPayMode);
        param.setIdentityId(identityId);
        param.setGatewayBank(TCEumeration.GATEWAY_BANK.ABC);
    }
}
