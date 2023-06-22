package com.ohmmx.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "返回结果", description = "响应结果.失败会给出具体的返回信息.")
public class Result {

	public Result() {
	}

	public Result(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	@Schema(name = "成功状态")
	private boolean success;

	@Schema(name = "返回信息")
	private String message;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
