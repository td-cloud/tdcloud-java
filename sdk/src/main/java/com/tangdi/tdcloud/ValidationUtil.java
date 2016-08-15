package com.tangdi.tdcloud;

import com.tangdi.tdcloud.TCEumeration.PAY_CHANNEL;
import com.tangdi.tdcloud.TCEumeration.RESULT_TYPE;
import com.tangdi.tdcloud.bean.TCException;
import com.tangdi.tdcloud.bean.TCOrder;
import com.tangdi.tdcloud.bean.TCQueryParameter;
import com.tangdi.tdcloud.bean.TCRefund;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TdCloud 接口参数验证类
 *
 * @version 1.0.0
 * @author deng_wk
 * @Date 2016-07-02
 */
public class ValidationUtil {

    /**
     * 验证支付下单参数合法
     * @param para
     * @throws TCException
     */
    static void validateTCPay(TCOrder para) throws TCException {

        if (para == null) {
            throw new TCException(TCErrorCode.e_104001.code(), RESULT_TYPE.MISS_PARAM.name(),TCErrorCode.e_104001.error());
        }
        if (para.getChannel() == null) {
            throw new TCException(TCErrorCode.e_104004.code(), RESULT_TYPE.MISS_PARAM.name(),TCErrorCode.e_104004.error());
        } else if (StrUtil.empty(para.getBillNo())) {
            throw new TCException(TCErrorCode.e_104005.code(), RESULT_TYPE.MISS_PARAM.name(),TCErrorCode.e_104005.error());
        } else if (StrUtil.empty(para.getTitle())) {
            throw new TCException(TCErrorCode.e_104007.code(), RESULT_TYPE.MISS_PARAM.name(),TCErrorCode.e_104007.error());
        } else if (StrUtil.empty(para.getTotalFee())) {
            throw new TCException(TCErrorCode.e_104008.code(), RESULT_TYPE.MISS_PARAM.name(),TCErrorCode.e_104008.error());
        } else if (para.getBillTimeout() != null && para.getBillTimeout() == 0) {
            throw new TCException(TCErrorCode.e_104009.code(), RESULT_TYPE.MISS_PARAM.name(),TCErrorCode.e_104009.error());
        } else if (para.getBillNo() != null && !para.getBillNo().matches("[0-9A-Za-z]{8,32}")) {
            throw new TCException(TCErrorCode.e_105008.code(), RESULT_TYPE.PARAM_INVALID.name(),TCErrorCode.e_105008.error());
        } else if (StrUtil.empty(para.getReturnUrl())
                && (para.getChannel().equals(PAY_CHANNEL.ALI_WEB)
                || para.getChannel().equals(PAY_CHANNEL.ALI_QRCODE)
                || para.getChannel().equals(PAY_CHANNEL.UN_WEB)
                || para.getChannel().equals(PAY_CHANNEL.JD_WEB) || para.getChannel()
                .equals(PAY_CHANNEL.JD_WAP))) {
            throw new TCException(TCErrorCode.e_104011.code(), RESULT_TYPE.MISS_PARAM.name(),TCErrorCode.e_104011.error());
        } else if (para.getChannel().equals(PAY_CHANNEL.WX_JSAPI)
                && StrUtil.empty(para.getOpenId())) {
            throw new TCException(TCErrorCode.e_104012.code(), RESULT_TYPE.MISS_PARAM.name(),TCErrorCode.e_104012.error());
        } else if (para.getChannel().equals(PAY_CHANNEL.YEE_WAP) && StrUtil.empty(para.getIdentityId())) {
            throw new TCException(TCErrorCode.e_104013.code(), RESULT_TYPE.MISS_PARAM.name(),TCErrorCode.e_104013.error());
        } else if (para.getChannel().equals(PAY_CHANNEL.YEE_WAP) && !para.getIdentityId().matches("[a-zA-Z0-9]{1,50}")) {
            throw new TCException(TCErrorCode.e_105003.code(), RESULT_TYPE.PARAM_INVALID.name(),TCErrorCode.e_105003.error());
        } else if (para.getChannel().equals(PAY_CHANNEL.ALI_QRCODE)
                && StrUtil.empty(para.getQrPayMode())) {
            throw new TCException(TCErrorCode.e_104014.code(), RESULT_TYPE.MISS_PARAM.name(),TCErrorCode.e_104014.error());
        } else if (para.getChannel().equals(PAY_CHANNEL.YEE_NOBANKCARD)
                && (para.getCardNo() == null || para.getCardPwd() == null || para.getFrqid() == null)) {
            throw new TCException(TCErrorCode.e_104015.code(), RESULT_TYPE.MISS_PARAM.name(),TCErrorCode.e_104015.error());
        } else if (para.getChannel().equals(PAY_CHANNEL.TC_GATEWAY)  && para.getGatewayBank() == null) {
            throw new TCException(TCErrorCode.e_104016.code(), RESULT_TYPE.MISS_PARAM.name(),TCErrorCode.e_104016.error());
        } else if (para.getChannel().equals(PAY_CHANNEL.TC_EXPRESS)  && para.getGatewayBank() == null) {
            throw new TCException(TCErrorCode.e_104016.code(), RESULT_TYPE.MISS_PARAM.name(),TCErrorCode.e_104016.error());
        } else
            try {
                if (para.getTitle().getBytes("GBK").length > 200) {
                    throw new TCException(TCErrorCode.e_105004.code(), RESULT_TYPE.PARAM_INVALID.name(),TCErrorCode.e_105004.error());
                }
            } catch (UnsupportedEncodingException e) {
                if (para.getTitle().length() > 100) {
                    throw new TCException(TCErrorCode.e_105004.code(), RESULT_TYPE.PARAM_INVALID.name(),TCErrorCode.e_105004.error());
                }
            }
    }

    
    /**
     * 验证退款输入参数
     * @param para
     * @throws TCException
     */
    static void validateTCRefund(TCRefund para) throws TCException {
        if (para == null) {
            throw new TCException(TCErrorCode.e_104002.code(), RESULT_TYPE.MISS_PARAM.name(),TCErrorCode.e_104002.error());
        } else if (StrUtil.empty(para.getBillNo())) {
            throw new TCException(TCErrorCode.e_104005.code(), RESULT_TYPE.MISS_PARAM.name(),TCErrorCode.e_104005.error());
        } else if (StrUtil.empty(para.getRefundFee())) {
            throw new TCException(TCErrorCode.e_104008.code(), RESULT_TYPE.MISS_PARAM.name(),TCErrorCode.e_104008.error());
        } else if (para.getRefundFee() <= 0) {
            throw new TCException(TCErrorCode.e_104017.code(), RESULT_TYPE.MISS_PARAM.name(),TCErrorCode.e_104017.error());
        } else if (StrUtil.empty(para.getRefundNo())) {
            throw new TCException(TCErrorCode.e_104018.code(), RESULT_TYPE.MISS_PARAM.name(),TCErrorCode.e_104018.error());
        } else if (!para.getRefundNo().startsWith(new SimpleDateFormat("yyyyMMdd").format(new Date()))) {
            throw new TCException(TCErrorCode.e_105006.code(), RESULT_TYPE.PARAM_INVALID.name(),TCErrorCode.e_105006.error());
        }
        else if (!para.getRefundNo().substring(8, para.getRefundNo().length())
                .matches("[0-9A-Za-z]{3,24}")
                || para.getRefundNo().substring(8, para.getRefundNo().length()).matches("000")) {
        	throw new TCException(TCErrorCode.e_105006.code(), RESULT_TYPE.PARAM_INVALID.name(),TCErrorCode.e_105006.error());
        } else if (!para.getBillNo().matches("[0-9A-Za-z]{8,32}")) {
        	throw new TCException(TCErrorCode.e_104005.code(), RESULT_TYPE.MISS_PARAM.name(),TCErrorCode.e_104005.error());
        }
    }

