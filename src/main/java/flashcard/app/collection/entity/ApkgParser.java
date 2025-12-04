package flashcard.app.collection.entity;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.nio.file.FileSystems.newFileSystem;
import static java.nio.file.Files.createTempDirectory;
import static java.nio.file.Files.createTempFile;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.sql.DriverManager.getConnection;
import static java.util.Arrays.stream;

public class ApkgParser implements FlashcardRetriever {

	private final Path apkgPath;

	public ApkgParser(Path apkgPath) { this.apkgPath = apkgPath; }

	@Override
	public List<Flashcard> get() throws CouldNotRetrieve {
		var flashcards = new ArrayList<Flashcard>();

		final Path dbPath = extractDatabaseFromApkgFile();

		final var url   = "jdbc:sqlite:" + dbPath;
		final var query = "SELECT notes.flds FROM notes";

		try (var connection = getConnection(url);
			 var statement = connection.createStatement();
			 var result = statement.executeQuery(query))
		{
			while (result.next()) {
				String[] fields = result.getString("flds").split("\u001F");
				if (fields.length >= 2) {
					flashcards.add(new Flashcard(fields[0], fields[1]));
				}
			}
		} catch (SQLException e) {
			flashcards.clear();	// rollback

			throw new CouldNotRetrieve("Database could not be read." , e);
		}

		return flashcards;
	}

	private Path extractDatabaseFromApkgFile() throws CouldNotRetrieve {
		Path tempDbPath = null;
		try (var fileSystem = newFileSystem(apkgPath)) {
			Path dbToUse = findDatabase(fileSystem);

			String fileName      = dbToUse.getFileName().toString();
			String fileExtension = fileName.substring(fileName.lastIndexOf('.'));

			Path tempFile;
			try {
				tempFile = createTempFile(createTempDirectory("flashcards_"), null, fileExtension);
			} catch (IOException e) {
				throw new CouldNotRetrieve("Temporary files could not be created" + apkgPath, e);
			}

			try {
				tempDbPath = Files.copy(dbToUse, tempFile, REPLACE_EXISTING);
			} catch (IOException e) {
				throw new CouldNotRetrieve("Database could not be copied to its temporary location" + apkgPath, e);
			}

		} catch (IOException ignored) {}

		return tempDbPath;
	}

	private Path findDatabase(FileSystem fileSystem) throws CouldNotRetrieve {
		Optional<Path> dbPath = Optional.empty();
		try (final Stream<Path> dbPaths = Files.find(
				fileSystem.getPath("."),
				1,
				(path, attrs) -> hasOneOfExtension(path, ".anki2", ".anki21")))
		{
			dbPath = dbPaths.findFirst();
		} catch (IOException ignored) {}

		if (dbPath.isEmpty()) {
			throw new CouldNotRetrieve("No database file found in APKG file: " + apkgPath);
		}
		return dbPath.get();
	}

	private static boolean hasOneOfExtension(final Path path, String... extensions) {
		return stream(extensions).anyMatch(path.toString()::endsWith);
	}
}
