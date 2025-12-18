package flashcard.app.collections.entity;

import flashcard.app.Db;

import java.sql.*;

public class AlreadyExistsInDatabase implements AlreadyExists {

	private final Db database;

	public AlreadyExistsInDatabase(final Db database) {
		this.database = database;
	}

	@Override
	public boolean alreadyExists(String collectionName) throws CouldNotBeDetermined {
		String sql = "SELECT EXISTS (SELECT 1 FROM collections WHERE name = ?)";

		try (var connection = database.open(); var statement = connection.prepareStatement(sql)) {
			statement.setString(1, collectionName);

			try (var result = statement.executeQuery()) {
				if (result.next()) {
					return result.getBoolean(1);
				}
			}
		} catch (SQLException e) {
			throw new CouldNotBeDetermined(e);
		}
		return false;
	}
}