    /**
     * 验证批量查询参数校验
     * @param para
     * @throws TCException
     */
    static void validateQueryPays(TCQueryParameter para) throws TCException {
        if (!StrUtil.empty(para.getBillNo())
                && !para.getBillNo().matches("[0-9A-Za-z]{8,32}")) {
        	throw new TCException(TCErrorCode.e_105008.code(), RESULT_TYPE.PARAM_INVALID.name(),TCErrorCode.e_105008.error());
        }else if (para.getPages() != null && !para.getPages().toString().matches("^[0-9]*[1-9][0-9]*$")) {
        	throw new TCException(TCErrorCode.e_105011.code(), RESULT_TYPE.PARAM_INVALID.name(),TCErrorCode.e_105011.error());
        }else if (para.getNumber() != null && para.getNumber() >200) {
        	throw new TCException(TCErrorCode.e_105009.code(), RESULT_TYPE.PARAM_INVALID.name(),TCErrorCode.e_105009.error());
        }
    }

    /**
     * 验证批量退款查询输入参数
     * @param para
     * @throws TCException
     */
    static void validateQueryRefunds(TCQueryParameter para) throws TCException {
        if (para == null) {
            throw new TCException(TCErrorCode.e_104003.code(), RESULT_TYPE.MISS_PARAM.name(),TCErrorCode.e_104003.error());
        } else if (!StrUtil.empty(para.getBillNo())
                && !para.getBillNo().matches("[0-9A-Za-z]{8,32}")) {
        	throw new TCException(TCErrorCode.e_105008.code(), RESULT_TYPE.PARAM_INVALID.name(),TCErrorCode.e_105008.error());
        } else if (!StrUtil.empty(para.getRefundNo())
                && (!para.getRefundNo().substring(8, para.getRefundNo().length())
                .matches("[0-9A-Za-z]{3,24}") || para.getRefundNo()
                .substring(8, para.getRefundNo().length()).matches("000"))) {
            throw new TCException(TCErrorCode.e_105006.code(), RESULT_TYPE.PARAM_INVALID.name(),TCErrorCode.e_105006.error());
        } else if (para.getNumber() != null && para.getNumber() >200) {
            throw new TCException(TCErrorCode.e_105009.code(), RESULT_TYPE.PARAM_INVALID.name(),TCErrorCode.e_105009.error());
        }
    }

