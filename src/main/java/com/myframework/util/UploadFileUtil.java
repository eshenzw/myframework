package com.myframework.util;

import com.myframework.constant.Constants;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;

/**
 * 上传文件工具类
 * @author zw<br />
 * 2013-1-18
 */
public class UploadFileUtil {

	/**
	 * 上传文件
	 * @param request
	 * @param uploadPath
	 * @param jspFileName
	 * @param isCover
	 * @param modifyFileName
	 * @return 返回上传后的文件名
	 * @throws Exception
	 */
	public static String uploadFile(HttpServletRequest request, String uploadPath, String jspFileName, boolean isCover, boolean modifyFileName) throws Exception {
		String longFileName = "", fileName = "";
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile imageFile = null;
		imageFile = (CommonsMultipartFile) multipartRequest.getFile(jspFileName);
		if (imageFile != null && !imageFile.isEmpty()) {
			fileName = imageFile.getOriginalFilename();
			if (modifyFileName) {
				fileName = DateUtil.getTimestamp("yyyyMMddHHmmss") + "_" + RandomUtil.getRandom(4) + fileName.substring(fileName.lastIndexOf("."));
			}
			longFileName = Constants.UPLOAD_ROOT_PATH + uploadPath + fileName;
			File uploadFile = new File(longFileName);
			if (!isCover && uploadFile.exists()) {
				throw new Exception(uploadFile.getPath() + " is exists");
			}
			FileUtil.makeFilePath(Constants.UPLOAD_ROOT_PATH + uploadPath);
			FileCopyUtils.copy(imageFile.getBytes(), uploadFile);
		}
		return fileName;
	}
	
	/**
	 * spring上传文件
	 * @param file
	 * @param uploadPath
	 * @param isCover
	 * @param modifyFileName
	 * @return 返回上传后的文件名
	 * @throws Exception
	 */
	public static String uploadFile(MultipartFile file, String uploadPath, boolean isCover, boolean modifyFileName) throws Exception {
		String longFileName = "", fileName = "";
		if (file != null && !file.isEmpty()) {
			fileName = file.getOriginalFilename();
			if (modifyFileName) {
				fileName = DateUtil.getTimestamp("yyyyMMddHHmmss") + "_" + RandomUtil.getRandom(4) + fileName.substring(fileName.lastIndexOf("."));
			}
			longFileName = Constants.UPLOAD_ROOT_PATH + uploadPath + fileName;
			File uploadFile = new File(longFileName);
			if (!isCover && uploadFile.exists()) {
				throw new Exception(uploadFile.getPath() + " is exists");
			}
			FileUtil.makeFilePath(Constants.UPLOAD_ROOT_PATH + uploadPath);
			FileCopyUtils.copy(file.getBytes(), uploadFile);
		}
		return fileName;
	}
	
	/**
	 * 上传文件，返回InputStream
	 * @param request
	 * @param jspFileName
	 * @return 返回上传后的文件流
	 * @throws Exception
	 */
	public static InputStream uploadFile(HttpServletRequest request, String jspFileName) throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile imageFile = null;
		imageFile = (CommonsMultipartFile) multipartRequest.getFile(jspFileName);
		return imageFile.getInputStream();
	}
}
