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

			String query = "select SYS_CONTEXT ('WS_CTX', 'WS_ID') as one, SYS_CONTEXT ('WS_CTX', 'WS_TYPE') as two from dual";
			Statement stmt = connection.createStatement();
			ResultSet rs2 = stmt.executeQuery(query);

			int i = 1;
			while (rs2.next()) {
				String one = rs2.getString("one");
				String two = rs2.getString("two");

				System.out.println(one + " " + two);
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
