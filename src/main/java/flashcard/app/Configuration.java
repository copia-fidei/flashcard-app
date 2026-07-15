package flashcard.app;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.mustache.MustacheMvcFeature;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Configuration extends ResourceConfig {

	public static final Path COLLECTIONS_DIR = Paths.get("data/app/flashcards/collections");

	public Configuration() {
		register(MultiPartFeature.class);

		property(MustacheMvcFeature.TEMPLATE_BASE_PATH, "WEB-INF/classes/templates");
		register(MustacheMvcFeature.class);

		register(new Binder());
	}
}