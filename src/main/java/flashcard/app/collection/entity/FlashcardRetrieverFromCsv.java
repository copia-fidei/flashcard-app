package flashcard.app.collection.entity;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;

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

	private List<Flashcard> getFlashcardsWithFastCsvLibrary() throws CouldNotRetrieve {
		Optional<String> optLastLine = csv.lines().reduce((a, b) -> b);
		if (optLastLine.isEmpty()) {
			return List.of();
		}
		char fieldSeparator = determineFieldSeparator(optLastLine.get());

		try(CsvReader<CsvRecord> reader = CsvReader.builder().fieldSeparator(fieldSeparator).ofCsvRecord(csv)) {
			return reader.stream().map(csvRecord -> new Flashcard(csvRecord.getField(0), csvRecord.getField(1))).toList();
		} catch (IOException e) {
			throw new CouldNotRetrieve(e);
		}
	}

	// brittle
	private static char determineFieldSeparator(String lastLine) {
		char comma              = ',';
		int  amountOfCommas     = 0;
		char semicolon          = ';';
		int  amountOfSemicolons = 0;

		for (int i = 0; i < lastLine.length(); i++) {
			if (lastLine.charAt(i) == comma) {
				amountOfCommas++;
			}
			if (lastLine.charAt(i) == semicolon) {
				amountOfSemicolons++;
			}
		}
		char delimiter;
		if (amountOfSemicolons > amountOfCommas) {
			delimiter = semicolon;
		} else {
			delimiter = comma;
		}
		return delimiter;
	}
}