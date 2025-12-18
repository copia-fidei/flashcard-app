package flashcard.app.collection.entity;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static flashcard.app.Extensions.hasExtension;
import static org.junit.jupiter.api.Assertions.*;

class UseNewerDatabaseVersionTest {

	public static final String NEW_VERSION = ".anki21";

	@Test
	void given_the_old_and_the_newer_database_version_the_new_version_is_selected() {
		final Path new1 = Path.of("file.anki21");
		final Path old1 = Path.of("file.anki2");

		final var unsortedList = new ArrayList<>(List.of(new1, old1));
		unsortedList.sort(new UseNewerDatabaseVersion());

		assertEquals(new1, unsortedList.getFirst());
	}

	@Test
	void all_newer_databases_become_the_first_entries_after_sorting() {
		final Path other1 = Path.of("other");
		final Path new1   = Path.of("file.anki21");
		final Path old1   = Path.of("file.anki2");
		final Path new2   = Path.of("file2.anki21");
		final Path other2 = Path.of("other2");
		final Path old2   = Path.of("file2.anki2");
		final Path new3   = Path.of("file3.anki21");

		final var unsortedList = new ArrayList<>(List.of(other1, new1, old1, new2, other2, old2, new3));
		final long amountOfNewerDatabases =
				unsortedList.stream().filter(e -> hasExtension(e, NEW_VERSION)).count();

		unsortedList.sort(new UseNewerDatabaseVersion());

		System.out.println("Sorted list: " + unsortedList);
		for (int i = 0; i < amountOfNewerDatabases; i++) {
			assertTrue(hasExtension(unsortedList.get(i), NEW_VERSION));
		}
	}
}