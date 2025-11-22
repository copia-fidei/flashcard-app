package flashcard.app.collection.boundary;

import flashcard.app.HtmlResponse;

import javax.ws.rs.core.Response;

import static java.util.Objects.requireNonNullElse;

public class DeleteFailureHtmlResponse extends HtmlResponse {

	private static final String NO_ERROR_MESSAGE_PROVIDED = "Error message has not been provided.";

	private static final String HTML_FILE_LOCATION = "/html_templates/delete-failure.html";

	private final String errorMessage;

	public DeleteFailureHtmlResponse(final String errorMessage) {
		super (HTML_FILE_LOCATION);

		this.errorMessage = errorMessage;
	}

	@Override
	public Response ResponseWithAvailableHtmlTemplate(String htmlTemplate) {
		return Response.ok(htmlTemplate.replace(
									  "${errorMessage}",
									  requireNonNullElse(errorMessage, NO_ERROR_MESSAGE_PROVIDED)))
							  .build();
	}
}
