package flashcard.app;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

//@ApplicationPath("service")
public class Configuration
		extends ResourceConfig
{

   public Configuration() {
	   register(MultiPartFeature.class);
   }
}