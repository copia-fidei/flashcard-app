package flashcard.app.collection.entity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE;

public class FileCollection extends CollectionImp {

	private final Path directory;
	private final byte[] fileContent;

	public FileCollection(final Path directory, final String fileName, final byte[] fileContent, final List<Flashcard> flashcards) {
		super(fileName, flashcards);
		this.directory = directory;
		this.fileContent = fileContent;
	}

	@Override
	public void save() {
		try {
			Files.write(directory.resolve(Name()), fileContent, CREATE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


}
