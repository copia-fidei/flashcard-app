package flashcard.app.collections.entity;

import flashcard.app.Db;
import flashcard.app.ExampleApkgFile;
import flashcard.app.collection.entity.Collection;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import static java.util.Collections.sort;
import static org.junit.jupiter.api.Assertions.*;

// this is getting into integration test territory
class DatabaseImporterTest {

	@Test
	void importCollection() {
		Db database = new TestPostgresqlDb();
		reset(database);

		var apkgFile = new ExampleApkgFile();

		InputStream resource = apkgFile.InputStream();
		assertNotNull(resource);

		try {
			var collection_imported_into_the_database = new DatabaseImporter(database, apkgFile.name(), resource).importCollection();
			var all_collections_from_the_database     = new CollectionsFromDb(database).get();

			Optional<Collection> imported_collection_refetched_from_the_database = all_collections_from_the_database.stream()
				.filter(collection -> collection.Name().equals(apkgFile.name())).findAny();

			assertTrue(imported_collection_refetched_from_the_database.isPresent());

			var flashcards_of_collection_imported_into_the_database           = new ArrayList<>(collection_imported_into_the_database.Flashcards());
			var flashcards_of_imported_collection_refetched_from_the_database = new ArrayList<>(imported_collection_refetched_from_the_database.get().Flashcards());

			sort(flashcards_of_collection_imported_into_the_database);
			sort(flashcards_of_imported_collection_refetched_from_the_database);

			assertIterableEquals(flashcards_of_collection_imported_into_the_database, flashcards_of_imported_collection_refetched_from_the_database);
		} catch (Importer.CouldNotImport e) {
			fail(e);
		}
	}

	private static void reset(Db database) {
		try {
			new Db.SimpleConnection(database).execute("TRUNCATE TABLE collections CASCADE;");
		} catch (SQLException e) {
			fail(e);
		}
	}
}