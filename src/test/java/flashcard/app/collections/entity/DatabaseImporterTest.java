package flashcard.app.collections.entity;

import flashcard.app.Db;
import flashcard.app.collection.entity.Collection;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

// this is getting into integration test territory
class DatabaseImporterTest {


	/**
	 * INFO: This is a test database. It must be present for the test to work.
	 *
	 * You can create the database by running the init.sql file in the test/resources directory.
	 * The credentials are in the db.properties file in the test/resources directory which is not commited to git.
	 */
	private static class TestPostgresqlDb implements Db {

		@Override
		public Connection open() throws SQLException {
			try (var input = getClass().getResourceAsStream("/db.properties")) {
				if (input == null) {
					throw new SQLException("db.properties not found in classpath");
				}
				var prop = new Properties();
				prop.load(input);
				return DriverManager.getConnection(
						prop.getProperty("db.url"),
						prop.getProperty("db.username"),
						prop.getProperty("db.password")
				);
			} catch (IOException e) {
				throw new SQLException(e);
			}
		}

		@Override
		public String toString() {
			return "Test PostgreSQL database";
		}
	}

	@Test
	void importCollection() {
		final String apkgCollection = "US_Presidents_w_pics.apkg";

		final Db database = new TestPostgresqlDb();
		reset(database);

		final InputStream resource = getClass().getResourceAsStream("/" + apkgCollection);
		assertNotNull(resource);

		try {
			final var collection_that_has_been_imported_into_the_database = new DatabaseImporter(database, apkgCollection, resource).importCollection();
			final var all_collections_from_the_database                     = new CollectionsFromDb(database).get();

			final Optional<Collection> imported_collection_refetched_from_the_database = all_collections_from_the_database.stream()
				.filter(collection -> collection.Name().equals(apkgCollection)).findAny();

			assertTrue(imported_collection_refetched_from_the_database.isPresent());

			final var flashcards_of_collection_that_has_been_imported_into_the_database = new ArrayList<>(collection_that_has_been_imported_into_the_database.Flashcards());
			final var flashcards_of_imported_collection_refetched_from_the_database = new ArrayList<>(imported_collection_refetched_from_the_database.get().Flashcards());

			java.util.Collections.sort(flashcards_of_collection_that_has_been_imported_into_the_database);
			java.util.Collections.sort(flashcards_of_imported_collection_refetched_from_the_database);

			assertIterableEquals(flashcards_of_collection_that_has_been_imported_into_the_database, flashcards_of_imported_collection_refetched_from_the_database);
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