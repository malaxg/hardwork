import java.util.Date;

/**
 * TbOrderVo
 * 
 * @author wangrong
 * @version [版本号, 2099年12月31日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class TbOrderVo
{
	/** 订单id */
    private String orderId;

	/** 实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分 */
    private String payment;

	/** 支付类型，1、在线支付，2、货到付款 */
    private int paymentType;

	/** 邮费。精确到2位小数;单位:元。如:200.07，表示:200元7分 */
    private String postFee;

	/** 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭 */
    private int status;

	/** 订单创建时间 */
    private Date createTime;

	/** 订单更新时间 */
    private Date updateTime;

	/** 付款时间 */
    private Date paymentTime;

	/** 发货时间 */
    private Date consignTime;

	/** 交易完成时间 */
    private Date endTime;

	/** 交易关闭时间 */
    private Date closeTime;

	/** 物流名称 */
    private String shippingName;

	/** 物流单号 */
    private String shippingCode;

	/** 用户id */
    private Long userId;

	/** 买家留言 */
    private String buyerMessage;

	/** 买家昵称 */
    private String buyerNick;

	/** 买家是否已经评价 */
    private int buyerRate;

}
