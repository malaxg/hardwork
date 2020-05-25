import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * TbOrderDao
 * 
 * @author wangrong
 * @version [版本号, 2099年12月31日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Repository
public interface TbOrderDao extends JpaRepository<TbOrder, String>
{
}
