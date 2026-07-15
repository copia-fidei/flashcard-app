package flashcard.app.collections.entity;

import flashcard.app.Db;
import flashcard.app.collection.entity.*;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * Imports APKG files into the database.
 */
public class DatabaseImporter implements Importer {

	private final Db          database;
	private final String      fileName;
	private final InputStream inputStream;

	public DatabaseImporter(final Db database, final String fileName, final InputStream inputStream) {
		this.database = database;
		this.inputStream = inputStream;
		this.fileName = fileName;
	}

	@Override
	public Collection importCollection() throws CouldNotImport {
		var flashcardsFromSqliteDatabase = new FlashcardsFromSqliteDatabase(new DbFromInputStream(inputStream));

		List<Flashcard> flashcards;
		try {
			flashcards = flashcardsFromSqliteDatabase.get();
		} catch (RetrievedFlashcards.NotAvailable e) {
			throw CouldNotImport.Collection(fileName, e);
		}
		var collection = new CollectionFromDatabase(database, fileName, flashcards);
		
		var insertCollectionSQL = "INSERT INTO collections (name) VALUES (?)";
		var insertFlashcardsSQL = "INSERT INTO flashcards (collection, front, back) VALUES (?, ?, ?)";

		try (var connection = database.open()) {
			connection.setAutoCommit(false);
			try (var insertCollectionStmt = connection.prepareStatement(insertCollectionSQL);
				 var insertFlashcardsStmt = connection.prepareStatement(insertFlashcardsSQL)) {

				insertCollectionStmt.setString(1, fileName);
				insertCollectionStmt.executeUpdate();

				for (var flashcard : flashcards) {
					insertFlashcardsStmt.setString(1, fileName);
					insertFlashcardsStmt.setString(2, flashcard.Front());
					insertFlashcardsStmt.setString(3, flashcard.Back());
					insertFlashcardsStmt.addBatch();
				}
				insertFlashcardsStmt.executeBatch();

				connection.commit();
			} catch (SQLException e) {
				connection.rollback();
				throw CouldNotImport.Collection(fileName, e);
			}
		} catch (SQLException e) {
			throw CouldNotImport.Collection(fileName, e);
		}
		return collection;
	}

}
