package flashcard.app.collection.boundary;

import flashcard.app.HtmlTemplate;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static java.util.Objects.requireNonNullElse;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

@Path("delete-success")
public class DeleteSuccess {

    private static final String PAGE_LOAD_ERROR = "Page could not be loaded";
    private static final String COLLECTION_NOT_PROVIDED = "No collection name provided";

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response page(@QueryParam("collection") String collectionName) {
        return new HtmlTemplate("/html_templates/delete-success.html")
                .get()
                .map(template -> template.replace(
                        "${collectionName}",
                        requireNonNullElse(collectionName, COLLECTION_NOT_PROVIDED)))
                .map(Response::ok)
                .orElse(status(INTERNAL_SERVER_ERROR).entity(PAGE_LOAD_ERROR))
                .build();
    }
}