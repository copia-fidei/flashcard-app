package flashcard.app.collections.entity;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Collection {

	private String name;

	private List<Flashcard> flashcards = new ArrayList<>();

	public Collection(final String name) { this.name = name; }

	public Collection(final String name, final List<Flashcard> flashcards) {
		this.name = name;
		this.flashcards.addAll(flashcards);
	}

	public String Name() {
		return name;
	}

	public String EncodedName() {
		return URLEncoder.encode(name, StandardCharsets.UTF_8);
	}

	public List<Flashcard> Flashcards() {
		return flashcards;
	}

	@Override
	public String toString() {
		return EncodedName();
	}
}
