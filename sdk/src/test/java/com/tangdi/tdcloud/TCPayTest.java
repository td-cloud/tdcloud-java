package com.tangdi.tdcloud;

/**
 * JAVA SDK 单元测试主类
 * 
 * @author deng_wk
 * @Date 2016-06-29
 */
//@RunWith(JMockit.class)
public class TCPayTest {


    //@Before
    public void setUp() throws Exception {
        TdCloud.registerApp(TestConstant.KTestAppID, null, TestConstant.kTestAppSecret,
                TestConstant.kTestMasterSecret);
    }

   // @Test
    public void javaSDKTest() {

        /**
         * 测试LIVE模式
         */
        PayTest.testPay();
        BillQueryTest.testQueryPay();
        BillQueryTest.testQueryPays();
        RefundTest.testRefund();
        RefundQueryTest.testQueryRefund();
        RefundQueryTest.testQueryRefunds();

        /**
         * 测试SANDBOX模式
         */
        TdCloud.registerApp(TestConstant.KTestAppID, TestConstant.kTestTestSecret, null, null);
        TCCache.setSandbox(true);
        PayTest.testPay();
        BillQueryTest.testQueryPay();
        BillQueryTest.testQueryPays();
        RefundTest.testRefund();
        RefundQueryTest.testQueryRefund();
        RefundQueryTest.testQueryRefunds();

    }
}
