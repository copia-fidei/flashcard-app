package flashcard.app.collection.entity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class FlashcardsFromSqliteDatabase implements RetrievedFlashcards {

	private final ApkgFileSqliteDb database;

	public FlashcardsFromSqliteDatabase(ApkgFileSqliteDb database) {
		this.database = database;
	}

	@Override
	public List<Flashcard> get() throws NotAvailable {
		var flashcards = new ArrayList<Flashcard>();

		final Path dbLocation;
		try {
			dbLocation = database.tmpLocation();
		} catch (ApkgFileSqliteDb.NotAvailable e) {
			throw new NotAvailable(e);
		}

		var url   = "jdbc:sqlite:" + dbLocation;
		var query = "SELECT notes.flds FROM notes";

		try (var connection = getConnection(url);
			 var statement = connection.createStatement();
			 var result = statement.executeQuery(query))
		{
			while (result.next()) {
				String[] fields = result.getString("flds").split("\u001F");
				if (fields.length >= 2) {
					flashcards.add(new Flashcard(fields[0], fields[1]));
				}
			}
		} catch (SQLException e) {
			flashcards.clear();	// rollback

			throw new NotAvailable("Database could not be read." , e);
		}
		try {
			Files.delete(dbLocation);
		} catch (IOException ignored) {}


		return flashcards;
	}
}
