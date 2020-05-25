import java.sql.*;

/**
 * @Description:
 * @author: malaxg
 * @date: 2020-03-22 14:51
 */
public class JDBCMain {
	private static final String SERVER_ADDRESS = "jdbc:mysql://localhost/jbcdtest";
	private static final String USER_NAME = "root";
	private static final String PASSWORD = "root";

	public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
	}


	/**
	 * 测试JDBC
	 */
	public void testJDBC() {
		try {
			//1.加载数据库驱动
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			//2.使用驱动管理器获取一个连接
			Connection connection = DriverManager.getConnection(SERVER_ADDRESS, USER_NAME, PASSWORD);
			//3.使用PrepareStatement预编译 sql语句
			String sql = "select * from registration;";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				System.out.println(resultSet.getInt("id"));
				System.out.println(resultSet.getInt("age"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
