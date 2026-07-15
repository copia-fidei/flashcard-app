package flashcard.app.collections.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DbFromInputStreamTest {

	@Test
	void extracted_database_is_available() {
		var resource = getClass().getResourceAsStream("/US_Presidents_w_pics.apkg");
		assertNotNull(resource);
		assertDoesNotThrow(() -> new DbFromInputStream(resource).tmpLocation());
	}

}