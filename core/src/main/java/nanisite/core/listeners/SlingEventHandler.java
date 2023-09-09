package nanisite.core.listeners;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component(service= EventHandler.class)
public class SlingEventHandler implements EventHandler{
	
	private final Logger log = LoggerFactory.getLogger(SlingEventHandler.class);
	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
		
	}

}
