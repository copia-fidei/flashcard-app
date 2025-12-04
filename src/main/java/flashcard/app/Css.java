package flashcard.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class Css {
	private static final Logger LOG = Logger.getLogger(Css.class.getName());

	private static final String LOCATION = "/styles.css";

	public static final String SHARED = init();

	private static String init() {
		String css = "";
		try (InputStream is = Css.class.getResourceAsStream(LOCATION)) {
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