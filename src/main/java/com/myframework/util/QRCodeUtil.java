package com.myframework.util;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
import java.util.Hashtable;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码工具类
 * 
 */
public class QRCodeUtil
{
	private static final Logger logger = LoggerFactory.getLogger(QRCodeUtil.class);
	private static final String CHARSET = "utf-8";
	private static final String FORMAT_NAME = "JPG";
	// 二维码尺寸
	private static final int QRCODE_SIZE = 300;
	// LOGO宽度
	private static final int LOGO_WIDTH = 60;
	// LOGO高度
	private static final int LOGO_HEIGHT = 60;

	/***
	 * 
	 * @param content
	 * @param imgPath
	 * @param needCompress
	 * @return
	 * @deprecated 改用*Faster
	 * @throws Exception
	 */
	@Deprecated
	public static BufferedImage createImage(String content, String imgPath, boolean needCompress) throws Exception
	{
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
		hints.put(EncodeHintType.MARGIN, 1);
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE,
				hints);
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
			}
		}
		if (imgPath == null || "".equals(imgPath))
		{
			return image;
		}
		// 插入图片
		QRCodeUtil.insertImage(image, imgPath, needCompress);
		return image;
	}

	/**
	 * 更快的生成二维码
	 * 
	 * @param content
	 * @param imgPath
	 * @param needCompress
	 * @return
	 * @deprecated needCompressLogo参数不处理
	 * @throws Exception
	 */
	@Deprecated
	public static BufferedImage createImageFaster(String content, String logoPath, boolean needCompressLogo, int size)
		throws Exception
	{
		return createImageFaster(content, logoPath, size);
	}

	public static BufferedImage createImageFaster(String content, String logoPath, int size) throws Exception
	{
		Image logoImage = null;

		if (!StringUtil.isEmpty(logoPath))
		{
			try
			{
				if (logoPath.indexOf("http") >= 0)
				{
					logoImage = ImageIO.read(new URL(logoPath));
				}
				else
				{
					logoImage = ImageIO.read(new File(logoPath));
				}
			}
			catch (Exception e)
			{
				logger.error("读取二维码logo失败:" + logoPath, e);
			}
		}

		BufferedImage image = createImageFaster(content, logoImage, size);
		return image;
	}

	/**
	 * 更快的生成二维码
	 * 
	 * @param content
	 * @param logoImage
	 * @param needCompress
	 * @return
	 * @throws Exception
	 */
	public static BufferedImage createImageFaster(String content, Image logoImage, int size) throws Exception
	{
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
		hints.put(EncodeHintType.MARGIN, 1);
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints);
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 直接操作对应位置数据
		Raster raster = image.getRaster();
		int[] dstbuf = ((DataBufferInt) raster.getDataBuffer()).getData();
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				dstbuf[y * width + x] = bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
			}
		}
		if (logoImage != null)
		{
			QRCodeUtil.insertLogo(image, logoImage);
		}
		return image;
	}

	/**
	 * 插入LOGO
	 * 
	 * @param source
	 *            二维码图片
	 * @param imgPath
	 *            LOGO图片地址
	 * @param needCompress
	 *            是否压缩
	 * @throws Exception
	 */
	private static void insertLogo(BufferedImage source, Image logoImage) throws Exception
	{
		int logoWidth = logoImage.getWidth(null);
		int logoHeight = logoImage.getHeight(null);
		int srcWidth = source.getWidth();
		int srcHeight = source.getHeight();
		// 插入LOGO
		Graphics2D graph = source.createGraphics();
		int x = (srcWidth - logoWidth) / 2;
		int y = (srcHeight - logoHeight) / 2;
		graph.drawImage(logoImage, x, y, logoWidth, logoHeight, null);
		Shape shape = new RoundRectangle2D.Float(x, y, logoWidth, logoHeight, 6, 6);
		graph.setStroke(new BasicStroke(3f));
		graph.draw(shape);
		graph.dispose();
	}

	public static Image compressLogo(Image logoSrc, int logoWidth, int logoHeigh)
	{
		long t1 = System.currentTimeMillis();
		int width = logoSrc.getWidth(null);
		int height = logoSrc.getHeight(null);
		boolean needCompress = false;
		Image logoSmall = null;
		if (width > logoWidth)
		{
			width = logoWidth;
			needCompress = true;
		}
		if (height > logoHeigh)
		{
			height = logoHeigh;
			needCompress = true;
		}
		if (needCompress)
		{
			logoSmall = logoSrc.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(logoSmall, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			long duration = System.currentTimeMillis() - t1;
			logger.info("压缩logo耗时:{}ms", duration);
		}
		else
		{
			logoSmall = logoSrc;
		}
		return logoSmall;
	}

	/**
	 * 插入LOGO
	 * 
	 * @param source
	 *            二维码图片
	 * @param imgPath
	 *            LOGO图片地址
	 * @param needCompress
	 *            是否压缩
	 * @deprecated 改用insertLogo
	 * @throws Exception
	 * 
	 */
	@Deprecated
	private static void insertImage(BufferedImage source, String imgPath, boolean needCompress) throws Exception
	{
		File file = new File(imgPath);
		if (!file.exists())
		{
			System.err.println("" + imgPath + "   该文件不存在！");
			return;
		}
		Image src = ImageIO.read(new File(imgPath));
		int width = src.getWidth(null);
		int height = src.getHeight(null);
		if (needCompress)
		{ // 压缩LOGO
			if (width > LOGO_WIDTH)
			{
				width = LOGO_WIDTH;
			}
			if (height > LOGO_HEIGHT)
			{
				height = LOGO_HEIGHT;
			}
			Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			src = image;
		}
		// 插入LOGO
		Graphics2D graph = source.createGraphics();
		int x = (QRCODE_SIZE - width) / 2;
		int y = (QRCODE_SIZE - height) / 2;
		graph.drawImage(src, x, y, width, height, null);
		Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
		graph.setStroke(new BasicStroke(3f));
		graph.draw(shape);
		graph.dispose();
	}

	/**
	 * 生成二维码(内嵌LOGO)
	 * 
	 * @param content
	 *            内容
	 * @param imgPath
	 *            LOGO地址
	 * @param destPath
	 *            存放目录
	 * @param needCompress
	 *            是否压缩LOGO
	 * @throws Exception
	 */
	@Deprecated
	public static void encode(String content, String imgPath, String destPath, boolean needCompress) throws Exception
	{
		BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress);
		mkdirs(destPath);
		String file = new Random().nextInt(99999999) + ".jpg";
		ImageIO.write(image, FORMAT_NAME, new File(destPath + "/" + file));
	}

	/**
	 * 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
	 * 
	 * @param destPath
	 *            存放目录
	 */
	public static void mkdirs(String destPath)
	{
		File file = new File(destPath);
		// 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
		if (!file.exists() && !file.isDirectory())
		{
			file.mkdirs();
		}
	}

	/**
	 * 生成二维码(内嵌LOGO)
	 * 
	 * @param content
	 *            内容
	 * @param imgPath
	 *            LOGO地址
	 * @param destPath
	 *            存储地址
	 * @throws Exception
	 */
	@Deprecated
	public static void encode(String content, String imgPath, String destPath) throws Exception
	{
		QRCodeUtil.encode(content, imgPath, destPath, false);
	}

	/**
	 * 生成二维码
	 * 
	 * @param content
	 *            内容
	 * @param destPath
	 *            存储地址
	 * @param needCompress
	 *            是否压缩LOGO
	 * @throws Exception
	 */
	@Deprecated
	public static void encode(String content, String destPath, boolean needCompress) throws Exception
	{
		QRCodeUtil.encode(content, null, destPath, needCompress);
	}

	/**
	 * 生成二维码
	 * 
	 * @param content
	 *            内容
	 * @param destPath
	 *            存储地址
	 * @throws Exception
	 */
	@Deprecated
	public static void encode(String content, String destPath) throws Exception
	{
		QRCodeUtil.encode(content, null, destPath, false);
	}

	/**
	 * 生成二维码(内嵌LOGO)
	 * 
	 * @param content
	 *            内容
	 * @param imgPath
	 *            LOGO地址
	 * @param output
	 *            输出流
	 * @param needCompress
	 *            是否压缩LOGO
	 * @throws Exception
	 */
	@Deprecated
	public static void encode(String content, String imgPath, OutputStream output, boolean needCompress)
		throws Exception
	{
		BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress);
		ImageIO.write(image, FORMAT_NAME, output);
	}

	/**
	 * 生成二维码
	 * 
	 * @param content
	 *            内容
	 * @param output
	 *            输出流
	 * @throws Exception
	 */
	@Deprecated
	public static void encode(String content, OutputStream output) throws Exception
	{
		QRCodeUtil.encode(content, null, output, false);
	}

	/**
	 * 解析二维码
	 * 
	 * @param file
	 *            二维码图片
	 * @return
	 * @throws Exception
	 */
	public static String decode(File file) throws Exception
	{
		BufferedImage image;
		image = ImageIO.read(file);
		if (image == null)
		{
			return null;
		}
		BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		Result result;
		Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
		hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
		result = new MultiFormatReader().decode(bitmap, hints);
		String resultStr = result.getText();
		return resultStr;
	}

	/**
	 * 解析二维码
	 * 
	 * @param path
	 *            二维码图片地址
	 * @return
	 * @throws Exception
	 */
	public static String decode(String path) throws Exception
	{
		return QRCodeUtil.decode(new File(path));
	}

	public static void main(String[] args) throws Exception
	{
		System.out.println("开始时间:" + DateUtil.date2String("hh-mm-ss", new Date()));
		for (int i = 0; i < 1; i++)
		{
			String content = "http://www.baidu.com";
			String fileName = DateUtil.date2String("hh-mm-ss-", new Date());
			String path = "E:\\image\\" + fileName + i + ".jpg";
			try
			{
				BufferedImage img = QRCodeUtil.createImageFaster(content, "E:\\aab.png", true, 1000);
				File fileDst = new File(path);
				OutputStream os = new FileOutputStream(fileDst);
				ImageIO.write(img, "JPEG", os);
				os.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		System.out.println("结束时间:" + DateUtil.date2String("hh-mm-ss", new Date()));
	}

	private static void saveToFile(Image logoImage)
	{
		// /****************debug**********************//
		int w = logoImage.getWidth(null);
		int h = logoImage.getHeight(null);

		// 首先创建一个BufferedImage变量，因为ImageIO写图片用到了BufferedImage变量。
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);

		// 再创建一个Graphics变量，用来画出来要保持的图片，及上面传递过来的Image变量
		Graphics g = bi.getGraphics();
		try
		{
			g.drawImage(logoImage, 0, 0, null);

			// 将BufferedImage变量写入文件中。
			ImageIO.write(bi, "png", new File("d:/qrcode_logo_s.png"));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// qrcode_logo_s.png
		// /****************debug**********************//
	}
}
