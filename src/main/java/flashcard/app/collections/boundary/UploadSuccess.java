// File: src/main/java/flashcard/app/collections/boundary/UploadSuccess.java
package flashcard.app.collections.boundary;

import flashcard.app.HtmlTemplate;
import flashcard.app.collections.entity.Collections;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;

@Path("upload-success")
public class UploadSuccess {

    private static final String PAGE_LOAD_ERROR = "Page could not be loaded";
    private static final String COLLECTION_NOT_FOUND = "Collection not found";
    private static final String NO_COLLECTION_SPECIFIED = "No collection specified";

    @Inject
    private Collections collections;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response page(@QueryParam("collection") String collectionName) {
		// TODO this behaviour has to be specified.
        if (collectionName == null || collectionName.isEmpty()) {
            return status(Response.Status.BAD_REQUEST).entity(NO_COLLECTION_SPECIFIED).build();
        }

		// TODO what to do here?
		final var collection = collections.getCollection(collectionName);
        if (collection.isEmpty()) {
            return status(Response.Status.NOT_FOUND).entity(COLLECTION_NOT_FOUND).build();
        }

		return new HtmlTemplate("/html_templates/upload-success.html")
				.get()
				.map(template -> template
						.replace("${collectionName}", collectionName)
						.replace("${cardCount}", String.valueOf(collection.get().Flashcards().size())))
				.map(ok()::entity)
				.orElse(status(INTERNAL_SERVER_ERROR).entity(PAGE_LOAD_ERROR))
				.build();
    }
}