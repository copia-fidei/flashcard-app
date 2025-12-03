package flashcard.app.collection.entity;

import java.util.List;

public class CsvParser implements FlashcardRetriever {

	private String csvContent;

	public CsvParser(final String csvContent) { this.csvContent = csvContent; }

	@Override
	public List<Flashcard> get() {
		return csvContent.lines().map(line -> {
			final String[] split = line.split(",");
			return new Flashcard(split[0], split[1]);
		}).toList();
	}
}