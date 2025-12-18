package flashcard.app.collections.entity;

import flashcard.app.Db;
import flashcard.app.collection.entity.CollectionInMemory;
import flashcard.app.collection.entity.Flashcard;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;


public class CollectionFromDatabase extends CollectionInMemory {

	private final Db database;

	public CollectionFromDatabase(final Db database, final String name) {
		super(name);
		this.database = database;
	}

	public CollectionFromDatabase(final Db database, final String name, List<Flashcard> flashcards) {
		super(name, flashcards);
		this.database = database;
	}

	@Override
	public void delete() throws CouldNotBeDeleted {
		String deleteCollectionSQL = "DELETE FROM collections WHERE name = ?";
		String deleteFlashcardsSQL = "DELETE FROM flashcards WHERE collection = ?";

		try (var connection = database.open()) {
			connection.setAutoCommit(false);

			try (var deleteFlashcardsStmt = connection.prepareStatement(deleteFlashcardsSQL);
				 var deleteCollectionStmt = connection.prepareStatement(deleteCollectionSQL)) {

				deleteFlashcardsStmt.setString(1, Name());
				deleteFlashcardsStmt.executeUpdate();

				deleteCollectionStmt.setString(1, Name());
				int rowsAffected = deleteCollectionStmt.executeUpdate();

				if (rowsAffected == 0) {
					connection.rollback();
					throw CouldNotBeDeleted.becauseItDoesNotExist(Name());
				}
				connection.commit();
			} catch (SQLException e) {
				connection.rollback();
				throw CouldNotBeDeleted.name(Name(), e);
			}
		} catch (SQLException e) {
			throw new CouldNotBeDeleted(e);
		}
		Flashcards().clear();
	}

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof final CollectionFromDatabase that)) return false;
		return Objects.equals(Name(), that.Name());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(Name());
	}
}
