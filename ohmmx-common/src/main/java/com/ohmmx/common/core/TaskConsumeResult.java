package com.ohmmx.common.core;

public class TaskConsumeResult {

	private static final TaskConsumeResult RETRY = new TaskConsumeResult();
	private static final TaskConsumeResult SUCCEED = TaskConsumeResult.succeed("SUCCEED");
	private static final TaskConsumeResult FAILED = TaskConsumeResult.failed("FAILED");

	private String state;

	private boolean done;

	private boolean succeed;

	private int interval;

	public static TaskConsumeResult done(String state, boolean succeed) {
		TaskConsumeResult result = new TaskConsumeResult();
		result.state = state;
		result.done = true;
		result.succeed = succeed;
		return result;
	}

	/**
	 * 结束任务, 业务结果为成功.
	 *
	 * @param state 结束状态
	 */
	public static TaskConsumeResult succeed(String state) {
		TaskConsumeResult result = new TaskConsumeResult();
		result.state = state;
		result.succeed = true;
		result.done = true;
		return result;
	}

	public static TaskConsumeResult succeed() {
		return SUCCEED;
	}

	/**
	 * 结束任务, 业务结果为失败.
	 *
	 * @param state 结束状态
	 */
	public static TaskConsumeResult failed(String state) {
		TaskConsumeResult result = new TaskConsumeResult();
		result.state = state;
		result.succeed = false;
		result.done = true;
		return result;
	}

	public static TaskConsumeResult failed() {
		return FAILED;
	}

	/** 等待下一轮执行，增加retry计数 */
	public static TaskConsumeResult retry() {
		return RETRY;
	}

	public static TaskConsumeResult retry(int interval) {
		TaskConsumeResult result = new TaskConsumeResult();
		result.interval = interval;
		return result;
	}

	public static TaskConsumeResult next(String state) {
		return next(state, 0);
	}

	/** 流转到下一状态，等待下一轮任务触发, 如果下轮interval时间未到, 再等待下一轮 */
	public static TaskConsumeResult next(String state, int interval) {
		TaskConsumeResult result = new TaskConsumeResult();
		result.state = state;
		result.interval = interval;
		return result;
	}

	/** 挂起 */
	public static TaskConsumeResult hang(String state) {
		TaskConsumeResult result = new TaskConsumeResult();
		result.state = state;
		result.interval = 3600 * 24;
		return result;
	}

	public static TaskConsumeResult hang() {
		return retry(3600 * 24);
	}

	public boolean isRetry(String prevState) {
		return state == null || state.equals(prevState);
	}

	public String getState() {
		return state;
	}

	public boolean isDone() {
		return done;
	}

	public boolean isSucceed() {
		return succeed;
	}

	public int getInterval() {
		return interval;
	}
}
