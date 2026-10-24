package flashcard.app;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class ExampleApkgFile {

	private final static String NAME = "US_Presidents_w_pics.apkg";

	public InputStream InputStream() {
		return getClass().getResourceAsStream("/" + NAME);
	}

	public Path Path() throws URISyntaxException, NullPointerException {
		//noinspection DataFlowIssue
		return Path.of(getClass().getResource("/" + NAME).toURI());
	}

	public String name() {
		return NAME;
	}
}
