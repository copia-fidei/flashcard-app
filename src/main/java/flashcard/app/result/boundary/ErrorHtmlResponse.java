package flashcard.app.result.boundary;

import flashcard.app.HtmlResponse;

import javax.ws.rs.core.Response;

import static java.util.Objects.requireNonNullElse;

public class ErrorHtmlResponse extends HtmlResponse {

	private static final String NO_ERROR_TITLE_PROVIDED = "Error title has not been provided.";
	private static final String NO_ERROR_DESCRIPTION_PROVIDED = "Error description has not been provided.";

	private static final String HTML_FILE_LOCATION = "/html_templates/error.html";

	private final String title;
	private final String description;

	public ErrorHtmlResponse(final String title, final String description) {
		super (HTML_FILE_LOCATION);

		this.title = title;
		this.description = description;
	}

	@Override
	public Response ResponseWithAvailableHtmlTemplate(String htmlTemplate) {
		return Response.ok(htmlTemplate
							   .replace("${errorTitle}", 		 requireNonNullElse(title, NO_ERROR_TITLE_PROVIDED))
							   .replace("${errorDescription}", requireNonNullElse(description, NO_ERROR_DESCRIPTION_PROVIDED)))
					   .build();
	}
}
