package com.myframework.util;

import java.io.*;
import java.util.Enumeration;

import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZipUtil
{
	private static final Logger logger = LoggerFactory.getLogger(ZipUtil.class);
	public static final int BUFFER = 8192;
	public static final String ENCODING = "GBK";

	/**
	 * 压缩文件
	 * 
	 * @param zipFile
	 * @param files
	 */
	public static void zipFiles(String zipFile, File... files)
	{
		ZipOutputStream out = null;
		BufferedInputStream input = null;
		try
		{
			FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
			out = new ZipOutputStream(fileOutputStream);
			out.setEncoding(ENCODING);
			byte data[] = new byte[BUFFER];
			for (File file : files)
			{
				if (file.exists())
				{
					input = new BufferedInputStream(new FileInputStream(file));
					ZipEntry entry = new ZipEntry(file.getName());
					out.putNextEntry(entry);
					int count;
					while ((count = input.read(data, 0, BUFFER)) != -1)
					{
						out.write(data, 0, count);
					}
					out.flush();
					out.closeEntry();
					IOUtils.closeQuietly(input);
				}
				else
				{
					logger.error("压缩文件{}不存在", file.getName());
				}

			}
		}
		catch (FileNotFoundException e)
		{
			logger.error("压缩文件路径{}错误", zipFile, e);
		}
		catch (IOException e)
		{
			logger.error("文件压缩错误", e);
		}
		finally
		{
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(out);
		}
	}

	/**
	 * 解压
	 * 
	 * @param zipFile
	 * @throws IOException
	 */
	public static void unZipFiles(File zipFile) throws IOException
	{
		InputStream input = null;
		OutputStream out = null;
		ZipFile zf = null;
		byte[] buf = new byte[BUFFER];
		try
		{
			zf = new ZipFile(zipFile, ENCODING);
			for (Enumeration<?> entries = zf.getEntries(); entries.hasMoreElements();)
			{
				ZipEntry entry = (ZipEntry) entries.nextElement();
				input = zf.getInputStream(entry);
				String zipEntryName = entry.getName();
				out = new FileOutputStream(zipFile.getParent() + File.separator + zipEntryName);
				int len;
				while ((len = input.read(buf)) > 0)
				{
					out.write(buf, 0, len);
				}
				input.close();
				out.close();
			}
			zf.close();
		}
		finally
		{
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(out);
			if (zf != null)
			{
				try
				{
					zf.close();
				}
				catch (IOException e)
				{
				}
			}
		}
	}

	public static void zipFile(String srcPathName, String zipFile)
	{
		zipFile(srcPathName, zipFile, false);
	}

	public static void zipFile(String srcPathName, String zipFile, boolean isDelSourceFile)
	{
		File file = new File(srcPathName);
		if (!file.exists())
		{
			throw new RuntimeException(srcPathName + "不存在！");
		}
		FileOutputStream fileOutputStream = null;
		ZipOutputStream out = null;
		try
		{
			fileOutputStream = new FileOutputStream(zipFile);
			out = new ZipOutputStream(fileOutputStream);
			out.setEncoding("GBK");
			String basedir = "";
			if (file.isDirectory())
			{
				zipDirectory(file, out, basedir);
			}
			else
			{
				zipFile(file, out, basedir);
			}
			out.flush();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
				out.close();
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		}
		if (isDelSourceFile)
		{
			file.delete();
		}
	}

	/** 压缩一个目录 */
	private static void zipDirectory(File dir, ZipOutputStream out, String basedir)
	{
		if (!dir.exists())
			return;

		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++)
		{
			/* 递归 */
			zipFile(files[i], out, basedir + dir.getName() + "/");
		}
	}

	/** 压缩一个文件 */
	private static void zipFile(File file, ZipOutputStream out, String basedir)
	{
		if (!file.exists())
		{
			return;
		}
		try
		{
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			ZipEntry entry = new ZipEntry(basedir + file.getName());
			out.putNextEntry(entry);
			int count;
			byte data[] = new byte[BUFFER];
			while ((count = bis.read(data, 0, BUFFER)) != -1)
			{
				out.write(data, 0, count);
			}
			bis.close();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/** 压缩一个文件 */
	private static void zipFile(ZipOutputStream out, File... files)
	{
		for (File file : files)
		{

			if (!file.exists())
			{
				continue;
			}
			try
			{
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
				ZipEntry entry = new ZipEntry(file.getName());
				out.putNextEntry(entry);
				int count;
				byte data[] = new byte[BUFFER];
				while ((count = bis.read(data, 0, BUFFER)) != -1)
				{
					out.write(data, 0, count);
				}
				bis.close();
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
	}

}
