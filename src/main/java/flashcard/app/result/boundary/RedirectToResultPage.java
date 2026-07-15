package flashcard.app.result.boundary;

import javax.ws.rs.core.Response;
import java.net.URI;

import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.ws.rs.core.Response.seeOther;

public record RedirectToResultPage(String title, String description) {

	private static final String PATH_TO_RESULT_PAGE = "/flashcards/result";

	public RedirectToResultPage(String title, Exception exception) {
		this(title, exception.getMessage());
	}

	public Response direct() {
		return seeOther(URI.create(PATH_TO_RESULT_PAGE + "?title=" + _encode(title) + "&description=" + _encode(description))).build();
	}

	private static String _encode(String string) {
		return encode(string, UTF_8);
	}
}
