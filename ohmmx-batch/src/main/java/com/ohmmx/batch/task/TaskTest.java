package com.ohmmx.batch.task;

import java.util.Random;

import org.springframework.stereotype.Component;

import com.ohmmx.common.core.TaskConsumeResult;
import com.ohmmx.common.entity.TaskQueue;

// INSERT INTO TSK_QUEUE (TYPE, REFERENCE, STATE, NOT_AFTER)
// VALUES ('task.batch.test', UUID(), 'NEW', DATE_ADD(NOW(), INTERVAL 1 DAY));
@Component("task.batch.test")
public class TaskTest extends AbstractTask {

	@Override
	public TaskConsumeResult doInternal(TaskQueue task) throws Exception {
		String serialNo = task.getId().getReference();
		logger.info("serialNo: [{}]", serialNo);

		switch (task.getState()) {
		case "NEW":
			Random r = new Random();
			boolean flag = r.nextBoolean();
			logger.info("current flag: " + flag);
			if (flag) {
				return next("PROC");
			} else {
				return hang();
			}
		case "PROC":
			int retries = task.getRetries();
			if (retries < 10) {
				return retry(60);
			} else {
				return succeed();
			}
		default:
			return hang("UNKOWN");
		}
	}
}
