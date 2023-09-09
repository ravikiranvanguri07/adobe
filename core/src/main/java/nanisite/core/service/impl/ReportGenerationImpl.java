package nanisite.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.Session;

import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.mail.HtmlEmail;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.mail.MailTemplate;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import com.day.cq.wcm.api.Page;

import nanisite.core.schedulers.EmailReportingScheduler.Config;
import nanisite.core.service.ReportGeneration;
import nanisite.core.utility.ResourceResolverForAccess;
import nanisite.core.utility.SiteConstants;

@Component(enabled = true, service = { ReportGeneration.class }, immediate = true)
public class ReportGenerationImpl implements ReportGeneration {
	private final static Logger log = LoggerFactory.getLogger(ReportGenerationImpl.class);

	@Reference
	ResourceResolverForAccess resourceResolverForAccess;

	@Reference
	MessageGatewayService messageGatewayService;

	@Override
	public void pageReport(Config config) {
		log.info("Report Generation started with config: {}", config);

		List pageList = new ArrayList();

		ResourceResolver resourceResolver = resourceResolverForAccess.getResourceResolverMethod();
		Resource resource = resourceResolver.getResource(SiteConstants.MYSITE_PATH);
		
		if (resource != null) {
			Page pageChildren = resource.adaptTo(Page.class);
			if(pageChildren!=null) {
			getPageChildren(pageList, pageChildren);
			}
			sendEmail(pageList.toString(), resourceResolver);
			
		}
	}

	private void getPageChildren(List pageList, Page pageChildren) {
		
		Iterator<Page> pageIterator = pageChildren.listChildren();
		while(pageIterator.hasNext()) {
			Page childPage = pageIterator.next();
		
			// log.info("childPage {}", childPage);
			JSONObject jsonObj = new JSONObject();

			if (childPage != null) {
				try {
					jsonObj.put(SiteConstants.PAGE_TITLE, childPage.getTitle());
					jsonObj.put(SiteConstants.PAGE_PATH, childPage.getPath());
					jsonObj.put(SiteConstants.DESCRIPTION, childPage.getDescription());
				//	log.info(" JSON Object details : {}", jsonObj.toString());

					pageList.add(jsonObj);
					log.info("List Object: {}", pageList.toString());
				} catch (JSONException e) {
					log.info(" Any error", e);
					e.printStackTrace();
				}

			}
			getPageChildren(pageList, childPage);
		}
	}

	public void sendEmail(String emailContent, ResourceResolver resourceResolver) {
		log.info("Sending email with content: {}", emailContent);
		String fromEmail = SiteConstants.FROM_EMAIL;
		String password = SiteConstants.PASSWORD;
		// String toEmail = EmailReportingScheduler.Config.schedularEmail();
		String toEmail = SiteConstants.TO_EMAIL;
		try {

			Map<String, String> map = new HashMap();
			map.put("frommail", fromEmail);
			map.put("subject", "Page Report Mail");
			map.put("recipientName", "Ravi");
			map.put("message", emailContent);
			map.put("senderName", "Nani");

			MessageGateway<HtmlEmail> messageGateway = messageGatewayService.getGateway(HtmlEmail.class);
Resource templatePath = resourceResolver.getResource(SiteConstants.TEMPLATE_PATH);
log.info(" Template Path {}", templatePath.getPath());
			Session session = resourceResolver.adaptTo(Session.class);
			if (session != null && templatePath!=null) {
				MailTemplate mailTemplate = MailTemplate.create(templatePath.getPath(), session);
				HtmlEmail email = mailTemplate.getEmail(StrLookup.mapLookup(map), HtmlEmail.class);
				log.info("Email {}",email.toString() );
				if (email != null) {
					// messageGateway.send(email);
					log.info("Email Sent {}");
				}
			}
		}

		catch (Exception e) {
			log.error("Error while sending email", e);
		}

	}
}
