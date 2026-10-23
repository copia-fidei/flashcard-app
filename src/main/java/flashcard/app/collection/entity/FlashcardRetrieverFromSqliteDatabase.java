package flashcard.app.collection.entity;

import flashcard.app.collections.entity.SqliteDbFromInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class FlashcardRetrieverFromSqliteDatabase implements FlashcardRetriever {

	private final ApkgFileSqliteDb database;

	public FlashcardRetrieverFromSqliteDatabase(ApkgFileSqliteDb database) {
		this.database = database;
	}

	public FlashcardRetrieverFromSqliteDatabase(InputStream apkg) {
		this.database = new SqliteDbFromInputStream(apkg);
	}

	public FlashcardRetrieverFromSqliteDatabase(Path apkgFile) {
		this.database = new SqliteDbFromPath(apkgFile);
	}

	@Override
	public List<Flashcard> get() throws CouldNotRetrieve {
		var flashcards = new ArrayList<Flashcard>();

		final Path dbLocation;
		try {
			dbLocation = database.tmpLocation();
		} catch (ApkgFileSqliteDb.NotAvailable e) {
			throw new CouldNotRetrieve(e);
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
					flashcards.add(new Flashcard(fields));
				}
			}
		} catch (SQLException e) {
			flashcards.clear();	// rollback

			throw new CouldNotRetrieve("Database could not be read." , e);
		}
		try {
			Files.delete(dbLocation);
		} catch (IOException ignored) {}


		return flashcards;
	}
}
