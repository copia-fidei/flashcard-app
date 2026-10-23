package flashcard.app.collection.entity;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;

public class FlashcardRetrieverFromCsv implements FlashcardRetriever {

	private final String csv;

	public FlashcardRetrieverFromCsv(InputStream csv) throws IOException {
		this(csv.readAllBytes());
	}

	public FlashcardRetrieverFromCsv(byte[] csv) {
		this(new String(csv));
	}

	public FlashcardRetrieverFromCsv(String csv) { this.csv = csv; }

	@Override
	public List<Flashcard> get() throws CouldNotRetrieve {
		var settings = new CsvParserSettings();
		settings.detectFormatAutomatically();
		return new CsvParser(settings).parseAll(new StringReader(csv)).stream().map(Flashcard::new).toList();
	}
}