package com.ohmmx.ftree.dto;

public class DaumReqDto {
	private String prePath;
	private String txtFilePath;
	private String keyword;
	private int step;
	private boolean containFolder = true;

	public String getPrePath() {
		return prePath;
	}

	public void setPrePath(String prePath) {
		this.prePath = prePath;
	}

	public String getTxtFilePath() {
		return txtFilePath;
	}

	public void setTxtFilePath(String txtFilePath) {
		this.txtFilePath = txtFilePath;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public boolean isContainFolder() {
		return containFolder;
	}

	public void setContainFolder(boolean containFolder) {
		this.containFolder = containFolder;
	}
}
