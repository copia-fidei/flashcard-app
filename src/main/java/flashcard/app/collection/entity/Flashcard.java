package flashcard.app.collection.entity;

public class Flashcard {

	private final String front;
	private final String back;

	public Flashcard(final String front, final String back) {
		this.front = front;
		this.back = back;
	}

	public String Back() {
		return back;
	}

	public String Front() {
		return front;
	}
}
