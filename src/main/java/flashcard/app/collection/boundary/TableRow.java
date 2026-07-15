package flashcard.app.collection.boundary;

import flashcard.app.collection.entity.Collection;
import flashcard.app.collection.entity.Flashcard;

import java.util.ArrayList;
import java.util.List;

record TableRow(int index, String front, String back) {

	TableRow(int index, Flashcard card) {
		this(index, card.Front(), card.Back());
	}

	static List<TableRow> getRows(Collection collection) {
		var flashcards = collection.Flashcards();
		var rows = new ArrayList<TableRow>();
		for (int i = 0; i < flashcards.size(); i++) {
			rows.add(new TableRow(i, flashcards.get(i)));
		}
		return rows;
	}
}
