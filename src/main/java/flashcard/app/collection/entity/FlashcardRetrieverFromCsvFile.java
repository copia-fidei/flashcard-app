package flashcard.app.collection.entity;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FlashcardRetrieverFromCsvFile implements FlashcardRetriever {

	private final String csv;

	public FlashcardRetrieverFromCsvFile(InputStream csv) throws IOException {
		this(csv.readAllBytes());
	}

	public FlashcardRetrieverFromCsvFile(byte[] csv) {
		this(new String(csv));
	}

	public FlashcardRetrieverFromCsvFile(String csv) { this.csv = csv; }

	@Override
	public List<Flashcard> get() {
		return csv.lines().map(line -> {
			String[] split = line.split(",");
			return new Flashcard(split[0], split[1]);
		}).toList();
	}
}