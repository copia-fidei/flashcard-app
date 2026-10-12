package flashcard.app.collections.entity;

import flashcard.app.Db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * INFO: The test database can be created by running the test/resources/init.sql script in psql.
 * The database credentials are in test/resources/db.properties; this file is not commited to git.
 */
class TestPostgresqlDb implements Db {

	@Override
	public Connection open() throws SQLException {
		try (var input = getClass().getResourceAsStream("/db.properties")) {
			var prop = new Properties();
			prop.load(input);
			return DriverManager.getConnection(
				prop.getProperty("db.url"),
				prop.getProperty("db.username"),
				prop.getProperty("db.password")
			);
		} catch (IOException e) {
			throw new SQLException(e);
		}
	}

	@Override
	public String toString() {
		return "Test PostgreSQL database";
	}
}
