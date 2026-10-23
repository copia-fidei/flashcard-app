package flashcard.app.collection.entity;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRecord;

import java.io.IOException;
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

	private List<Flashcard> getFlashcardsWithFastCsvLibrary() throws CouldNotRetrieve {
		try(CsvReader<CsvRecord> reader = CsvReader.builder().ofCsvRecord(csvFile)) {
			return reader.stream().map(csvRecord -> new Flashcard(csvRecord.getField(0), csvRecord.getField(1))).toList();
		} catch (IOException e) {
			throw new CouldNotRetrieve(e);
		}
	}
}