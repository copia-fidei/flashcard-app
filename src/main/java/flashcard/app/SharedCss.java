package flashcard.app;

import java.io.IOException;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class SharedCss {

	private static final String LOCATION = "/html_templates/styles.css";

	private static String cache = null;

	public static String get() throws IOException {
		if (cache == null) {
			try (InputStream is = SharedCss.class.getResourceAsStream(LOCATION)) {
				if (is == null) {
					throw new IOException("CSS file not found: " + LOCATION);
				}
				cache = new String(is.readAllBytes(), UTF_8);
			}
		}
		return cache;
	}
}