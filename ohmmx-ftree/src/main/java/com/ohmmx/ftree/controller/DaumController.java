package com.ohmmx.ftree.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ohmmx.common.dto.Result;
import com.ohmmx.ftree.dto.DaumReqDto;
import com.ohmmx.ftree.service.DaumService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/ftree/daum")
@Tag(name = "目录树接口", description = "DaumController|目录树接口")
public class DaumController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DaumService daumService;

	@Operation(summary = "生成播放列表", description = "生成播放列表 1:解析 2:生成")
	@RequestMapping(value = "initDaumPlaylist", method = RequestMethod.POST)
	public Result initDaumPlaylist(
			@Parameter(name = "dto", description = "解析参数", required = true) @RequestBody DaumReqDto dto) {
		Result result = new Result();
		int step = dto.getStep();
		switch (step) {
		case 1:
			logger.info("步骤1开始");
			boolean succeed = daumService.initTxtFile(dto);
			if (!succeed) {
				break;
			}
		case 2:
			logger.info("步骤2开始");
			return daumService.makeFile(dto);
		case 3:
			logger.info("读取时长开始");
			daumService.readDuration(dto);
			break;
		default:
			result.setSuccess(false);
			result.setMessage("不支持的步骤");
		}
		return result;
	}

	@Operation(summary = "重命名FC2", description = "重命名FC2")
	@RequestMapping(value = "renameFc2", method = RequestMethod.POST)
	public Result renameFc2(@Parameter(name = "month", description = "月份", required = true) @RequestParam String month, //
			@Parameter(name = "filePath", description = "文件列表TXT全路径", required = true) @RequestParam String filePath) {
		Result result = new Result();
		try {
			daumService.fullfillFilename(month, filePath);
		} catch (Exception e) {
			logger.error("解析FC2PPV文件失败", e);
			result.setSuccess(false);
			result.setMessage("解析FC2PPV文件失败");
			return result;
		}
		result.setSuccess(true);
		return result;
	}
}
