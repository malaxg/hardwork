import javax.persistence.*;
import java.util.Date;

/**
 * TbOrder
 * 
 * @author wangrong
 * @version [版本号, 2099年12月31日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Entity
@Table(name = "tb_order")
public class TbOrder
{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "order_id", columnDefinition = "varchar(50) default  comment '订单id'")
    private String orderId;

    @Column(name = "payment", columnDefinition = "varchar(50) comment '实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分'")
    private String payment;

    @Column(name = "payment_type", columnDefinition = "int(2) comment '支付类型，1、在线支付，2、货到付款'")
    private int paymentType;

    @Column(name = "post_fee", columnDefinition = "varchar(50) comment '邮费。精确到2位小数;单位:元。如:200.07，表示:200元7分'")
    private String postFee;

    @Column(name = "status", columnDefinition = "int(10) comment '状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭'")
    private int status;

    @Column(name = "create_time", columnDefinition = "datetime comment '订单创建时间'")
    private Date createTime;

    @Column(name = "update_time", columnDefinition = "datetime comment '订单更新时间'")
    private Date updateTime;

    @Column(name = "payment_time", columnDefinition = "datetime comment '付款时间'")
    private Date paymentTime;

    @Column(name = "consign_time", columnDefinition = "datetime comment '发货时间'")
    private Date consignTime;

    @Column(name = "end_time", columnDefinition = "datetime comment '交易完成时间'")
    private Date endTime;

    @Column(name = "close_time", columnDefinition = "datetime comment '交易关闭时间'")
    private Date closeTime;

    @Column(name = "shipping_name", columnDefinition = "varchar(20) comment '物流名称'")
    private String shippingName;

    @Column(name = "shipping_code", columnDefinition = "varchar(20) comment '物流单号'")
    private String shippingCode;

    @Column(name = "user_id", columnDefinition = "bigint(20) comment '用户id'")
    private Long userId;

    @Column(name = "buyer_message", columnDefinition = "varchar(100) comment '买家留言'")
    private String buyerMessage;

    @Column(name = "buyer_nick", columnDefinition = "varchar(50) comment '买家昵称'")
    private String buyerNick;

    @Column(name = "buyer_rate", columnDefinition = "int(2) comment '买家是否已经评价'")
    private int buyerRate;

}
