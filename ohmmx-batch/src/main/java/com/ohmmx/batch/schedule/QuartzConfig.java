package com.ohmmx.batch.schedule;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ohmmx.batch.quartz.service.TaskQueueConsumer;

@Configuration
public class QuartzConfig {

	@Bean
	public JobDetail jobDetail() {
		return JobBuilder.newJob(TaskQueueConsumer.class).withIdentity("taskQueueConsumer").withDescription("任务队列").storeDurably().build();
	}

	@Bean
	public Trigger trigger() {
		return TriggerBuilder.newTrigger().forJob(jobDetail()).withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?")).build();
	}
}
