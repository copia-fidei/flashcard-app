package flashcard.app;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Supplier;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HtmlTemplate implements Supplier<Optional<String>> {

	private final String location;

	public HtmlTemplate(final String location) {
		this.location = location;
	}

	private final ClassLoader classLoader = getClass().getClassLoader();

	@Override
	public Optional<String> get() {
		try (var inputStream = classLoader.getResourceAsStream(location)) {
			return inputStream == null ? Optional.empty() : Optional.of(new String(inputStream.readAllBytes(), UTF_8));
		}
		catch (IOException e) {
			return Optional.empty();
		}

	}
//		} catch (IOException e) {
//			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//						   .entity("Error loading delete failure page: " + e.getMessage())
//						   .build();
}
