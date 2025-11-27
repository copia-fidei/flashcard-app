package flashcard.app.collections.boundary;

import flashcard.app.*;
import flashcard.app.collection.entity.Collection;
import flashcard.app.collection.entity.CollectionImp;
import flashcard.app.collections.entity.Collections;
import flashcard.app.result.boundary.ResultRedirect;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.server.mvc.Template;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.util.stream.Collectors.toList;

@Path("collections")
public class CollectionsResource {

	@Inject
    private Collections collections;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedFile,
            @FormDataParam("file") FormDataContentDisposition fileInfo) {

		try (uploadedFile) {
			try {
				final var fileContent   = uploadedFile.readAllBytes();
				final var fileName      = fileInfo.getFileName();
				final var csvParser     = new CsvParser();
				final var flashcards    = csvParser.parse(new String(fileContent, UTF_8));
				final var newCollection = new CollectionImp(fileName, flashcards);
				// how to deal with exceptions?
				Files.write(Configuration.COLLECTIONS_DIR.resolve(fileName), fileContent, CREATE);
				collections.List().add(newCollection);

				return redirectToSuccesPage(newCollection);
			} catch (IOException e) {
				return redirectToErrorPage(e);
			}
		} catch (IOException e) {
			return redirectToErrorPage(e);
		}
	}


	private static Response redirectToErrorPage(final IOException e) {
		return ResultRedirect.toResultPage("Import failed", e);
	}

	private static Response redirectToSuccesPage(final Collection collection) {
		return ResultRedirect.toResultPage("Import successful", "Collection " + collection.Name() + " has been successfully imported.");
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	@Template(name = "/collections.mustache")
	public Map<String, Object> view() {
		// Needed workaround because otherwise the property EncodedName is not read by mustache.
		final List<Map<String, String>> collectionsAsMap = collections.List().stream()
				.map(collection -> Map.of(
						"Name", collection.Name(), "EncodedName", collection.EncodedName()  // explicitly include EncodedName
				))
				.collect(toList());

		final var model = Map.of(
				"stylesheet", Shared.CSS,
				"collections", collectionsAsMap);
		return model;
	}


}