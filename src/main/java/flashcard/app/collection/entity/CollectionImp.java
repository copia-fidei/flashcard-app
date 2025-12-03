package flashcard.app.collection.entity;

import java.util.ArrayList;
import java.util.List;

public class CollectionImp implements Collection {

	private final String name;

	private final List<Flashcard> flashcards = new ArrayList<>();

	public CollectionImp(final String name) { this.name = name; }

	public CollectionImp(final String name, final List<Flashcard> flashcards) {
		this.name = name;
		this.flashcards.addAll(flashcards);
	}

	@Override
	public String Name() {
		return name;
	}

	@Override
	public List<Flashcard> Flashcards() {
		return flashcards;
	}

	@Override
	public void save() {}

	@Override
	public String toString() {
		return EncodedName();
	}
}
