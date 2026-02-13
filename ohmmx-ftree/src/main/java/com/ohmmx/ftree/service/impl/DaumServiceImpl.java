package com.ohmmx.ftree.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ohmmx.common.dto.Result;
import com.ohmmx.ftree.dto.DaumReqDto;
import com.ohmmx.ftree.entity.Fc2ppv;
import com.ohmmx.ftree.entity.PotPlaylist;
import com.ohmmx.ftree.mapper.Fc2ppvRepository;
import com.ohmmx.ftree.mapper.PotPlaylistRepository;
import com.ohmmx.ftree.service.DaumService;

@Service
public class DaumServiceImpl implements DaumService {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String[] IGNORE_SUFFIX = { "jpg", "jpeg", "png", "srt", "tif", "zip", "rar", "nfo", "txt" };

	private static String paths = "";
	private static String folderName = "";

	private static Stack<String> stack = new Stack<>();

	@Autowired
	private PotPlaylistRepository potPlaylistRepository;
	@Autowired
	private Fc2ppvRepository fc2ppvRepository;

	public boolean initTxtFile(DaumReqDto dto) {
		String prePath = dto.getPrePath();
		String txtFilePath = dto.getTxtFilePath();

		try {
			List<String> lines = Files.readAllLines(Paths.get(txtFilePath), StandardCharsets.UTF_8);
			stack = new Stack<>();

			logger.info("待解析记录数: " + lines.size());
			for (String line : lines) {
				if (StringUtils.isBlank(line)) {
					continue;
				}

				line = StringUtils.replace(line, "——", "-");
				int depth = StringUtils.countMatches(line, "|");
				line = line.replace("|-", "");
				line = line.replace("| ", "").trim();

				boolean isFolder = false;
				String suffix = "";
				if (StringUtils.indexOf(line, ".") <= 0) { // 文件夹
					isFolder = true;
				} else {
					suffix = StringUtils.substring(line, StringUtils.lastIndexOf(line, ".") + 1);
					if (StringUtils.length(suffix) > 5) { // 文件夹
						isFolder = true;
					}
				}
				if (Pattern.matches("\\d+", suffix)) { // 文件夹
					isFolder = true;
				}
				if (isFolder) {
					refreshDirs(depth - 1, line, true);
					logger.info("解析[文件夹]" + line + ", 当前路径: " + paths);
					continue;
				}
				if (ArrayUtils.contains(IGNORE_SUFFIX, suffix.toLowerCase())) { // 忽略指定文件类型
					// logger.info("解析[已忽略]" + line);
					continue;
				}
				String fileName = StringUtils.substring(line, 0, StringUtils.lastIndexOf(line, "."));

				refreshDirs(depth - 1, line, false);
				String path = prePath + paths;
				logger.info("解析[文件]" + fileName + ", 后缀名: " + suffix);
				PotPlaylist ppl = potPlaylistRepository.findById(path).orElse(null);
				if (ppl != null) {
					continue;
				}

				// long duration = VideoUtil.getDuration(path);
				long duration = 4 * 60 * 60 * 1000;

				PotPlaylist potPlaylist = new PotPlaylist();
				potPlaylist.setFilePath(path);
				potPlaylist.setFolderName(folderName);
				potPlaylist.setFileName(fileName);
				potPlaylist.setDuration(duration);
				potPlaylistRepository.save(potPlaylist);

				Thread.sleep(5);
			}
			logger.info("解析完成");
			return true;
		} catch (Exception e) {
			logger.error("init error.", e);
			return false;
		}
	}

	public void readDuration(DaumReqDto dto) {
		try {
			Map<String, String> checkMap = new HashMap<>();
			List<String> lines = Files.readAllLines(Paths.get(dto.getKeyword()), StandardCharsets.UTF_8);
			for (String line : lines) {
				if (StringUtils.indexOf(line, "*") <= 0) {
					continue;
				}
				List<String> item = Arrays.asList(StringUtils.split(line, "*"));
				if (!Pattern.matches("[1-9]\\d*", item.get(0))) {
					continue;
				}
				if (item.size() < 3) {
					continue;
				}
				if (!StringUtils.equals(item.get(0), checkMap.get("index"))) {
					checkMap.put("index", item.get(0));
					saveDuration(checkMap);
				}
				if (StringUtils.equals(item.get(1), "file")) {
					checkMap.put("file", item.get(2));
				}
				if (StringUtils.equals(item.get(1), "duration2")) {
					checkMap.put("duration", item.get(2));
				}
			}
			saveDuration(checkMap);
		} catch (IOException e) {
			logger.error("init error.", e);
		}
	}

