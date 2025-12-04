package flashcard.app.collection.entity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

public class FileCollection extends CollectionInMemory {

	private final Path filePath;

	public FileCollection(final Path filePath, final List<Flashcard> flashcards) {
		super(filePath.getFileName().toString(), flashcards);

		this.filePath = filePath;
	}

	@Override
	public void delete() throws CouldNotBeDeleted {
		try {
			Files.delete(filePath);
		} catch (NoSuchFileException e) {
			throw Collection.CouldNotBeDeleted.becauseItDoesNotExist(Name(), e);
		} catch (IOException e) {
			throw new CouldNotBeDeleted(e);
		}
	}
}
