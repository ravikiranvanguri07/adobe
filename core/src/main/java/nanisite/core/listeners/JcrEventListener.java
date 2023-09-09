package nanisite.core.listeners;

import javax.jcr.LoginException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;

import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JcrEventListener implements EventListener {
	
	private final Logger log = LoggerFactory.getLogger(JcrEventListener.class);
	@Reference
	SlingRepository slingRepository;
	
	@Activate
	public void activate() {
		try {
			Session session = slingRepository.login(null, null);
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchWorkspaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Override
	public void onEvent(EventIterator events) {
		// TODO Auto-generated method stub
		
	}

}
