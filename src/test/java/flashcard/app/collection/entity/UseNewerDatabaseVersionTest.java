package flashcard.app.collection.entity;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static flashcard.app.Extensions.hasExtension;
import static java.nio.file.Path.of;
import static org.junit.jupiter.api.Assertions.*;

/**
 * APKG files contain a SQL lite database.
 * The older version is .anki2 and the newer version is .anki21.
 * We test that sorting comparator works.
 */
class UseNewerDatabaseVersionTest {

	private static final String NEW_VERSION = ".anki21";

	@Test
	void given_the_old_and_the_newer_database_version_the_new_version_is_selected() {
		Path new1 = of("file.anki21");
		Path old1 = of("file.anki2");

		var list = new ArrayList<Path>();
		list.add(new1);
		list.add(old1);

		list.sort(new UseNewerDatabaseVersion());

		assertEquals(new1, list.getFirst());
	}

	@Test
	void newer_database_versions_are_sorted_first() {
		var versions = new ArrayList<>(List.of(
			of("other"),
			of("file.anki21"),
			of("file.anki2"),
			of("file2.anki21"),
			of("other2"),
			of("file2.anki2"),
			of("file3.anki21")
		));
		long amountOfNewVersions = versions.stream().filter(e -> hasExtension(e, NEW_VERSION)).count();

		versions.sort(new UseNewerDatabaseVersion());

		System.out.println("Sorted versions: " + versions);
		for (int i = 0; i < amountOfNewVersions; i++) {
			assertTrue(hasExtension(versions.get(i), NEW_VERSION));
		}
	}
}