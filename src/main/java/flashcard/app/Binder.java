package flashcard.app;

import flashcard.app.collections.entity.AllCollections;
import flashcard.app.collections.entity.Collections;
import org.glassfish.jersey.internal.inject.AbstractBinder;

import javax.inject.Singleton;

public class Binder extends AbstractBinder {

	@Override
	protected void configure() {
		bind(AllCollections.class).to(Collections.class).in(Singleton.class);
	}
}