	public void fullfillFilename(String month, String txtFilePath) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(txtFilePath), StandardCharsets.UTF_8);
		for (String line : lines) {
			if (StringUtils.isBlank(line)) {
				continue;
			}
			File file = new File(line);
			if (!file.exists()) {
				logger.info("文件不存在: " + file.getAbsolutePath());
				continue;
			}
			fileRename(month, file);

			Thread.sleep(5 * 1000);
		}
		logger.info("FC2PPV重命名完成");
	}

	private void saveDuration(Map<String, String> checkMap) {
		String file = checkMap.get("file");
		String duration = checkMap.get("duration");
		if (StringUtils.isBlank(file)) {
			return;
		}
		logger.info(file + " <===> " + duration);
		PotPlaylist ppl = potPlaylistRepository.findById(file).orElse(null);
		if (ppl != null) {
			ppl.setDuration(Long.valueOf(duration));
			potPlaylistRepository.save(ppl);
		}
	}

	public Result makeFile(DaumReqDto dto) {
		String keyword = dto.getKeyword();

		Result result = new Result();
		boolean flag = true;
		File file = new File("D:/tmp/dpl/playlist.dpl");
		FileWriter writer = null;
		try {
			writer = new FileWriter(file, false);

			writer.write("DAUMPLAYLIST" + "\n");
			writer.write("topindex=0" + "\n");
			writer.write("saveplaypos=0" + "\n");

			List<PotPlaylist> list = potPlaylistRepository.findList(keyword);
			logger.info("生成列表开始: " + list.size());
			int idx = 1;
			for (PotPlaylist item : list) {
				writer.write(idx + "*file*" + item.getFilePath() + "\n");
				String title = idx + "*title*";
				if (dto.isContainFolder()) {
					title += "[" + item.getFolderName() + "] ";
				}
				title += item.getFileName() + "\n";
				writer.write(title);
				writer.write(idx + "*duration2*" + item.getDuration() + "\n");
				idx++;
			}
		} catch (Exception e) {
			logger.error("make data file error.", e);
			flag = false;
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				flag = false;
				logger.error("FileWriter close error.", e);
			}
		}
		result.setSuccess(flag);
		result.setMessage("完成");
		logger.info("生成列表完成");
		return result;
	}

	private void refreshDirs(int depth, String line, boolean isFolder) {
		paths = "";
		if (stack.size() > depth) {
			int stackSize = stack.size();
			for (int i = 0; i < stackSize - depth; i++) {
				stack.pop();
			}
		}
		stack.add(line);
		for (String item : stack) {
			paths += item + "\\";
		}
		if (!isFolder) {
			folderName = stack.get(depth - 1);
			trimFolderName();
			paths = StringUtils.substring(paths, 0, StringUtils.length(paths) - 1);
		}
	}

	private void trimFolderName() {
		if (StringUtils.indexOf(folderName, " ") > 0) {
			// folderName = StringUtils.substring(folderName, 0,
			// StringUtils.indexOf(folderName, " ")); // 女优合集
			folderName = StringUtils.substring(folderName, StringUtils.indexOf(folderName, "-") + 2);
		}
		if (StringUtils.indexOf(folderName, ".") > 0) {
			folderName = StringUtils.substring(folderName, StringUtils.indexOf(folderName, ".") + 1);
		}
	}

	private void fileRename(String month, File file) {
		String fileName = StringUtils.substring(file.getAbsolutePath(), 30);
		String searchName = StringUtils.substring(fileName, 8, 15);

		String fcName = "PPV-" + searchName;
		Fc2ppv fc2;
		try {
			fc2 = fc2ppvRepository.getByFcid(fcName, month);
		} catch (Exception e) {
			logger.error("查询FC2PPV记录失败", e);
			fc2 = null;
		}

		if (fc2 != null) {
			String pattern = "(.*?\\s)";
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(fileName);

			String newName;
			if (m.find()) {
				newName = m.group(1) + fc2.getVideoName();
			} else {
				newName = StringUtils.replace(fileName, ".mp4", "") + " " + fc2.getVideoName();
			}
			newName = "Z:/动作片/东洋/FC2PPV/2024/" + month + "/" + newName + ".mp4";
			logger.info("New file name: " + newName);
			// rename
			if (file.renameTo(new File(newName))) {
				// update
				fc2.setStatus("1");
				fc2ppvRepository.save(fc2);
			}
		}
	}
}
