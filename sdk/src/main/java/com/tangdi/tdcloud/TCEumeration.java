package com.tangdi.tdcloud;

/**
 * TdClod JAVA　SDK枚举类
 * 
 * @author deng_wk
 * @Date 2016-06-29
 */
public class TCEumeration {

    public enum RESULT_TYPE {
    	OK,
    	APP_INVALID,
    	PAY_FACTOR_NOT_SET,
    	CHANNEL_INVALID,
    	MISS_PARAM,
    	PARAM_INVALID,
    	CERT_FILE_ERROR,
    	CHANNEL_ERROR,
    	UNIFIED_TRANS_ERROR,
    	OTHER_ERROR,
    	RUNTIME_ERROR,
    	COMMUNICATION_ERROR
    }

    public enum PAY_CHANNEL {
        WX,
        WX_APP,
        WX_NATIVE,
        WX_JSAPI,
        WX_SCAN,
        ALI,
        ALI_APP,
        ALI_WEB,
        ALI_WAP,
        ALI_QRCODE,
        ALI_SCAN,
        ALI_OFFLINE_QRCODE,
        UN,
        UN_APP,
        UN_WEB,
        UN_WAP,
        YEE,
        YEE_WEB,
        YEE_WAP,
        YEE_NOBANKCARD,
        JD,
        JD_WEB,
        JD_WAP,
        KUAIQIAN,
        KUAIQIAN_WAP,
        KUAIQIAN_WEB,
        BD,
        BD_WEB,
        BD_WAP,
        BD_APP,
        PAYPAL,
        PAYPAL_SANDBOX,
        PAYPAL_LIVE,
        PAYPAL_PAYPAL,
        PAYPAL_CREDITCARD,
        PAYPAL_SAVED_CREDITCARD,
        TC_GATEWAY,
        TC_EXPRESS,
        CP_WEB
    }

    public enum TRANSFER_CHANNEL {
        ALI_TRANSFER,
        WX_REDPACK,
        WX_TRANSFER
    }

    public enum QR_PAY_MODE {
        MODE_BRIEF_FRONT,
        MODE_FRONT,
        MODE_MINI_FRONT
    }

    public enum PAYPAL_CURRENCY {
        AUD,
        BRL,
        CAD,
        CZK,
        DKK,
        EUR,
        HKD,
        HUF,
        ILS,
        JPY,
        MYR,
        MXN,
        TWD,
        NZD,
        NOK,
        PHP,
        PLN,
        GBP,
        SGD,
        SEK,
        CHF,
        THB,
        TRY,
        USD
    }

    public enum GATEWAY_BANK {
        CMB,
        ICBC,
        CCB,
        BOC,
        ABC,
        BOCM,
        SPDB,
        GDB,
        CITIC,
        CEB,
        CIB,
        SDB,
        CMBC

    }

    public enum CARD_TYPE {
        visa,
        mastercard,
        discover,
        amex
    }
    
    public enum API_NAME{
    	BILL,
    	REFUND,
    	QUERYPAY,
    	QUERYPAYS,
    	QUERYREFUND,
    	QUERYREFUNDS,
    	
    }
}
