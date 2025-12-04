package flashcard.app.result.boundary;

import org.glassfish.jersey.server.mvc.Template;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Path("result")
public class ResultResource {

	@GET
	@Produces(MediaType.TEXT_HTML)
	@Template(name = "/result.mustache")
	public Map<String, String> show(@QueryParam("title") String title, @QueryParam("description") String description) {
		return Map.of("title", title, "description", description);
	}
}