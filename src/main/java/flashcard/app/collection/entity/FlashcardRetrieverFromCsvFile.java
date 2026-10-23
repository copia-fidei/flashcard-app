package flashcard.app.collection.entity;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.nio.file.Path;
import java.util.List;

public class FlashcardRetrieverFromCsvFile implements FlashcardRetriever {

	private final Path csvFile;

	public FlashcardRetrieverFromCsvFile(Path csvFile) {
		this.csvFile = csvFile;
	}

	@Override
	public List<Flashcard> get() throws CouldNotRetrieve {
		var settings = new CsvParserSettings();
		settings.detectFormatAutomatically();
		return new CsvParser(settings).parseAll(csvFile.toFile()).stream().map(Flashcard::new).toList();
	}
}