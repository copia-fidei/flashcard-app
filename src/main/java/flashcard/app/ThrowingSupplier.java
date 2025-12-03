package flashcard.app;

import java.util.function.Supplier;

@FunctionalInterface
public interface ThrowingSupplier<T, X extends Exception> {

	T get() throws X;
}
