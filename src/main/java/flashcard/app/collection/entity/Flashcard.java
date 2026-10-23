package flashcard.app.collection.entity;

import java.util.Objects;

import static java.util.Comparator.comparing;
import static java.util.Objects.hash;

public class Flashcard implements Comparable<Flashcard> {

	private final String front;
	private final String back;

	public Flashcard(String front, String back) {
		this.front = front;
		this.back  = back;
	}

	public Flashcard(String[] row) {
		this.front = row[0];
		this.back  = row[1];
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
	public boolean equals(Object o) {
		if (!(o instanceof final Flashcard flashcard)) return false;
		return Objects.equals(front, flashcard.front) && Objects.equals(back, flashcard.back);
	}

	@Override
	public int hashCode() {
		return hash(front, back);
	}

	@Override
	public String toString() {
		return "Front: " + front + ", " + "Back: " + back;
	}

	@Override
	public int compareTo(Flashcard o) {
		return comparing(Flashcard::Front).thenComparing(Flashcard::Back).compare(this, o);
	}
}
