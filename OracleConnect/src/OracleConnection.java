import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class OracleConnection {

	public static void main(String[] args) throws Exception {

		System.out.println("Oracle DB Connection");

		Class.forName("oracle.jdbc.OracleDriver");

		Connection connection = DriverManager.getConnection(
				"jdbc:oracle:thin:@//rff.fors.ru:1521/rff.fors.ru",
				"FK_ACADEMY", "fk_academy");

		try {
			CallableStatement cs = null;
			cs = connection
					.prepareCall("{call ws_ctx_pkg.set_ws_id(1,'ADMIN')}");
			ResultSet rs = cs.executeQuery();
			
			String query = "select SYS_CONTEXT ('WS_CTX', 'WS_ID'), SYS_CONTEXT ('WS_CTX', 'WS_TYPE') from dual";
			Statement stmt = connection.createStatement();
			ResultSet rs2 = stmt.executeQuery(query); 
			
			int i = 1;
			while(rs2.next()){
				System.out.println(rs2.getString(i));
				i++;
			}
			
			
		} finally {
			try {
				connection.close();
			} catch (Exception ignore) {
			}
		}
	}

}
