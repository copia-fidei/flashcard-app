package flashcard.app;

import java.io.FileNotFoundException;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HtmlTemplateLoader {

	private final String location;

	public HtmlTemplateLoader(final String location) {
		this.location = location;
	}

	public String loadHtml() throws IOException {
		try (var inputStream = getClass().getClassLoader().getResourceAsStream(location)) {
			if (inputStream == null) {
				throw new FileNotFoundException("Template not found: " + location);
			}
			return new String(inputStream.readAllBytes(), UTF_8);
		}
	}
}
