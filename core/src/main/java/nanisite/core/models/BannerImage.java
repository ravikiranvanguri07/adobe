package nanisite.core.models;

import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;

@Model(adaptables = {Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BannerImage {

	@Inject
	public String fileReference;

	@Inject
	public String alt;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public String getFileReference() {
		logger.info("file reference path: {}",fileReference);
		return fileReference;
	}

	public String getAlt() {
		return alt;
	}
	

}
