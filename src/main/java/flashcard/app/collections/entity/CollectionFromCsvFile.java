package flashcard.app.collections.entity;

import flashcard.app.collection.entity.FlashcardRetriever.CouldNotRetrieve;
import flashcard.app.collection.entity.FlashcardRetrieverFromCsvFile;
import flashcard.app.collection.entity.Collection;
import flashcard.app.collection.entity.FileCollection;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

public class CollectionFromCsvFile implements Supplier<List<Collection>> {

	private static final Logger LOG = getLogger(CollectionFromCsvFile.class.getName());

	private final Path csvFile;

	public CollectionFromCsvFile(Path csvFile) {
		this.csvFile = csvFile;
	}

	@Override
	public List<Collection> get() {
		try {
			var flashcards = new FlashcardRetrieverFromCsvFile(csvFile).get();
			return List.of(new FileCollection(csvFile, flashcards));
		} catch (CouldNotRetrieve e) {
			LOG.warning("Flashcards could not be retrieved from file " + csvFile + ": " + e.getMessage());

			return List.of();
		}
	}
}
