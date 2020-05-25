package com.malaxg.codegenerator.httpcontroller;

import java.util.List;

import com.malaxg.codegenerator.Generator;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

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
public class TbOrderController {


	/**
	 * 查询单个
	 *
	 * @param order_id order_id
	 * @return RestResult RestResult
	 * @author wangrong
	 * @see [相关类/方法]
	 */
	@GetMapping
	@ApiOperation("查询单个")
	public void findOne(@RequestParam(value = "order_id") String order_id) {
	}

	/**
	 * 根据主键删除
	 *
	 * @param order_ids order_ids
	 * @author wangrong
	 * @see [相关类/方法]
	 */
	@DeleteMapping
	@ApiOperation("根据主键删除")
	public void deleteByIds(@RequestParam(value = "order_ids") List<String> order_ids) {
	}

	/**
	 * 添加
	 *
	 * @param generator generator
	 * @return RestResult RestResult
	 * @author wangrong
	 * @see [相关类/方法]
	 */
	@PostMapping
	@ApiOperation("添加")
	public void save(@RequestBody Generator generator) {
	}
}
