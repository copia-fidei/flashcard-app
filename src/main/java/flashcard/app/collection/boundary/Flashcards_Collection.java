package flashcard.app.collection.boundary;

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


@Path("/collections/{collection}")
public class Flashcards_Collection {

	@Inject
	private Collections collections;


	@GET
	@Produces(MediaType.TEXT_HTML)
	public String collection(@PathParam("collection") String name) {
		try (InputStream res = getClass().getClassLoader().getResourceAsStream("/html_templates/flashcards_collection.html")) {
			if (res == null) {
				throw new IOException("Template not found");
			}
			String htmlTemplate = new String(res.readAllBytes(), UTF_8);

			final Optional<Collection> optCollection = collections.getCollection(name);
			if (optCollection.isPresent()) {
				final var collection = optCollection.get();
				final var tableRows = new StringBuilder(lineSeparator());
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

				return htmlTemplate
						.replace("$collectionName", collection.Name())
						.replace("$flashcards", tableRows)
						.replace("$stylesheet", Shared.CSS);
			}
			else {
				// TODO replace with error Does not exist.
				return name + System.lineSeparator().repeat(2) + URLDecoder.decode(name) + lineSeparator().repeat(2) + collections.List().get(1);
			}

		} catch (Exception e) {
			return new StackTrace(e).toString();
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
		final String errorMessage = "Error deleting collection: " + e.getMessage();
		final String encodedErrorMessage = URLEncoder.encode(errorMessage, UTF_8);
		return Response.seeOther(URI.create("/flashcards/delete-failure?message=" + encodedErrorMessage)).build();
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
