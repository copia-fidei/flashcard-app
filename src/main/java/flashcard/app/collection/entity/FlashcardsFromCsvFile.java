package flashcard.app.collection.entity;

import java.util.List;

public class FlashcardsFromCsvFile implements RetrievedFlashcards {

	private final String csvContent;

	public FlashcardsFromCsvFile(final String csvContent) { this.csvContent = csvContent; }

	@Override
	public List<Flashcard> get() {
		return csvContent.lines().map(line -> {
			String[] split = line.split(",");
			return new Flashcard(split[0], split[1]);
		}).toList();
	}
}