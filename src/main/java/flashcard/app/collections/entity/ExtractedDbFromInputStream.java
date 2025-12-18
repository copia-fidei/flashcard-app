package flashcard.app.collections.entity;

import flashcard.app.collection.entity.ExtractedSqliteDb;
import flashcard.app.collection.entity.TemporaryDatabaseLocation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ExtractedDbFromInputStream implements ExtractedSqliteDb {

	private final InputStream inputStream;

	public ExtractedDbFromInputStream(final InputStream inputStream) {
		this.inputStream = inputStream;
	}

	@Override
	public Path get() throws NotAvailable {
		String databaseName = "";
		byte[] database     = new byte[0];
		try (var zipInputStream = new ZipInputStream(inputStream)) {
			ZipEntry entry;
			while ((entry = zipInputStream.getNextEntry()) != null) {
				if (entry.getName().endsWith(".anki2")) {
					database = zipInputStream.readAllBytes();
					databaseName = entry.getName();
				}
				// .anki21 will be used instead of .anki2 if it exists.
				if (entry.getName().endsWith(".anki21")) {
					database = zipInputStream.readAllBytes();
					databaseName = entry.getName();
				}
			}
		} catch (IOException e) {
			throw new NotAvailable(e);
		}

		try {
			return Files.write(new TemporaryDatabaseLocation(databaseName).path(), database);
		} catch (IOException e) {
			throw new NotAvailable("Database could not be copied to a temporary location", e);
		} catch (TemporaryDatabaseLocation.NotAvailable e) {
			throw new NotAvailable(e);
		}
	}
}
