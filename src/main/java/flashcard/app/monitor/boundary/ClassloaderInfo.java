package flashcard.app.monitor.boundary;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.Arrays;

import static java.lang.System.lineSeparator;

@Path("classloader")
public class ClassloaderInfo {

	@GET
	@Path("res")
	@Produces(MediaType.TEXT_PLAIN)
	public String resourceLocation() {
		return getClass().getClassLoader().getResource("") + lineSeparator();
	}

	final MessageFormat format = new MessageFormat("""
			{0}
			    {1}
			    {2}
			    {3}
			""");

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String classloader() {
		final var classLoader   = getClass().getClassLoader();
		final var pClassloader  = classLoader.getParent();
		final var ppClassloader = pClassloader.getParent();
		return "Class loader hierarchy, from this classloader to its parent (top to bottom):"
				+ clInfo(classLoader)
				+ "-".repeat(100)
				+ clInfo(pClassloader)
				+ "-".repeat(100)
				+ clInfo(ppClassloader);
	}

	private String clInfo(ClassLoader cl) {
		return format.format(new Object[] {
				cl, cl.getClass(), vertical(", package", cl.getDefinedPackages()), urlsOf(cl)
		});
	}

	private static String urlsOf(ClassLoader cl) {
		if (cl instanceof final URLClassLoader urlClassLoader) {
			return vertical(", ", urlClassLoader.getURLs());
		}
		else return "";
	}

	private static String vertical(String separator, Object[] array) {
		return Arrays.toString(array).replace(separator, lineSeparator());
	}

}
