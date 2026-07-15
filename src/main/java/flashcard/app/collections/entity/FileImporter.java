package flashcard.app.collections.entity;

import flashcard.app.collection.entity.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static flashcard.app.Extensions.getExtension;
import static java.nio.file.StandardOpenOption.CREATE;

/**
 * Imports APKG and CSV files directly as files onto the server.
 */
public class FileImporter implements Importer {

	private final Path        directory;
	private final String      fileName;
	private final InputStream inputStream;

	public FileImporter(Path directory, String fileName, InputStream inputStream) {
		this.directory = directory;
		this.fileName = fileName;
		this.inputStream = inputStream;
	}

	@Override
	public Collection importCollection() throws CouldNotImport {
		try (inputStream) {
			byte[] fileContent = inputStream.readAllBytes();

			Path fileOnServer = Files.write(directory, fileContent, CREATE);

			String extension = getExtension(fileName);
			var flashcards = switch (extension) {
				case ".csv"  -> new FlashcardsFromCsvFile(new String(fileContent));
				case ".apkg" -> new FlashcardsFromSqliteDatabase(new DbFromPath(fileOnServer));

				default -> throw new CouldNotImport("Unsupported file format: " + extension);
			};

			return new FileCollection(fileOnServer, flashcards.get());
		} catch (IOException | RetrievedFlashcards.NotAvailable e) {
			throw new CouldNotImport(e);
		}
	}
}
