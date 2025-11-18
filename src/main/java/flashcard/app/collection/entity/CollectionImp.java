package flashcard.app.collection.entity;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class CollectionImp implements Collection {

	private String name;

	private List<Flashcard> flashcards = new ArrayList<>();

	public CollectionImp(final String name) { this.name = name; }

	public CollectionImp(final String name, final List<Flashcard> flashcards) {
		this.name = name;
		this.flashcards.addAll(flashcards);
	}

	@Override
	public String Name() {
//		// TODO, remove this distinction, just use file name
//		final int endIndex = name.lastIndexOf('.');
//		if (endIndex != -1)
//				return name.substring(0, endIndex);
		return name;
	}


	@Override
	public List<Flashcard> Flashcards() {
		return flashcards;
	}

	@Override
	public String toString() {
		return EncodedName();
	}
}
