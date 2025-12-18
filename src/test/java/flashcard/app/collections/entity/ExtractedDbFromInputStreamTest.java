package flashcard.app.collections.entity;

import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class ExtractedDbFromInputStreamTest {

	@Test
	void extracted_database_is_available() {
		final InputStream resource = getClass().getResourceAsStream("/US_Presidents_w_pics.apkg");
		assertNotNull(resource);
		assertDoesNotThrow(() -> new ExtractedDbFromInputStream(resource).get());
	}

}