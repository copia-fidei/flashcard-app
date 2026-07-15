package flashcard.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

public class Shared {
	private static final Logger LOG = getLogger(Shared.class.getName());

	private static final String LOCATION = "/styles.css";

	public static final String CSS = init();

	private static String init() {
		String css = "";
		try (InputStream is = Shared.class.getResourceAsStream(LOCATION)) {
			if (is != null)
				css = new String(is.readAllBytes());
			else
				LOG.warning("CSS file not found at: " + LOCATION);
		} catch (IOException e) {
			LOG.warning(e.getMessage());
		}
		return css;
	}
}