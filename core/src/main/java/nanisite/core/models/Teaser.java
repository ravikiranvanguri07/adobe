package nanisite.core.models;


import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
@Model(adaptables = {Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Teaser {
    
   
	@Inject
	public String fileReference;

	 @Inject
	public String alt;
	
	 @Inject
	public String heading;
	
	 @Inject
	public String subheading;
	
	@Inject
	public String buttonLabel;
	
	@Inject
	public String buttonLinkTo;
	
	public String getButtonLinkTo() {
		return buttonLinkTo;
	}

	public String getFileReference() {
		return fileReference;
	}

	public String getAlt() {
		return alt;
	}

	public String getHeading() {
		return heading;
	}

	public String getSubheading() {
		return subheading;
	}

	public String getButtonLabel() {
		return buttonLabel;
	}
	

}
