package flashcard.app.collections.entity;

import flashcard.app.collection.entity.CsvParser;
import flashcard.app.collection.entity.Collection;
import flashcard.app.collection.entity.CollectionImp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

public class CsvFileSource implements CollectionsSource {

	private static final Logger LOG = Logger.getLogger(CsvFileSource.class.getName());

	private final Path csvPath;

	public CsvFileSource(Path csvPath) {
		this.csvPath = csvPath;
	}

	@Override
	public List<Collection> get() {
		final String csvContent;
		try {
			csvContent = Files.readString(csvPath);
		} catch (IOException e) {
			LOG.warning("CSV file " + csvPath + " could be read: " + e.getMessage());

			return List.of();
		}
		return List.of(new CollectionImp(csvPath.getFileName().toString(), new CsvParser(csvContent).get()));
	}
}
