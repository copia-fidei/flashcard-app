package flashcard.app.collection.entity;

import flashcard.app.Extensions;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import static java.nio.file.FileSystems.newFileSystem;
import static java.nio.file.Files.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class SqliteDbFromPath implements ApkgFileSqliteDb {

	private final Path apkgFile;

	public SqliteDbFromPath(final Path apkgFile) { this.apkgFile = apkgFile; }

	@Override
	public Path tmpLocation() throws NotAvailable {
		try (var fileSystem = newFileSystem(apkgFile)) {
			Path dbToUse = findDatabase(fileSystem);
			
			return copy(dbToUse, new TempDbLocation(dbToUse).path(), REPLACE_EXISTING);
		} catch (IOException e) {
			throw new NotAvailable("Database could not be copied to its temporary location");
		} catch (Exception e) {
			throw new NotAvailable(e);
		}
	}

	private Path findDatabase(FileSystem fileSystem) throws NotAvailable {
		Optional<Path> dbPath;
		try (final Stream<Path> everyPossibleDb = Files.find(
				fileSystem.getPath("."),
				1,
				(path, attrs) -> Extensions.hasOneOf(path, ".anki2", ".anki21")))
		{
			dbPath = everyPossibleDb.min(new UseNewerDatabaseVersion());
		} catch (IOException e) {
			throw new NotAvailable(e);
		}
		if (dbPath.isEmpty()) {
			throw new NotAvailable("No database file found in APKG file: " + apkgFile);
		}
		return dbPath.get();
	}
}
