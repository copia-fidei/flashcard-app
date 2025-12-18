package flashcard.app.collections.entity;

import flashcard.app.collection.entity.FlashcardsFromCsvFile;
import flashcard.app.collection.entity.Collection;
import flashcard.app.collection.entity.FileCollection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

public class CollectionFromCsvFile implements CollectionsSource {

	private static final Logger LOG = Logger.getLogger(CollectionFromCsvFile.class.getName());

	private final Path csvFile;

	public CollectionFromCsvFile(Path csvFile) {
		this.csvFile = csvFile;
	}

	@Override
	public List<Collection> get() {
		final String csvContent;
		try {
			csvContent = Files.readString(csvFile);
		} catch (IOException e) {
			LOG.warning("CSV file " + csvFile + " could not be read: " + e.getMessage());

			return List.of();
		}
		return List.of(new FileCollection(csvFile, new FlashcardsFromCsvFile(csvContent).get()));
	}
}
