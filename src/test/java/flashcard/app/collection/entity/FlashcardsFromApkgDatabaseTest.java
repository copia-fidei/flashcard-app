package flashcard.app.collection.entity;

import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;

class FlashcardsFromApkgDatabaseTest {

	@Test
	void flashcards_are_available() {
		Path apkgFile;
		try {
			final URL resource = getClass().getResource("/US_Presidents_w_pics.apkg");
			apkgFile = Paths.get(requireNonNull(resource).toURI());
		} catch (Exception e) {
			fail(e);
			return;
		}
		assertDoesNotThrow(() -> new FlashcardsFromApkgDatabase(new ExtractedDbFromPath(apkgFile)).get());
	}
}