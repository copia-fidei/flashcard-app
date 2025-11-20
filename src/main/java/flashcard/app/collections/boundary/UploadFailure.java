package flashcard.app.collections.boundary;

import flashcard.app.HtmlTemplate;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static java.util.Objects.requireNonNullElse;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.status;

@Path("upload-failure")
public class UploadFailure {

    private static final String PAGE_LOAD_ERROR = "Page could not be loaded";
    private static final String NO_ERROR_MESSAGE_PROVIDED = "Error message has not been provided.";

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response page(@QueryParam("message") String errorMessage) {
        return new HtmlTemplate("/html_templates/upload-failure.html")
                .get()
                .map(template -> template
						.replace("${errorMessage}", requireNonNullElse(errorMessage, NO_ERROR_MESSAGE_PROVIDED)))
                .map(Response::ok)
                .orElse(status(INTERNAL_SERVER_ERROR).entity(PAGE_LOAD_ERROR))
                .build();
    }
}