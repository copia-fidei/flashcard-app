package flashcard.app.collection.entity;

import flashcard.app.Extensions;

import java.io.IOException;
import java.nio.file.Path;

import static java.nio.file.Files.createTempDirectory;
import static java.nio.file.Files.createTempFile;

public class TempDbLocation {

	private final String extension;

	// two primary constructors unfortunately
	public TempDbLocation(String fileNameWithExtension) {
		this.extension = Extensions.getExtension(fileNameWithExtension);
	}

	TempDbLocation(Path pathWithExtension) {
		this.extension = Extensions.getExtension(pathWithExtension);
	}

	public Path path() throws NotAvailable {
		try {
			return createTempFile(createTempDirectory("flashcards_"), null, extension);
		} catch (IOException e) {
			throw new NotAvailable("Temporary files could not be created", e);
		}
	}

	public static class NotAvailable extends Exception {

		public NotAvailable(String s, Throwable t) {
			super(s, t);
		}
	}
}
