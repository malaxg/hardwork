package com.malaxg.codegenerator.httpcontroller;

import java.util.List;

import com.malaxg.codegenerator.Generator;
import com.malaxg.hardwork.web.common.RestResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * BudgetSourceController
 *
 * @author wangrong
 * @version [版本号, 2099年12月31日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@RestController
@RequestMapping("/budgetsource")
public class BudgetSourceController
{
	
	/**
	 * 查询单个
	 *
	 * @param id id
	 * @return RestResult RestResult
	 * @see [相关类/方法]
	 */
	@GetMapping
	@ApiOperation("查询单个")
	public RestResult findOne(@RequestParam(value = "id") String id)
	{
		return null;
	}
	
	/**
	 * 根据主键删除
	 *
	 * @param ids ids
	 * @see [相关类/方法]
	 */
	@DeleteMapping
	@ApiOperation("根据主键删除")
	public void deleteByIds(@RequestParam(value = "ids") List<String> ids)
	{
	}
	
	/**
	 * 添加
	 *
	 * @param budgetSource budgetSource
	 * @return RestResult RestResult
	 * @see [相关类/方法]
	 */
	@PostMapping
	@ApiOperation("添加")
	public RestResult save(@RequestBody Generator budgetSource)
	{
		return null;
	}
}
