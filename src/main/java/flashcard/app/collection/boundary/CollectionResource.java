package flashcard.app.collection.boundary;

import flashcard.app.result.boundary.ResultRedirect;
import flashcard.app.Shared;
import flashcard.app.collection.entity.Flashcard;
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
		System.out.println(name);
		final var optCollection = collections.getCollection(name);
		if (optCollection.isPresent()) {
			final var collection = optCollection.get();
			class Row {
				int index;
				String front;
				String back;
				Row(int index, String front, String back) {
					this.index = index;
					this.front = front;
					this.back = back;
				}
			}
			final List<Flashcard> flashcards = collection.Flashcards();
			final List<Row> rows = new ArrayList<>();
			for (int i = 0; i < flashcards.size(); i++) {
				final Flashcard flashcard = flashcards.get(i);
				rows.add(new Row(i, flashcard.Front(), flashcard.Back()));
			}
			var data = Map.of(
					"collectionName", collection.Name(),
					"stylesheet", Shared.CSS,
					"flashcards", rows);

			return Response.ok(new Viewable("/collection.mustache", data)).build();
		}
		else {
			return ResultRedirect.toResultPage("Collection does not exist", "Collection " + name + " does not exist.");
		}


//		return new CollectionHtmlResponse(collections, name).Response();
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
