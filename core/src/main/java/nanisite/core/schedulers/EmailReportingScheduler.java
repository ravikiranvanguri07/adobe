package nanisite.core.schedulers;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nanisite.core.service.ReportGeneration;

@Component(service = Runnable.class, immediate = true, enabled = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = EmailReportingScheduler.Config.class)
public class EmailReportingScheduler implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(EmailReportingScheduler.class);

	@ObjectClassDefinition(name = "Email Reporting Scheduler Configuration", description = "This Scheduler is for a cron job to send sheduled report emails")
	public static @interface Config {

		@AttributeDefinition(name = "Enter Cron Job Expression")
		String schedularExpression() default "*/30 * * * * ?";

		@AttributeDefinition(name = "Enter Business Email", description = "Reports are send to this particular mail")
		String schedularEmail();

		@AttributeDefinition(name = "Task Enabled")
		boolean schedularEnabled() default true;

	}

	private Config config;
	
	@Reference
	ReportGeneration reportGeneration;

	@Activate
	@Modified
	protected void activate(Config config) {
		this.config = config;
		reportGeneration.pageReport(config);
		log.info("Configuration Activated in this schdlr {}", config);
	}

	@Override
	public void run() {
		  log.info("Scheduler run method started with config: {}", config);
		reportGeneration.pageReport(config);
		  log.info("Scheduler run method finished.");

	}

}
