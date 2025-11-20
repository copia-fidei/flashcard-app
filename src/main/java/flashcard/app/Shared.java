package flashcard.app;

import flashcard.app.collection.entity.CsvFileCollection;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Shared {
	private static Logger LOG = Logger.getLogger(Shared.class.getName());

	private static final String LOCATION = "/html_templates/styles.css";

	public static String CSS = init();

	private static String init() {
		try (InputStream is = Shared.class.getResourceAsStream(LOCATION)) {
			if (is == null) {
				LOG.warning("CSS file not found: " + LOCATION);
				return "";
			}
			return new String(is.readAllBytes(), UTF_8);
		} catch (IOException ignored) {
			return "";
		}
	}
}