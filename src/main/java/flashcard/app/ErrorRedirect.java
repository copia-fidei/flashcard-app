package flashcard.app;

import javax.ws.rs.core.Response;
import java.net.URI;

import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.*;

public class ErrorRedirect {

	private static final String PATH_TO_ERROR_PAGE = "/flashcards/error";

	/**
	 * Creates a redirect response to the error page with the given title and description
	 * @param title The error title
	 * @param description The error description
	 * @return A Response object that will redirect to the error page
	 */
	public static Response toErrorPage(String title, String description) {
		return Response
				.seeOther(URI.create(PATH_TO_ERROR_PAGE + "?title=" + encode(title, UTF_8) + "&description=" + encode(description, UTF_8))).build();

	}

	/**
	 * Creates a redirect response to the error page with the given title and exception message
	 * @param title The error title
	 * @param e The exception containing the error description
	 * @return A Response object that will redirect to the error page
	 */
	public static Response toErrorPage(String title, Exception e) {
		return toErrorPage(title, e.getMessage());
	}
}
