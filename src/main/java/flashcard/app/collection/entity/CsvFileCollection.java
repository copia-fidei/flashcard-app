package flashcard.app.collection.entity;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

public class CsvFileCollection implements Collection {

	private static Logger LOG = Logger.getLogger(CsvFileCollection.class.getName());

	private final Path filePath;
	private final List<Flashcard> flashcards = new ArrayList<>();

	public CsvFileCollection(final Path filePath) {
		this.filePath = filePath;
	}

	@Override
	public String Name() {
		return filePath.getFileName().toString();
	}

	@Override
	public List<Flashcard> Flashcards() {
		if (flashcards.isEmpty()) {
			try {
				flashcards.addAll(Files.readString(filePath)
						.lines()
						.map(line -> {
							final String[] split = line.split(",");
							return new Flashcard(split[0], split[1]);
						})
						.collect(toList()));
			} catch (IOException e) {
				LOG.warning("IO Exception occurred while retrieving flashcards from file.");
			}
		}
		return flashcards;
	}
}
