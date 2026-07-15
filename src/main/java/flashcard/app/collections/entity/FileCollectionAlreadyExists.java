package flashcard.app.collections.entity;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileCollectionAlreadyExists implements CollectionAlreadyExists {

	private final Path directory;

	public FileCollectionAlreadyExists(final Path directory) {
		this.directory = directory;
	}

	@Override
	public boolean alreadyExists(String collectionName) {
		return Files.exists(directory.resolve(collectionName));
	}
}
