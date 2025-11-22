package flashcard.app.result.boundary;

import flashcard.app.HtmlTemplateLoader;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static java.util.Objects.requireNonNullElse;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.status;

@Path("error")
public class ErrorResource {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response page(@QueryParam("title") String title, @QueryParam("description") String description) {
		return new ErrorHtmlResponse(title, description).Response();
	}
}