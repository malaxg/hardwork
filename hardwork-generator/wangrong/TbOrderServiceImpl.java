import java.util.ArrayList;
import java.util.List;

import com.huawei.allinone.utils.ParameterUtil;
import com.huawei.allinone.web.common.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TbOrderServiceImpl
 * 
 * @author wangrong
 * @version [版本号, 2099年12月31日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service
public class TbOrderServiceImpl implements TbOrderService
{
	@Autowired
    private TbOrderDao tbOrderDao;

	@Override
	public RestResult findOne(s") String s)
	{
		List<TbOrder> tbOrder = new ArrayList<>();
		tbOrder.add(tbOrderDao.findOne(s));
		return RestResult.ok(tbOrder);
	}

	@Override
	public RestResult findAll()
	{
		return RestResult.ok(tbOrderDao.findAll());
	}

	@Override
	public void deleteByIds(List<String> ids)
	{
		if (ParameterUtil.isEmpty(ids))
		{
			return;
		}

		ids.forEach(k -> tbOrderDao.delete(k));
	}

	@Override
	public RestResult save(entity") TbOrder entity)
	{
		List<TbOrder> tbOrder = new ArrayList<>();
		tbOrder.add(tbOrderDao.save(entity));
		return RestResult.ok(tbOrder);
	}

}
