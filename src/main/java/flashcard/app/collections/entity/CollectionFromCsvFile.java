package flashcard.app.collections.entity;

import flashcard.app.collection.entity.FlashcardRetrieverFromCsvFile;
import flashcard.app.collection.entity.Collection;
import flashcard.app.collection.entity.FileCollection;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

import static java.nio.file.Files.readString;
import static java.util.logging.Logger.getLogger;

public class CollectionFromCsvFile implements CollectionsSource {

	private static final Logger LOG = getLogger(CollectionFromCsvFile.class.getName());

	private final Path csvFile;

	public CollectionFromCsvFile(Path csvFile) {
		this.csvFile = csvFile;
	}

	@Override
	public List<Collection> get() {
		final String csvContent;
		try {
			csvContent = readString(csvFile);
		} catch (IOException e) {
			LOG.warning("CSV file " + csvFile + " could not be read: " + e.getMessage());

			return List.of();
		}
		return List.of(new FileCollection(csvFile, new FlashcardRetrieverFromCsvFile(csvContent).get()));
	}
}
