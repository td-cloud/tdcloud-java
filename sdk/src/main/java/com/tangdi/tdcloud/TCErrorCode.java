package com.tangdi.tdcloud; 
/** 
* @author deng_wk
* @version 1.0.0 
* 交易网关错误编码<br/>
* 创建时间：2016年6月6日 上午9:19:15
*/
public enum TCErrorCode {
	
	/**
	 * 网关相关交易错误码定义请需按照下述格式进行定义
	 * 
	    错误码                        错误类型                                                         明细（举列）
	000000        OK
	101xxx       APP_INVALID             根据app_id找不到对应的APP或者app_sign不正确 
	102xxx       PAY_FACTOR_NOT_SET      支付要素在后台没有设置 (如一些APPID的开关)
	103xxx       CHANNEL_INVALID         channel 参数不合法 
	104xxx       MISS_PARAM              缺少必填参数 
	105xxx       PARAM_INVALID           参数不合法 
	106xxx       CERT_FILE_ERROR         证书错误 
	107xxx       CHANNEL_ERROR           渠道内部错误
	108xxx       UNIFIED_TRANS_ERROR     业务错误
	109xxx       COMMUNICATION_ERROR     通讯错误         
	120xxx       OTHER_ERROR             其他错误
	90xxxx       RUNTIME_ERROR           运行时错误（比如系统错误）
	*/
	
	e_000000("000000", "成功"),
	e_101001("101001", "app_id不存在或无效，请检查参数！"),
	e_101002("101002", "app_id关联的商户不可用，系统拒绝交易！"),
	e_101003("101003", "系统验证签名不通过，请参考TdCloud接口规范文档上送参数！"),
	
	e_102001("102001", "app_id状态不正常，请前往TdCloud设置页面进行正确配置！"),
	
    e_104001("104001", "支付参数不能为空！"),
    e_104002("104002", "退款参数不能为空！"),
    e_104003("104003", "查询参数不能为空！"),
	e_104004("104004", "channel 不能为空！"),
	e_104005("104005", "billNo 不能为空！"),
	e_104006("104006", "查询参数id,billNo不能全为空！"),
	e_104007("104007", "title 不能为空！"),
	e_104008("104008", "totalFee 不能为空！"),
	e_104009("104009", "billTimeout不能为0！"),
	
	e_104011("104011", "returnUrl 不能为空！"),
	e_104012("104012", "openid 不能为空！"),
	
	e_104013("104013", "identityId 不能为空！"),
	e_104014("104014", "qrPayMode 不能为空！"),
	e_104015("104015", "cardNo, cardPwd, frqid 不能为空！"),
	e_104016("104016", "bank 不能为空！"),
	e_104017("104017", "refundFee 必须大于零！"),
	e_104018("104018", "refundNo 不能为空！"),
	e_104019("104019", "查询参数id,refundNo不能全为空！"),
	
	
	e_105001("105001", "测试模式仅支持国内支付(WX_JSAPI暂不支持)、订单查询、单笔订单查询"),
	e_105002("105002", "查询参数id,billNo不能全为空！"),
	e_105003("105003", "identityId 是一个长度不超过50个字符的数字字母字符串！"),
	e_105004("105004", "title 是一个长度不超过100字节的字符串！"),
	e_105005("105005", "退款只支持WX, UN, ALI！"),
	e_105006("105006", "refundNo 是格式为当前日期加3-24位数字字母（不能为000）流水号的字符串！"),
	e_105008("105008", "billNo 是一个长度介于8至32字符的数字字母字符串！"),
	e_105009("105009", "number 的最大为200！"),
	e_105011("105011", "pages 需为正整数！"),
	e_105012("105012", "id 只能包含数字、字母或者-"),
	
	
	 
	e_120001("120001", "网络错误"),
	e_120002("120002", "无响应信息"),
	e_120003("120003", "响应数据格式错误"),
	e_120004("120004", "url不合法"),
	e_120005("120005", "编码错误"),
	
	
	e_999999("999999", "e.getMessage()响应信息"),
	
	
	;
	
	private final String code;
	private final String error;

	private TCErrorCode(String code, String error) {
		this.code = code;
		this.error = error;
	}
	public String code() {
		return code;
	}

	public String error() {
		return error;
	}
	
    public static String getCode(String val) {
        for (TCErrorCode c : TCErrorCode.values()) {
            if (val.equals(c.error)) {
                return c.code;
            }
        }
        return "999999";
    }
}