    /**
     * 验证单笔订单查询输入参数
     * @param para
     * @throws TCException
     */
    static void validateQuerySingle(TCQueryParameter para) throws TCException {
    	if (para == null) {
            throw new TCException(TCErrorCode.e_104003.code(), RESULT_TYPE.MISS_PARAM.name(),TCErrorCode.e_104003.error());
        }else if (StrUtil.empty(para.getBillNo()) && StrUtil.empty(para.getId())) {
            throw new TCException(TCErrorCode.e_104006.code(), RESULT_TYPE.MISS_PARAM.name(),TCErrorCode.e_104006.error());
        }else if (!StrUtil.empty(para.getId()) && !para.getId().matches("[0-9a-zA-Z\\-]+")) {
        	throw new TCException(TCErrorCode.e_105012.code(), RESULT_TYPE.PARAM_INVALID.name(),TCErrorCode.e_105012.error());
        }else if (!StrUtil.empty(para.getBillNo()) && !para.getBillNo().matches("[0-9A-Za-z]{8,32}")) {
        	throw new TCException(TCErrorCode.e_105008.code(), RESULT_TYPE.PARAM_INVALID.name(),TCErrorCode.e_105008.error());
        }
    }
    
    /**
     * 验证单笔退款查询输入参数
     * @param para
     * @throws TCException
     */
    static void validateRefundQuerySingle(TCQueryParameter para) throws TCException {
    	if (para == null) {
            throw new TCException(TCErrorCode.e_104003.code(), RESULT_TYPE.MISS_PARAM.name(),TCErrorCode.e_104003.error());
        }else if (StrUtil.empty(para.getRefundNo()) && StrUtil.empty(para.getId())) {
            throw new TCException(TCErrorCode.e_104019.code(), RESULT_TYPE.MISS_PARAM.name(),TCErrorCode.e_104019.error());
        }else if (!StrUtil.empty(para.getId()) && !para.getId().matches("[0-9a-zA-Z\\-]+")) {
        	throw new TCException(TCErrorCode.e_105012.code(), RESULT_TYPE.PARAM_INVALID.name(),TCErrorCode.e_105012.error());
        }else if (!StrUtil.empty(para.getRefundNo())  && (!para.getRefundNo().substring(8, para.getRefundNo().length())
                .matches("[0-9A-Za-z]{3,24}") || para.getRefundNo()
                .substring(8, para.getRefundNo().length()).matches("000"))) {
        	throw new TCException(TCErrorCode.e_105006.code(), RESULT_TYPE.PARAM_INVALID.name(),TCErrorCode.e_105006.error());
        }
	}
}
