package flashcard.app;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.nio.file.Path;
import java.nio.file.Paths;

//@ApplicationPath("service")
public class Configuration
		extends ResourceConfig
{
	public static final Path COLLECTIONS_DIR = Paths.get("data/app/flashcards/collections");

   public Configuration() {
	   register(MultiPartFeature.class);

	   register(new Binder());
   }
}