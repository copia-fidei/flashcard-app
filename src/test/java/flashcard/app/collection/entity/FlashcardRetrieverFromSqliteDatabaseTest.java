package flashcard.app.collection.entity;

import flashcard.app.ExampleApkgFile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class FlashcardRetrieverFromSqliteDatabaseTest {

	@Test
	void flashcards_are_available() {
		assertDoesNotThrow(() -> new FlashcardRetrieverFromSqliteDatabase(new SqliteDbFromPath(new ExampleApkgFile().getPath())).get());
	}
}