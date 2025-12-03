package flashcard.app.collection.boundary;

import flashcard.app.collection.entity.Collection;
import flashcard.app.result.boundary.ResultRedirect;
import flashcard.app.Css;
import flashcard.app.collections.entity.*;
import org.glassfish.jersey.server.mvc.Viewable;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Path("/collections/{collection}")
public class CollectionResource {

	@Inject
	private Collections collections;

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response collection(@PathParam("collection") String name) {
		final var optCollection = collections.getCollection(name);
		if (optCollection.isEmpty()) {
			return ResultRedirect.toResultPage("Collection does not exist", "Collection " + name + " does not exist.");
		}

		final var collection = optCollection.get();
		final var data = Map.of(
				"collectionName", collection.Name(),
				"stylesheet", Css.SHARED,
				"flashcards", TableRow.getRows(collection));

		return Response.ok(new Viewable("/collection.mustache", data)).build();
	}


	@DELETE
	@Produces(MediaType.TEXT_HTML)
	public Response deleteCollection(@PathParam("collection") String name) {
		if (collections.removeCollection(name)) {
			return redirectToSuccessPage(name);
		} else {
			return redirectToErrorPage(new IOException("Collection not found: " + name));
		}
	}

	private Response redirectToErrorPage(IOException e) {
		return ResultRedirect.toResultPage("Error deleting collection", e);
	}

	private Response redirectToSuccessPage(String collectionName) {
		return ResultRedirect.toResultPage("Collection successfully deleted", "Collection " + collectionName + "has been successfully deleted.");
	}
}
