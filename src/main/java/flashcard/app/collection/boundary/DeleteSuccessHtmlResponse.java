package flashcard.app.collection.boundary;

import flashcard.app.HtmlResponse;

import javax.ws.rs.core.Response;

import static java.util.Objects.requireNonNullElse;

public class DeleteSuccessHtmlResponse extends HtmlResponse {

	private static final String COLLECTION_NOT_PROVIDED = "No collection name provided";

	private static final String HTML_FILE_LOCATION = "/html_templates/delete-success.html";

	private final String collectionName;

	public DeleteSuccessHtmlResponse(final String collectionName) {
		super (HTML_FILE_LOCATION);

		this.collectionName = collectionName;
	}

	@Override
	public Response ResponseWithAvailableHtmlTemplate(String htmlTemplate) {
		return Response.ok(htmlTemplate
				.replace("${collectionName}", requireNonNullElse(collectionName, COLLECTION_NOT_PROVIDED)))
		.build();
	}
}
