package com.myframework.util;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myframework.constant.Constants;

/**
 * 通用的磁盘文件管理
 * 
 * @author <a href="mailto:fanwenqiang@nj.fiberhome.com.cn">AndyFan*</a>
 */
public class FileUtil
{
	/***/
	private static String thisClass = new String("com.fh.iasp.platform.util.FileUtil");
	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
	/**
	 * 文件分隔符（系统不同分隔符不同）
	 */
	public static final String FILE_SEP = Constants.FILE_SEP;

	/**
	 * 获取web跟路径。
	 * 
	 * @return string
	 */
	public static String getWebRoot()
	{
		String pathStr = "";
		try
		{
			pathStr = FileUtil.class.getResource("/").toURI().getPath().toString();
			pathStr = pathStr.substring(1, pathStr.length() - 16);
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
		return pathStr;
	}

	/**
	 * CleanDirs is a utility method for cleaning up temporary directories, used
	 * by various methods to hold & process files on the file system. CleanDir
	 * takes a directory name as an argument, and checks for any empty
	 * directories within that directory. If it finds any, it remove the empty
	 * directory & checks again until no empty directories are found.
	 * 
	 * @param fileName
	 *            fileName
	 * @throws IOException
	 *             IOException
	 */
	public static void cleanDirs(String fileName) throws IOException
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Cleaning " + fileName);
		}
		if (!fileName.endsWith("/"))
		{
			fileName = fileName + "/";
		}
		Vector contents = getDir(fileName);
		String oneItem = null;
		File oneFile = null;
		for (Enumeration e = contents.elements(); e.hasMoreElements();)
		{
			oneItem = (String) e.nextElement();
			oneFile = new File(fileName + oneItem);
			/* If it's a directory.... */
			if (oneFile.isDirectory())
			{
				if (logger.isDebugEnabled())
				{
					logger.debug("Cleaning subdirectory " + fileName + oneItem);
				}
				/* Try cleaning it */
				cleanDirs(fileName + oneItem);
				/* If it's now empty... */
				if (getDir(fileName + oneItem).size() == 0)
				{
					oneFile = new File(fileName + oneItem);
					logger.info("Deleting empty directory " + oneItem);
					/* Zap it ... */
					if (!oneFile.delete())
					{
						logger.error("Unable to delete directory " + oneItem);
					} /* if we were unable to delete */
				} /* if the dir is now empty */
			} /* if the entry is a directory */
		} /* for each entry in the directory */
	} /* cleanDirs(String) */

	/**
	 * 实用的方法来拷贝文件从一个地方到另一个地方&删除原来文件。
	 * 我们这样做,为了保存目录的安全,这是如果我们不保存在新台币移动文件简单(renameTo)如果我们失败,
	 * 我们把IOException,也报道有哪些用户这个过程跑步和协助系统管理员在设定适当的许可
	 * 
	 * @param sourceFile
	 *            Source file pathname
	 * @param destFile
	 *            Destination file pathname
	 * @throws IOException
	 *             If the copy fails due to an I/O error
	 */
	public static void copyFile(String sourceFile, String destFile) throws IOException
	{
		String myName = new String(thisClass + "copyFile(String, String)");
		if ("".equals(sourceFile))
		{
			throw new IOException(myName + ":Source file name empty - cannot" + " copy.");
		}
		if ("".equals(destFile))
		{
			throw new IOException(myName + ":Destination file name empty - " + "cannot copy.");
		}
		int onechar = 0;
		if (sourceFile.equals(destFile))
		{
			throw new IOException(myName + ":Cannot copy file '" + sourceFile + "' to itself");
		}
		File dest = new File(destFile);
		dest.mkdirs();
		if (dest.exists())
		{
			if (!dest.delete())
			{
				throw new IOException(myName + ":Unable to delete existing " + "destination file '" + destFile
						+ "'. Logged in as " + System.getProperty("user.name"));
			}
		}
		File source = new File(sourceFile);
		if (!source.exists())
		{
			throw new IOException(myName + ":Source file '" + sourceFile
					+ "' does not exist. Cannot copy. Logged in as " + System.getProperty("user.name"));
		}
		FileOutputStream fout = new FileOutputStream(destFile);
		BufferedOutputStream bout = new BufferedOutputStream(fout);
		FileInputStream fin = new FileInputStream(sourceFile);
		BufferedInputStream bin = new BufferedInputStream(fin);
		onechar = bin.read();
		while (onechar != -1)
		{
			bout.write(onechar);
			onechar = bin.read();
		}
		bout.flush();
		bin.close();
		fin.close();
		fout.close();
		if (!dest.exists())
		{
			throw new IOException(myName + ":File copy failed: destination file" + " '" + destFile
					+ "' does not exist after copy.");
		}
	}

	/**
	 * 加载本地文件到数据流中
	 * 
	 * @param fileName
	 *            文件名称
	 * @return InputStream
	 */
	public static InputStream loadFileAsStream(String fileName)
	{
		logger.info("动态加载配置文件：{}", fileName);
		InputStream ins = null;
		try
		{
			String path = FileUtil.class.getClassLoader().getResource(fileName).getPath();
			logger.info(path);
			return new FileInputStream(path);
		}
		catch (FileNotFoundException e)
		{
			logger.error("配置文件 {} 找不到！", fileName, e);
		}
		catch (Exception e)
		{
			logger.error("读取配置文件 {} 失败，原因：{}", fileName, e);
		}
		finally
		{
			IOUtils.closeQuietly(ins);
		}
		return null;
	}

	/**
	 * Get the base filename of a file (e.g. no directory or extension)
	 * 
	 * @param fileName
	 *            Original pathname to get the base name from. IMPORTANT: This
	 *            method assumes that "/" is the directory seperator, not "\"!!!
	 * @return String The base file name
	 */
	public static String getBase(String fileName)
	{
		String tempName1 = new String("");
		StringTokenizer stk1 = new StringTokenizer(fileName, "/");
		while (stk1.hasMoreTokens())
		{
			tempName1 = stk1.nextToken();
		}
		StringTokenizer stk = new StringTokenizer(tempName1, ".");
		return stk.nextToken();
	}

	/**
	 * 获取文件夹下的所有文件Vector类型的
	 * 
	 * @param dirName
	 *            文件夹名称
	 * @return Vector类型的文件列表
	 * @throws IOException
	 *             如果指定的名字不是一个目录时发生I/O错误
	 */
	public static Vector getDir(String dirName) throws IOException
	{
		String myName = new String(thisClass + "getDir(String)");
		File dirFile = new File(dirName);
		if (!dirFile.isDirectory())
		{
			throw new IOException(myName + ":'" + dirName + "' is not a directory.");
		}
		String[] dir = dirFile.list();
		if (dir == null)
		{
			throw new IOException(myName + ":Null array reading directory " + " of " + dirName);
		}
		Vector fileList = new Vector(1);
		String oneFileName = null;
		for (int i = 0; i < dir.length; i++)
		{
			oneFileName = dir[i].trim();
			fileList.addElement(oneFileName);
		}
		return fileList;
	}

	/**
	 * 获取文件扩展名(after the ".")
	 * 
	 * @param fileName
	 *            原有的全部文件的名字
	 * @return String Extension name 过滤null和"" 直接返回null 如果没有扩展名直接返回""
	 * @throws IOException
	 *             如果无法分配到的文件扩展
	 */
	public static String getExtension(String fileName) throws IOException
	{
		// 过滤null和"" 直接返回null
		if (fileName == null || "".equals(fileName.trim()))
		{
			return null;
		}
		if (fileName.lastIndexOf(".") != -1)
		{
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		}
		else
		{
			return new String("");
		}
	}

	/**
	 * 通过文件名获取文件路径
	 * 
	 * @param fileName
	 *            Original pathname
	 * @return String Path portion of the pathname
	 */
	public static String getPath(String fileName)
	{
		StringBuffer path = new StringBuffer("/");
		String nextToken;
		StringTokenizer stk = new StringTokenizer(fileName, "/");
		while (stk.hasMoreTokens())
		{
			nextToken = stk.nextToken();
			if (stk.hasMoreTokens())
			{
				path.append(nextToken);
				path.append("/");
			}
		}
		return path.toString();
	}

	/**
	 * 复制一个文件,然后去除原始文件
	 * 
	 * @param sourceFile
	 *            Original file name
	 * @param destFile
	 *            Destination file name
	 * @throws IOException
	 *             If an I/O error occurs during the copy
	 */
	public static void moveFile(String sourceFile, String destFile) throws IOException
	{
		String myName = new String(thisClass + "moveFile(String, String)");
		File source = new File(sourceFile);
		if (!source.canRead())
		{
			throw new IOException(myName + ":Cannot read source file '" + sourceFile + "'. Logged in as "
					+ System.getProperty("user.name"));
		}
		if (!source.canWrite())
		{
			throw new IOException(myName + ":Cannot write to source file '" + sourceFile + "'. Logged in as "
					+ System.getProperty("user.name") + ". Cannot move without " + "write permission to source file.");
		}
		if (logger.isDebugEnabled())
		{
			logger.debug("Moving file '" + sourceFile + "' to '" + destFile + "'");
		}
		if (sourceFile.equals(destFile))
		{
			logger.error("Source and destination the same - no move " + "required");
			return;
		}
		copyFile(sourceFile, destFile);
		if (!new File(sourceFile).delete())
		{
			logger.error("Copy completed, but unable to " + "delete source file '" + sourceFile + "'. Logged in as "
					+ System.getProperty("user.name"));
		}
	}

	/**
	 * 拿一个前缀和相对的路径,把这两个在一起转换成绝对路径
	 * 
	 * @param prefix
	 *            prefix
	 * @param originalPath
	 *            originalPath
	 * @return string
	 */
	public static String makeAbsolutePath(String prefix, String originalPath)
	{
		StringUtil.assertNotBlank(originalPath, "Original path may not be blank here");
		prefix = StringUtil.notNull(prefix);
		originalPath = originalPath.replace('\\', '/');
		prefix = prefix.replace('\\', '/');
		if (originalPath.startsWith("/"))
		{
			return originalPath;
		}
		/* Check for a drive specification for windows-type path */
		if (":".equals(originalPath.substring(1, 2)))
		{
			return originalPath;
		}
		/* Otherwise... */
		/* Make sure the prefix ends with a "/" */
		if (!prefix.endsWith("/"))
		{
			prefix = prefix + "/";
		}
		/* and put the two together */
		return prefix + originalPath;
	} /* makeAbsolutePath(String, String) */

	/**
	 * 读取文件内容，并替换指定占位符
	 * 
	 * @param file
	 *            文件
	 * @param replaceWith
	 *            替换内容String数组，依次为：占位符1，替换内容1，占位符2，替换内容2，...
	 * @return 文件内容String
	 */
	public static String parseFileContent(File file, String[] replaceWith)
	{
		// 文件内容字符串
		String content = "";
		// 每次读入数据块
		char[] block = new char[4096];
		int chunk;
		CharArrayWriter out = new CharArrayWriter();
		FileReader in;
		try
		{
			in = new FileReader(file);
			while ((chunk = in.read(block)) > 0)
			{
				out.write(block, 0, chunk);
			}
			in.close();
			content = out.toString();
			out.close();
		}
		catch (FileNotFoundException e)
		{
			logger.error("文件[" + file.getName() + "]不存在:", e);
		}
		catch (IOException e)
		{
			logger.error("文件读写出错:", e);
		}
		// 用数组[i*2+1]位置的内容替换[i*2]位置的内容
		if (replaceWith != null && replaceWith.length > 1)
		{
			for (int i = 0; i < replaceWith.length / 2; i++)
			{
				content = content.replaceAll(replaceWith[i * 2], replaceWith[i * 2 + 1]);
			}
		}
		if (logger.isDebugEnabled())
		{
			logger.debug("mail content:" + content);
		}
		return content;
	}

	/**
	 * 创建完整目录
	 * 
	 * @param filePath
	 *            目录地址
	 * @return 结果
	 */
	public static boolean makeFilePath(String filePath)
	{
		File path = new File(filePath);
		if (!path.exists())
		{
			return path.mkdirs();
		}
		return true;
	}

	private static Map<String, String> extToContentType = new ConcurrentHashMap<String, String>(512);
	static
	{
		extToContentType.put("jpg", "image/jpg");
		extToContentType.put("jpeg", "image/jpeg");
		extToContentType.put("bmp", "image/bmp");
		extToContentType.put("png", "image/png");
		extToContentType.put("gif", "image/gif");
		extToContentType.put("tif", "image/tif");
		extToContentType.put("tiff", "image/tiff");
		extToContentType.put("rar", "application/rar");
		extToContentType.put("zip", "application/zip");
		extToContentType.put("pdf", "application/pdf");
		extToContentType.put("txt", "text/plain");
		extToContentType.put("xml", "text/xml");
		extToContentType.put("doc", "application/msword");
		extToContentType.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		extToContentType.put("ppt", "application/vnd.ms-powerpoint");
		extToContentType.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
		extToContentType.put("xls", "application/vnd.ms-excel");
		extToContentType.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	}

	public static String getContentType(String fileName)
	{
		String ext = extractFileExt(fileName);
		String contentType = extToContentType.get(ext);
		if (contentType == null)
		{
			contentType = "application/octet-stream";
		}
		return contentType;
	}

	// ContentDisposition
	public static String extractFileName(String filePath)
	{
		int a = filePath.lastIndexOf("/");
		if (a > 0)
		{
			return filePath.substring(a + 1);
		}
		else
		{
			return null;
		}
	}

	/***************************************************************************
	 * 提取文件的后缀
	 * 
	 * @param filePath
	 * @return
	 */
	public static String extractFileExt(String filePath)
	{
		int a = filePath.lastIndexOf(".");
		if (a > 0)
		{
			return filePath.substring(a + 1);
		}
		else
		{
			return "";
		}
	}

	/**
	 * 删除指定路径文件
	 * 
	 * @param pathname
	 * @return
	 */
	public static boolean deleteQuietly(String pathname)
	{
		File file = new File(pathname);
		if (file.exists())
		{
			return FileUtils.deleteQuietly(file);
		}
		else
		{
			return false;
		}
	}
}
