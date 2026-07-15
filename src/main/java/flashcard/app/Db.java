package flashcard.app;

import java.sql.Connection;
import java.sql.SQLException;

public interface Db {

	Connection open() throws SQLException;

	record SimpleConnection(Db db) {

		public void execute(String sql) throws SQLException {
			try (var connection = db.open(); var statement = connection.createStatement()) {
				statement.execute(sql);
			}
		}
	}
}
