package nanisite.core.utility;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Component(enabled=true, service= { ResourceResolverForAccess.class }, immediate=true)
public class ResourceResolverForAccess {

	@Reference
	ResourceResolverFactory resourceResolverFactory;
	
	private static final Logger Logger = LoggerFactory.getLogger(ResourceResolverForAccess.class);	
	
	public ResourceResolver getResourceResolverMethod() {
		
		ResourceResolver resourceResolver = null;
		Map<String, Object> param = new HashMap();
		param.put(ResourceResolverFactory.SUBSERVICE, "subService");		
		try {
			Logger.info("before resourceResolver {}",resourceResolver);
			resourceResolver= this.resourceResolverFactory.getServiceResourceResolver(param);
			Logger.info("resourceResolver {}",resourceResolver);
			
		}catch (LoginException e) {
			Logger.error("Error Caught in get resource resolver {}",e);
		
		}
		return resourceResolver;		
	}	
}
