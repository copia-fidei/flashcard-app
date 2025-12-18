package flashcard.app.collections.boundary;

import flashcard.app.Css;
import flashcard.app.Extensions;
import flashcard.app.PostgresqlDbFromKaraf;
import flashcard.app.collection.entity.Collection;
import flashcard.app.collections.entity.*;
import flashcard.app.result.boundary.ResultRedirect;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.server.mvc.Template;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Path("collections")
public class CollectionsResource {

	private static final PostgresqlDbFromKaraf DATABASE = new PostgresqlDbFromKaraf();

	@Inject
    private Collections collections;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response importFile(
            @FormDataParam("file") InputStream uploadedFile,
            @FormDataParam("file") FormDataContentDisposition fileInfo
	) {

		final String fileName = fileInfo.getFileName();

		if (Extensions.hasNoneOf(fileName, ".csv", ".apkg")) {
			return redirectToErrorPage("File type is not supported");
		}
		AlreadyExists check = new AlreadyExistsInDatabase(DATABASE);
		// If you want check if the file was saved on the server instead:
		//AlreadyExists check = new FileAlreadyExists(COLLECTIONS_DIR);
		try {
			if (check.alreadyExists(fileName)) {
				return redirectToErrorPage("Collection " + fileName + " already exists");
			}
		} catch (AlreadyExists.CouldNotBeDetermined e) {
			return redirectToErrorPage(e.getMessage());
		}

		final Collection newCollection;
		try {
			newCollection = new DatabaseImporter(DATABASE, fileName, uploadedFile).importCollection();
			// If you want to use the file importer instead:
			//newCollection = new FileImporter(COLLECTIONS_DIR, fileName, uploadedFile).importCollection();
		} catch (Importer.CouldNotImport e) {
			return redirectToErrorPage(e.getMessage());
		}
		collections.List().add(newCollection);
		return redirectToSuccesPage(newCollection);
	}

	private static Response redirectToErrorPage(final String message) {
		return ResultRedirect.toResultPage("Import failed", message);
	}

	private static Response redirectToSuccesPage(final Collection collection) {
		return ResultRedirect.toResultPage("Import successful", "Collection " + collection.Name() + " has been imported successfully.");
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	@Template(name = "/collections.mustache")
	public Map<String, Object> listCollections() {
		// Needed workaround because otherwise the property EncodedName is not read by mustache.
		final List<Map<String, String>> collectionsAsMap = collections.List().stream()
				.map(collection -> Map.of(
						"Name", collection.Name(),
						"EncodedName", collection.EncodedName()
				))
				.toList();

		return Map.of(
				"stylesheet", Css.SHARED,
				"collections", collectionsAsMap);
	}


}