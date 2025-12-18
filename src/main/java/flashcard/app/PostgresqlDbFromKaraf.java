package flashcard.app;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * PostgreSQL database acquired using JNDI lookup to get the DataSource from the Karaf container.
 */
public class PostgresqlDbFromKaraf implements Db {

	// Using OSGi injection complicates simple injection
	// and conflicts with the hk2 injection container.
	// That is why I am using JNDI look up for now.
//	@Reference(target = "(osgi.jndi.service.name=collections)")
//	public void setDataSource(DataSource ds) {
//		this.dataSource = ds;
//	}

	@Override
	public Connection open() throws SQLException {
		DataSource dataSource;
		try {
			InitialContext ctx = new InitialContext();
			dataSource = (DataSource) ctx.lookup("osgi:service/collections");
		} catch (NamingException e) {
			throw new SQLException("Failed to lookup DataSource 'osgi:service/collections'", e);
		}
		return dataSource.getConnection();
	}

	@Override
	public String toString() {
		return "PostgreSQL";
	}
}
