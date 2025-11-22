package flashcard.app;

import javax.ws.rs.core.Response;
import java.io.IOException;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.status;

public abstract class HtmlResponse {

	protected static final String PAGE_LOAD_ERROR = "Page could not be loaded";

	private HtmlTemplateLoader templateLoader;

	protected HtmlResponse(final String htmlFileLocation) {
		templateLoader = new HtmlTemplateLoader(htmlFileLocation);
	}

	public abstract Response ResponseWithAvailableHtmlTemplate(String htmlTemplate);

	public Response Response() {
		try {
			return ResponseWithAvailableHtmlTemplate(templateLoader.loadHtml());
		} catch (IOException e) {
			return status(INTERNAL_SERVER_ERROR)
					.entity(PAGE_LOAD_ERROR)
					.build();
		}
	}
}
