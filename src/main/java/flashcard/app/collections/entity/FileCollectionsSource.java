package flashcard.app.collections.entity;

import flashcard.app.Configuration;
import flashcard.app.CsvParser;
import flashcard.app.collection.entity.Collection;
import flashcard.app.collection.entity.CollectionImp;
import flashcard.app.collection.entity.Flashcard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import static java.nio.file.Files.readString;
import static java.util.stream.Collectors.toList;

public class FileCollectionsSource implements CollectionsSource {

	private static Logger LOG = Logger.getLogger(FileCollectionsSource.class.getName());

	private final Path collectionsDir;

	private final Predicate<Path> filterCsvFiles = path -> path
			.getFileName()
			.toString()
			.endsWith(".csv");

	public FileCollectionsSource() {
		this(Configuration.COLLECTIONS_DIR);
	}

	public FileCollectionsSource(final Path collectionsDir) {
		this.collectionsDir = collectionsDir;
	}

	@Override
	public List<Collection> get() {
		List<Collection> csvCollections = List.of();
		try (var paths = Files.list(collectionsDir)) {
			csvCollections = paths
					.filter(filterCsvFiles)
					.map(this::toCollection)
					.collect(toList());
		} catch (IOException ignored) {}

		return csvCollections;
	}

	private Collection toCollection(Path path) {
		var flashcards = new ArrayList<Flashcard>();
		var csvParser = new CsvParser();
		try {
			flashcards.addAll(csvParser.parse(readString(path)));
		} catch (IOException e) {
			LOG.warning("IO Exception occurred while retrieving flashcards from file.");
		}
		return new CollectionImp(path.getFileName().toString(), flashcards);
	}
}
