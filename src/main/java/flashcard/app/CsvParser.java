package flashcard.app;

import flashcard.app.collection.entity.Flashcard;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class CsvParser {

	public List<Flashcard> parse(String csvContent) {
		return csvContent.lines()
				.map(line -> {
					final String[] split = line.split(",");
					return new Flashcard(split[0], split[1]);
				})
				.collect(toList());
	}
}