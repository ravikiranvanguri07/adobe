package nanisite.core.listeners;

import java.util.List;

import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component(service = ResourceChangeListener.class, 
property = {ResourceChangeListener.CHANGES+"=ADDED",
		ResourceChangeListener.CHANGES+" =CHANGED",
		ResourceChangeListener.CHANGES+ "=REMOVED",
		ResourceChangeListener.PATHS+"=/content/nani-site/home"
		
})
public class SlingResourceListener implements ResourceChangeListener {
	
	private final Logger log = LoggerFactory.getLogger(SlingResourceListener.class);

	@Override
	public void onChange(List<ResourceChange> list) {
		// TODO Auto-generated method stub
		
	}

}
