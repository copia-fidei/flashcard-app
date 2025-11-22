package flashcard.app.collection.boundary;

import flashcard.app.ErrorRedirect;
import flashcard.app.HtmlTemplateLoader;
import flashcard.app.Shared;
import flashcard.app.StackTrace;
import flashcard.app.collection.entity.Collection;
import flashcard.app.collections.entity.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Optional;

import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.status;


@Path("/collections/{collection}")
public class CollectionResource {

	@Inject
	private Collections collections;

	private static final String PAGE_LOAD_ERROR           = "Page could not be loaded";

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response collection(@PathParam("collection") String name) {
		final Optional<Collection> optCollection = collections.getCollection(name);
		final var tableRows = new StringBuilder(lineSeparator());
		if (optCollection.isPresent()) {
			final var collection = optCollection.get();
			int index = 1;
			for (var flashcard : collection.Flashcards()) {
				tableRows
						.append("<tr>")
							.append("<td>")
								.append(index++)
							.append("</td>")
							.append("<td>")
								.append(escapeHtml(flashcard.Front()))
						.append("</td>")
							.append("<td>")
								.append(escapeHtml(flashcard.Back()))
							.append("</td>")
						.append("</tr>")
						.append(lineSeparator());
			}
		}
		else {
			// TODO replace with error Does not exist.
			// TODO which is the best error status code?
			return status(INTERNAL_SERVER_ERROR)
					.entity(name + lineSeparator().repeat(2) + URLDecoder.decode(name) + lineSeparator().repeat(2) + collections.List().get(1))
			.build();
		}
		var htmlTemplateLoader = new HtmlTemplateLoader("/html_templates/flashcards_collection.html");
		try {
			final String htmlTemplate = htmlTemplateLoader.loadHtml();
			return Response.ok(htmlTemplate
					.replace("$collectionName", name)
					.replace("$flashcards", tableRows)
					.replace("$stylesheet", Shared.CSS))
			.build();
		} catch (IOException e) {
			return status(INTERNAL_SERVER_ERROR).entity(PAGE_LOAD_ERROR).build();

		}
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
		return ErrorRedirect.toErrorPage("Error deleting collection", e);
	}

	private Response redirectToSuccessPage(String collectionName) {
		final String encodedName = URLEncoder.encode(collectionName, UTF_8);
		return Response.seeOther(URI.create("/flashcards/delete-success?collection=" + encodedName)).build();
	}

	private String escapeHtml(String input) {
		return input.replace("&", "&amp;")
					.replace("<", "&lt;")
					.replace(">", "&gt;")
					.replace("\"", "&quot;")
					.replace("'", "&#39;");
	}

}
