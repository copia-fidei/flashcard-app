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

	/**
	 * Removes the ending if it is a file name.
	 *
	 * @return
	 */
	public String Name() {
//		// TODO, remove this distinction, just use file name
//		final int endIndex = name.lastIndexOf('.');
//		if (endIndex != -1)
//				return name.substring(0, endIndex);
		return name;
	}

	public String EncodedName() {
		return URLEncoder.encode(Name(), StandardCharsets.UTF_8);
	}

	/**
	 * Removes the ending if it is a file name.
	 *
	 * @return
	 */
	public List<Flashcard> Flashcards() {
		return flashcards;
	}

	@Override
	public String toString() {
		return EncodedName();
	}
}
