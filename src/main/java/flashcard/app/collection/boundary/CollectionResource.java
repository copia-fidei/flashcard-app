package flashcard.app.collection.boundary;

import flashcard.app.collection.entity.Collection;
import flashcard.app.result.boundary.RedirectToResultPage;
import flashcard.app.collections.entity.*;
import org.glassfish.jersey.server.mvc.Viewable;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static flashcard.app.Shared.CSS;
import static flashcard.app.collection.boundary.TableRow.getRows;
import static java.util.Map.of;
import static javax.ws.rs.core.Response.ok;


@Path("/collections/{collection}")
public class CollectionResource {

	@Inject
	private Collections collections;

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response showCollection(@PathParam("collection") String name) {
		Optional<Collection> optCollection = collections.getCollection(name);
		if (optCollection.isEmpty()) {
			return new RedirectToResultPage("Collection does not exist", "Collection " + name + " does not exist.").direct();
		}

		var collection = optCollection.get();
		var data = of("collectionName", collection.Name(),
					  "stylesheet", 	CSS,
				      "flashcards", 	getRows(collection));

		return ok(new Viewable("/collection.mustache", data)).build();
	}


	@DELETE
	@Produces(MediaType.TEXT_HTML)
	public Response deleteCollection(@PathParam("collection") String name) {
		Optional<Collection> optCollection = collections.getCollection(name);
		if (optCollection.isEmpty()) {
			return redirectToErrorPage("Collection '" + name + "' not found.");
		}
		try {
			var collection = optCollection.get();
			collection.delete();
			collections.List().remove(collection);
			return redirectToSuccessPage(name);
		} catch (Collection.CouldNotBeDeleted e) {
			return redirectToErrorPage(e);
		}
	}

	private static final String ERROR_DELETING_COLLECTION = "Error deleting collection";

	private Response redirectToErrorPage(Exception message) {
		return new RedirectToResultPage(ERROR_DELETING_COLLECTION, message).direct();
	}

	private Response redirectToErrorPage(String message) {
		return new RedirectToResultPage(ERROR_DELETING_COLLECTION, message).direct();
	}

	private Response redirectToSuccessPage(String collectionName) {
		return new RedirectToResultPage("Collection successfully deleted", "Collection " + collectionName + "has been successfully deleted.").direct();
	}
}
