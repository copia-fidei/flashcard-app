package flashcard.app.result.boundary;

import javax.ws.rs.core.Response;
import java.net.URI;

import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.*;

public class ResultRedirect {

	private static final String PATH_TO_RESULT_PAGE = "/flashcards/result";

	/**
	 * Creates a redirect response to the result page with the given title and description
	 * @param title The title
	 * @param description The description
	 * @return A Response object that will redirect to the result page
	 */
	public static Response toResultPage(String title, String description) {
		return Response
				.seeOther(URI.create(PATH_TO_RESULT_PAGE + "?title=" + encode(title, UTF_8) + "&description=" + encode(description, UTF_8))).build();

	}

	/**
	 * Creates a redirect response to the result page with the given title and exception message
	 * @param title The title
	 * @param e The exception containing the description
	 * @return A Response object that will redirect to the result page
	 */
	public static Response toResultPage(String title, Exception e) {
		return toResultPage(title, e.getMessage());
	}
}
