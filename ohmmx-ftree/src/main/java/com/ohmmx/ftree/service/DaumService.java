package com.ohmmx.ftree.service;

import com.ohmmx.common.dto.Result;
import com.ohmmx.ftree.dto.DaumReqDto;

public interface DaumService {
	boolean initTxtFile(DaumReqDto dto);

	Result makeFile(DaumReqDto dto);

	void readDuration(DaumReqDto dto);
	
	void fullfillFilename(String month, String txtFilePath) throws Exception;
}
