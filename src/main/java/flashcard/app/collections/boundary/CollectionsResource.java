package flashcard.app.collections.boundary;

import flashcard.app.Db;
import flashcard.app.Extensions;
import flashcard.app.PostgresqlDbFromKaraf;
import flashcard.app.collection.entity.Collection;
import flashcard.app.collections.entity.*;
import flashcard.app.result.boundary.RedirectToResultPage;
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

import static flashcard.app.Shared.CSS;
import static java.util.Map.of;

@Path("collections")
public class CollectionsResource {

	private static final Db DATABASE = new PostgresqlDbFromKaraf();

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
		var check = new CollectionAlreadyExistsInDatabase(DATABASE);
		// If you want check if the file was saved on the server instead:
		//CollectionAlreadyExists check = new FileCollectionAlreadyExists(COLLECTIONS_DIR);
		try {
			if (check.alreadyExists(fileName)) {
				return redirectToErrorPage("Collection " + fileName + " already exists");
			}
		} catch (CollectionAlreadyExists.CouldNotBeDetermined e) {
			return redirectToErrorPage(e.getMessage());
		}

		final Collection newCollection;
		try {
			// FIXME this fails with csv files
			newCollection = new DatabaseImporter(DATABASE, fileName, uploadedFile).importCollection();
			// If you want to use the file importer instead:
			//newCollection = new FileImporter(COLLECTIONS_DIR, fileName, uploadedFile).importCollection();
		} catch (Importer.CouldNotImport e) {
			return redirectToErrorPage(e.getMessage());
		}
		collections.List().add(newCollection);
		return redirectToSuccessPage(newCollection);
	}

	private static Response redirectToErrorPage(String message) {
		return new RedirectToResultPage("Import failed", message).direct();
	}

	private static Response redirectToSuccessPage(Collection collection) {
		return new RedirectToResultPage("Import successful", "Collection " + collection.Name() + " has been imported successfully.").direct();
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	@Template(name = "/collections.mustache")
	public Map<String, Object> listCollections() {
		// Needed workaround because otherwise the property EncodedName is not read by mustache.
		List<Map<String, String>> collectionsAsMap = collections.List().stream()
				.map(collection -> of(
						"Name", 	   collection.Name(),
						"EncodedName", collection.EncodedName()
				))
				.toList();

		return of("stylesheet", CSS,
				  "collections", collectionsAsMap);
	}


}