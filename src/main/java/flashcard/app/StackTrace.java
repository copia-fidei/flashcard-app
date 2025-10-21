package flashcard.app;

import static java.lang.System.lineSeparator;

/**
 * A stacktrace as a string. For debugging. Looks similar but not the same to ex.printStackTrace().
 */
public class StackTrace {

	private final Exception exception;

	public StackTrace(final Exception exception) { this.exception = exception; }

	@Override
	public String toString() {
		final var sb = new StringBuilder();
		final var stackTrace = exception.getStackTrace();
		for (var el : stackTrace) {
			sb.append(lineSeparator());
			sb.append(el);
		}
		return sb.toString();
	}
}
