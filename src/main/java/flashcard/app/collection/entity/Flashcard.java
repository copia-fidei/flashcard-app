package flashcard.app.collection.entity;

import static java.lang.System.lineSeparator;

public class Flashcard {

	private final String front;
	private final String back;

	public Flashcard(final String front, final String back) {
		this.front = front;
		this.back = back;
	}

	/**
	 * @return the front of the card
	 */
	public String Front() {
		return front;
	}

	/**
	 * @return the back of the card
	 */
	public String Back() {
		return back;
	}

	@Override
	public String toString() {
		return "Front: " + front + lineSeparator() +
				"Back: " + back;
	}
}
