package flashcard.app.collection.entity;

import flashcard.app.ExampleApkgFile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class FlashcardsFromSqliteDatabaseTest {

	@Test
	void flashcards_are_available() {
		assertDoesNotThrow(() -> new FlashcardsFromSqliteDatabase(new DbFromPath(new ExampleApkgFile().getPath())).get());
	}
}