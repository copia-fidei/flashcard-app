package flashcard.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

// TODO does shared CSS work with Jersey MCV templates
public class Css {
	private static final Logger LOG = Logger.getLogger(Css.class.getName());

	private static final String LOCATION = "/styles.css";

	public static final String SHARED = init();

	private static String init() {
		try (InputStream is = Css.class.getResourceAsStream(LOCATION)) {
			if (is == null) {
				LOG.warning("CSS file not found: " + LOCATION);
				return "";
			}
			return new String(is.readAllBytes());

		} catch (IOException e) {
			LOG.warning(e.getMessage());

			return "";
		}
	}
}