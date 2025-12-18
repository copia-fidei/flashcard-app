package flashcard.app.collection.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CollectionInMemory implements Collection {

	private final String          name;
	private final List<Flashcard> flashcards = new ArrayList<>();

	public CollectionInMemory(final String name) {
		this(name, List.of());
	}

	public CollectionInMemory(final String name, final List<Flashcard> flashcards) {
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
	public void delete() throws CouldNotBeDeleted {
		throw new CouldNotBeDeleted("Collection only exists in memory. Changes are not persisted.");
	}

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof final CollectionInMemory that)) return false;
		return Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name);
	}

	@Override
	public String toString() {
		return EncodedName();
	}
}
