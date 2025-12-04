package flashcard.app;

@FunctionalInterface
public interface ThrowingSupplier<T, X extends Exception> {

	T get() throws X;
}
