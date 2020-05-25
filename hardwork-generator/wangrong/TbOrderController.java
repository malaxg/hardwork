import java.util.List;

import com.huawei.allinone.web.common.RestResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**
 * TbOrderController
 * 
 * @author wangrong
 * @version [版本号, 2099年12月31日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@RestController
@RequestMapping("/tborder")
public class TbOrderController
{
	@Autowired
    private TbOrderService tbOrderService;


	/**
	 * 查询单个
	 * 
	 * @param orderId orderId
	 * @return RestResult RestResult
	 * @author wangrong
	 * @see [相关类/方法]
	 */
	@GetMapping
	@ApiOperation("查询单个")
	public RestResult findOne(@RequestParam(value = "orderId") String orderId)
	{
		return tbOrderService.findOne(orderId);
	}

	/**
	 * 根据主键删除
	 * 
	 * @param orderIds orderIds
	 * @author wangrong
	 * @see [相关类/方法]
	 */
	@DeleteMapping
	@ApiOperation("根据主键删除")
	public void deleteByIds(@RequestParam(value = "orderIds") List<String> orderIds)
	{
		tbOrderService.deleteByIds(orderIds);
	}

	/**
	 * 添加
	 * 
	 * @param tbOrder tbOrder
	 * @return RestResult RestResult
	 * @author wangrong
	 * @see [相关类/方法]
	 */
	@PostMapping
	@ApiOperation("添加")
	public RestResult save(@RequestBody TbOrder tbOrder)
	{
		return tbOrderService.save(tbOrder);
	}
}
