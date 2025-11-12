package flashcard.app;

import java.io.IOException;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Css {

	private static final String CSS_LOCATION = "/html_templates/styles.css";

	private static String cache = null;

	public static String get() throws IOException {
		if (cache == null) {
			try (InputStream is = Css.class.getResourceAsStream(CSS_LOCATION)) {
				if (is == null) {
					throw new IOException("CSS file not found: " + CSS_LOCATION);
				}
				cache = new String(is.readAllBytes(), UTF_8);
			}
		}
		return cache;
	}
}