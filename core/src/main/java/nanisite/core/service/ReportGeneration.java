package nanisite.core.service;

import nanisite.core.schedulers.EmailReportingScheduler.Config;

public interface ReportGeneration {

	public void pageReport(Config config);

}
