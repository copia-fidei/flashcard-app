package flashcard.app.collections.boundary;

import flashcard.app.HtmlResponse;
import flashcard.app.collections.entity.Collections;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import static java.util.Objects.requireNonNullElse;

public class UploadSuccessHtmlResponse extends HtmlResponse {
    private static final String COLLECTION_NOT_FOUND = "Collection not found";
    private static final String NO_COLLECTION_SPECIFIED = "No collection specified";

    private static final String HTML_FILE_LOCATION = "/html_templates/upload-success.html";

	private final Collections collections;
	private final String collectionName;

    public UploadSuccessHtmlResponse(Collections collections, String collectionName) {
        super(HTML_FILE_LOCATION);

		this.collections = collections;
		this.collectionName = collectionName;
	}

    @Override
    public Response ResponseWithAvailableHtmlTemplate(String htmlTemplate) {
		// TODO this behaviour has to be specified.
		System.out.println(collections);
		if (collectionName == null || collectionName.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(NO_COLLECTION_SPECIFIED).build();
        }

		// TODO what to do here?
		final var collection = collections.getCollection(collectionName);
        if (collection.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity(COLLECTION_NOT_FOUND).build();
        }

        return Response.ok(htmlTemplate
                .replace("${collectionName}", collectionName)
                .replace("${cardCount}", String.valueOf(collection.get().Flashcards().size())))
            .build();
    }
}