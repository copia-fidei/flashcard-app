package flashcard.app.collection.entity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class FlashcardsFromApkgDatabase implements RetrievedFlashcards {

	private final ExtractedSqliteDb extractedDatabase;

	public FlashcardsFromApkgDatabase(ExtractedSqliteDb extractedDatabase) {
		this.extractedDatabase = extractedDatabase;
	}

	@Override
	public List<Flashcard> get() throws NotAvailable {
		var flashcards = new ArrayList<Flashcard>();

		final Path extractedDbPath;
		try {
			extractedDbPath = extractedDatabase.get();
		} catch (ExtractedSqliteDb.NotAvailable e) {
			throw new NotAvailable(e);
		}

		final var url   = "jdbc:sqlite:" + extractedDbPath;
		final var query = "SELECT notes.flds FROM notes";

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
			Files.delete(extractedDbPath);
		} catch (IOException ignored) {}


		return flashcards;
	}
}
