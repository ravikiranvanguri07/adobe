package nanisite.core.workflow;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.mail.HtmlEmail;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.commons.mail.MailTemplate;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;

import nanisite.core.utility.ResourceResolverForAccess;
import nanisite.core.utility.SiteConstants;

@Component(service = WorkflowProcess.class, property = { "process.label = Activation page under nanisite" })
public class NaniPageActivationWf implements WorkflowProcess {
	@Reference
	Replicator replicator;
	@Reference
	MessageGatewayService messageGatewayService;
	@Reference
	ResourceResolverForAccess resourceResolverForAccess;
	private static final Logger log = LoggerFactory.getLogger(NaniPageActivationWf.class);

	@Override
	public void execute(WorkItem workItem, WorkflowSession wfsession, MetaDataMap metamap) throws WorkflowException {

		String payloadPath = workItem.getWorkflowData().getPayload().toString();
		log.info("Pay Load path {}", payloadPath);
		Session session = wfsession.adaptTo(Session.class);
		try {
			ResourceResolver resourceResolver = resourceResolverForAccess.getResourceResolverMethod();
			UserManager userManager = resourceResolver.adaptTo(UserManager.class);
			String initiator = workItem.getWorkflow().getInitiator();
			Authorizable authorizableUser = userManager.getAuthorizable(initiator);
			String notifyuser = "";
			if (authorizableUser.hasProperty("profile/email")) {
				Value[] emailUser = authorizableUser.getProperty("profile/email");
				notifyuser = emailUser[0].getString();
				log.info("User email ID {}", notifyuser);
			}
			replicator.replicate(session, ReplicationActionType.ACTIVATE, payloadPath);
			log.info("Page Activated");
			notifyEmail(notifyuser, payloadPath, resourceResolver);
		} catch (ReplicationException e) {
			log.info("ReplicationException {}", e);

			e.printStackTrace();
		} catch (RepositoryException e) {
			log.info("ReplicationException {}", e);
			e.printStackTrace();
		} finally {
			if (session != null && session.isLive()) {
				session.logout();
			}
		}

	}

	public void notifyEmail(String notifyuser, String payloadPath, ResourceResolver resourceResolver) {
		log.info("Sending email with content: {}");
		String fromEmail = SiteConstants.FROM_EMAIL;
		String password = SiteConstants.PASSWORD;
		String toEmail = notifyuser;
		try {

			Map<String, String> map = new HashMap();
			map.put("frommail", fromEmail);
			map.put("subject", "Page Report Mail");
			map.put("recipientName", "Ravi");
			map.put("message", "Page is activated under this path");
			map.put("payLoadPath", payloadPath);
			map.put("senderName", "Nani");
			log.info(" Notification {}", map.toString());

			MessageGateway<HtmlEmail> messageGateway = messageGatewayService.getGateway(HtmlEmail.class);

			Resource templatePath = resourceResolver.getResource(SiteConstants.WORKFLOW_TEMPLATE_PATH);
			log.info(" WorkFlow Template Path {}", templatePath.getPath());
			Session session = resourceResolver.adaptTo(Session.class);
			if (session != null && templatePath != null) {
				MailTemplate mailTemplate = MailTemplate.create(templatePath.getPath(), session);
				HtmlEmail email = mailTemplate.getEmail(StrLookup.mapLookup(map), HtmlEmail.class);
				log.info("Email {}", email.toString());
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
