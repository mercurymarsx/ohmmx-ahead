package com.ohmmx.common.core.task;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultTaskQueueSelector extends AbstractTaskQueueSelector {

	@Value("task.")
	private String taskPrefix;
}
