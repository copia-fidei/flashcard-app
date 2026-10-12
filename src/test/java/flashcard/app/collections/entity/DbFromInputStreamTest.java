package flashcard.app.collections.entity;

import flashcard.app.ExampleApkgFile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DbFromInputStreamTest {

	@Test
	void extracted_database_is_available() {
		assertDoesNotThrow(() -> new DbFromInputStream(new ExampleApkgFile().getInputStream()).tmpLocation());
	}

}