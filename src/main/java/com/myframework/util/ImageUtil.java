package com.myframework.util;

/**
 * author zw 2014-10-29
 */

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.springframework.web.multipart.MultipartFile;

public class ImageUtil {
    /**
     * 几种常见的图片格式
     */
    public static String IMAGE_TYPE_GIF = "gif";// 图形交换格式
    public static String IMAGE_TYPE_JPG = "jpg";// 联合照片专家组
    public static String IMAGE_TYPE_JPEG = "jpeg";// 联合照片专家组
    public static String IMAGE_TYPE_BMP = "bmp";// 英文Bitmap（位图）的简写，它是Windows操作系统中的标准图像文件格式
    public static String IMAGE_TYPE_PNG = "png";// 可移植网络图形
    public static String IMAGE_TYPE_PSD = "psd";// Photoshop的专用格式Photoshop
    private static int MAX_WIDTH = 1500;
    private static String IMAGE_Ext = "gif,jpg,jpeg,png,bmp"; //允许上传的图片

    public BufferedImage getImage() {
        return image;
    }

    //对象内变量
    private BufferedImage image = null;

    public ImageUtil load(File imageFile) throws IOException {
        this.image = ImageIO.read(imageFile);
        return this;
    }

    public ImageUtil load(InputStream inputStream) throws IOException {
        this.image = ImageIO.read(inputStream);
        return this;
    }

    public int getImageWidth() {
        return image.getWidth();
    }

    public int getImageHeight() {
        return image.getHeight();
    }

    public void zoom(int tarWidth,int tarHeight) throws IOException {
        this.image = Thumbnails.of(this.image)
                .size(tarWidth,tarHeight)
                .keepAspectRatio(true)
                .outputQuality(1.0f)
                .asBufferedImage();
    }

    public void cut(int x,int y,int tarWidth,int tarHeight) throws IOException {
        this.image = Thumbnails.of(this.image)
                .sourceRegion(x,y,tarWidth,tarHeight)
                .size(tarWidth,tarHeight)
                .outputQuality(1.0f)
                .asBufferedImage();
    }

    public void rotate(double d) throws IOException {
        this.image = Thumbnails.of(this.image)
                .scale(1.0)
                .rotate(d)
                .outputQuality(1.0f)
                .asBufferedImage();
    }

    public void save(String filepath, String formatName) throws IOException {
        Thumbnails.of(this.image)
                .scale(1.0)
                .outputQuality(0.95f)
                .outputFormat(formatName)
                .toFile(filepath);
    }

    public void copy( BufferedImage srcImage,int dst_x, int dst_y, int src_x, int src_y, int dst_width, int dst_height, int src_width, int src_height){
        //
        Image image = srcImage.getScaledInstance(src_width, src_height, Image.SCALE_DEFAULT);
        // 四个参数分别为图像起点坐标和宽高
        // 即: CropImageFilter(int x,int y,int width,int height)
        ImageFilter cropFilter = new CropImageFilter(src_x, src_y, dst_width, dst_height);
        Image img = Toolkit.getDefaultToolkit().createImage(
                new FilteredImageSource(image.getSource(),
                        cropFilter));
        BufferedImage tag = new BufferedImage(dst_width, dst_height, BufferedImage.TYPE_INT_RGB);
        Graphics g = tag.getGraphics();
        g.drawImage(img, dst_x, dst_y, dst_width, dst_height, null); // 绘制切割后的图
        g.dispose();
        this.image = tag;
    }

    /*
      * imgFile 图片文件
      * root 图片路径
      * type 图片类型
      * photo 文件名
    * */
    public static Map uploadPhoto(MultipartFile imgFile,String root) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String today = formatter.format(new Date());
        String imgroot = today + "/";
        root = root + imgroot;
        // 目录不存在创建文件夹
        File fileDir = new File(root);
        if (!fileDir.getParentFile().exists()) {
            fileDir.getParentFile().mkdirs();// 目录不存在的情况下，会抛出异常
        }
        //
        String fileName = imgFile.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
        ext = ext.toLowerCase();

        fileName = UUID.randomUUID().toString() + "." + ext;
        File file = new File(root+fileName);
        Map map = new HashMap();
        if(Arrays.<String>asList(IMAGE_Ext.split(",")).contains(ext)) {                      //如果扩展名属于允许上传的类型，则创建文件
            //创建目录
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            //
            try {
                imgFile.transferTo(file);                   //保存上传的文件
                BufferedImage sourceImg = ImageIO.read(file);
                int picWidth = sourceImg.getWidth();
                int picHeight = sourceImg.getHeight();
                if(picWidth > MAX_WIDTH) {
                    picHeight = picHeight * MAX_WIDTH / picWidth;
                    picWidth = MAX_WIDTH;
                    sourceImg = resize(sourceImg, picWidth, picHeight);
                    writeFile(sourceImg, file, ext);
                }
                map.put("width", picWidth);
                map.put("height", picHeight);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        map.put("photo", today + "/" + fileName);
        return map;
    }
    /**保存图片
     * @param photoWidth 结果图片的宽度
     * @param photoHeight 结果图片的高度
     **/
    public static String subPhoto(Map<String,Float> map, String srcPhotoPath, String destRoot,int photoWidth, int photoHeight) throws IOException {
        //
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String today = formatter.format(new Date());
        String imgroot = today + "/";
        destRoot = destRoot + imgroot;
        // 目录不存在创建文件夹
        File fileDir = new File(destRoot);
        if (!fileDir.exists() && !fileDir.isDirectory()) {
            fileDir.mkdirs();// 目录不存在的情况下，会抛出异常
        }
        // 读取源图像
        BufferedImage bi = ImageIO.read(new File(srcPhotoPath));
        int biWidth = bi.getWidth();
        int biHeight = bi.getHeight();
        float srcWidth = map.get("width");
        float x = map.get("x");
        float y = map.get("y");
        float w = map.get("w");
        float h = map.get("h");

        x = x * biWidth / srcWidth;
        y = y * biWidth / srcWidth;
        w = w * biWidth / srcWidth;
        h = h * biWidth / srcWidth;

        if((int)w + (int)x > biWidth)
            w = biWidth - x;
        if((int)h + (int)y > biHeight)
            h = biHeight - y;
        //
        String ext = srcPhotoPath.substring(srcPhotoPath.lastIndexOf(".")+1,srcPhotoPath.length());
        ext = ext.toLowerCase();
        String filename = UUID.randomUUID().toString() + "." + ext;
        //
        bi = bi.getSubimage((int)x, (int)y, (int)w, (int)h);
        BufferedImage imgBuff = resize(bi, photoWidth, photoHeight);
        Thumbnails.of(imgBuff)
                .sourceRegion(Positions.CENTER,photoWidth,photoHeight)
                .size(photoWidth,photoHeight)
                .outputQuality((float)0.9)
                .toFile(destRoot + filename);
        return today + "/" + filename;
    }

    /**
     * 实现图像的等比缩放和缩放后的截取
     * @param inFilePath 要截取文件的路径
     * @param outFilePath 截取后输出的路径
     * @param width 要截取宽度
     * @param hight 要截取的高度
     * @param proportion  截取大于图片 是否进行放大
     * @throws Exception
     */

    public static void scaleAndCut(String inFilePath, String outFilePath,
                                   int width, int height, int radius, float quality, boolean proportion)throws Exception {
        //
        File srcfile = new File(inFilePath);
        File saveFile = new File(outFilePath);
        // 目录已存在创建文件夹
        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs();// 目录不存在的情况下，会抛出异常
        }
        //
        if(!saveFile.exists()){
            try {
                saveFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            return;
        }
        //
        String fileName = saveFile.getName();
        String formatName = fileName.substring(fileName.lastIndexOf('.') + 1);
        //
        IMOperation op = new IMOperation();
        op.addImage();
        //
        BufferedImage img = ImageIO.read(srcfile);
        // 原图的大小
        int sw = img.getWidth();
        int sh = img.getHeight();
        int tw = width;
        int th = height;
        if (width > 0 || height > 0) {
            //
            double ratio = (double) sw / sh;
            double sx = (double) width / sw;
            double sy = (double) height / sh;
            // 如果原图像的大小小于要缩放的图像大小，直接将要缩放的图像复制过去
            if (sw > width && sh > height) {
                if (Math.abs(sx-1) < Math.abs(sy-1)) {
                    th = (int) (width / ratio);
                } else {
                    tw = (int) (height * ratio);
                }
                //img = Thumbnails.of(img).size(tw, th).asBufferedImage();
                op.resize(tw,th);
            }else{
                if(proportion){
                    if(sw < width && sh < height){
                        if (Math.abs(sx-1) < Math.abs(sy-1)) {
                            tw = (int) (height * ratio);
                        } else {
                            th = (int) (width / ratio);
                        }
                    }else if(sw < width && sh>= height){
                        th = (int) (width / ratio);
                    }else if(sw >= width && sh < height){
                        tw = (int) (height * ratio);
                    }
                    //img = Thumbnails.of(img).size(tw, th).asBufferedImage();
                    op.resize(tw,th);
                }
            }
        }
        if(tw > width || th>height){
//            img = Thumbnails.of(img)
//                    .sourceRegion(Positions.CENTER, width, height)
//                    .size(width,height).asBufferedImage();
            int startX = 0;
            int startY = 0;
            if(tw > width){
                startX = (int)((tw-width) / 2);
            } else if(th>height){
                startY = (int)((th-height) / 2);
            }
            op.crop(width,height,startX,startY);
            //op.gravity("center").extent(width,height);//此方法回导致图片有莫名白边 原因不详
        }
        if(quality < 1){
//            img = Thumbnails.of(img)
//                    .size(width, height)
//                    .outputQuality(quality).asBufferedImage();
            op.quality((double)(quality * 100));
        }
        op.strip();
        op.addImage();
        ConvertCmd convert = new ConvertCmd(true); //true 代表使用gm
        convert.run(op,img,outFilePath);
        //
        if(radius > 0) {
            //进行圆边切图
            saveFile = new File(outFilePath);
            img = ImageIO.read(saveFile);
            img = makeRoundedCorner(img, radius);
            formatName = "png";
            ImageIO.write(img,formatName,saveFile);
        }
    }

    /**
     * 实现图像的等比缩放
     * @param source
     * @param targetW
     * @param targetH
     * @return
     */
    private static BufferedImage resize(BufferedImage source, int targetW,
                                        int targetH) {
        // targetW，targetH分别表示目标长和宽
        int type = source.getType();
        BufferedImage target = null;
        double sx = (double) targetW / source.getWidth();
        double sy = (double) targetH / source.getHeight();
        // 这里想实现在targetW，targetH范围内实现等比缩放。如果不需要等比缩放
        // 则将下面的if else语句注释即可
        if (sx < sy) {
            sx = sy;
            targetW = (int) (sx * source.getWidth());
        } else {
            sy = sx;
            targetH = (int) (sy * source.getHeight());
        }
        if (type == BufferedImage.TYPE_CUSTOM) { // handmade
            ColorModel cm = source.getColorModel();
            WritableRaster raster = cm.createCompatibleWritableRaster(targetW,
                    targetH);
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();
            target = new BufferedImage(cm, raster, alphaPremultiplied, null);
        } else {
            target = new BufferedImage(targetW, targetH, type);
        }
        Graphics2D g = target.createGraphics();
        //
        //写入背景
        //g.drawImage(ImageIO.read(new File("ok/blank.png")), 0, 0, null);
        // smoother than exlax:
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
        g.dispose();
        return target;
    }

    //加圆角
    public static BufferedImage makeRoundedCorner(BufferedImage image,
                                                  int cornerRadius) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();

        // This is what we want, but it only does hard-clipping, i.e. aliasing
        // g2.setClip(new RoundRectangle2D ...)

        // so instead fake soft-clipping by first drawing the desired clip shape
        // in fully opaque white with antialiasing enabled...
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius,
                cornerRadius));

        // ... then compositing the image on top,
        // using the white shape from above as alpha source
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);

        g2.dispose();
        return output;
    }

    public static void writeFile(BufferedImage imgBuf, File destFile, String ext) throws IOException {
        File parent = destFile.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        ImageIO.write(imgBuf, ext, destFile);
    }

    /**
     * 缩放图像（按比例缩放）
     * @param srcImageFile 源图像文件地址
     * @param result 缩放后的图像地址
     * @param scale 缩放比例
     * @param flag 缩放选择:true 放大; false 缩小;
     */
    public final static void scale(String srcImageFile, String result,
                                   int scale, boolean flag) {
        try {
            BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
            int width = src.getWidth(); // 得到源图宽
            int height = src.getHeight(); // 得到源图长
            if (flag) {// 放大
                width = width * scale;
                height = height * scale;
            } else {// 缩小
                width = width / scale;
                height = height / scale;
            }
            Image image = src.getScaledInstance(width, height,
                    Image.SCALE_DEFAULT);
            BufferedImage tag = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 缩放图像（按高度和宽度缩放）
     * @param srcImageFile 源图像文件地址
     * @param result 缩放后的图像地址
     * @param height 缩放后的高度
     * @param width 缩放后的宽度
     * @param bb 比例不对时是否需要补白：true为补白; false为不补白;
     */
    public final static void scale2(String srcImageFile, String result, int height, int width, boolean bb) {
        try {
            double ratio = 0.0; // 缩放比例
            File f = new File(srcImageFile);
            BufferedImage bi = ImageIO.read(f);
            Image itemp = bi.getScaledInstance(width, height, bi.SCALE_SMOOTH);
            // 计算比例
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                if (bi.getHeight() > bi.getWidth()) {
                    ratio = (new Integer(height)).doubleValue()
                            / bi.getHeight();
                } else {
                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();
                }
                AffineTransformOp op = new AffineTransformOp(AffineTransform
                        .getScaleInstance(ratio, ratio), null);
                itemp = op.filter(bi, null);
            }
            if (bb) {//补白
                BufferedImage image = new BufferedImage(width, height,
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == itemp.getWidth(null))
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                else
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                g.dispose();
                itemp = image;
            }
            ImageIO.write((BufferedImage) itemp, "JPEG", new File(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 图像切割(按指定起点坐标和宽高切割)
     * @param srcImageFile 源图像地址
     * @param result 切片后的图像地址
     * @param x 目标切片起点坐标X
     * @param y 目标切片起点坐标Y
     * @param width 目标切片宽度
     * @param height 目标切片高度
     */
    public final static void cut(String srcImageFile, String result,
                                 int x, int y, int width, int height) {
        try {
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getWidth(); // 源图宽度
            int srcHeight = bi.getHeight(); // 源图高度
            if (srcWidth > 0 && srcHeight > 0) {
                Image image = bi.getScaledInstance(srcWidth, srcHeight,
                        Image.SCALE_DEFAULT);
                // 四个参数分别为图像起点坐标和宽高
                // 即: CropImageFilter(int x,int y,int width,int height)
                ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
                Image img = Toolkit.getDefaultToolkit().createImage(
                        new FilteredImageSource(image.getSource(),
                                cropFilter));
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
                g.dispose();
                // 输出为文件
                File file = new File(result);
                // 目录已存在创建文件夹
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();// 目录不存在的情况下，会抛出异常
                }
                ImageIO.write(tag, "JPEG", new File(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图像切割（指定切片的行数和列数）
     * @param srcImageFile 源图像地址
     * @param descDir 切片目标文件夹
     * @param rows 目标切片行数。默认2，必须是范围 [1, 20] 之内
     * @param cols 目标切片列数。默认2，必须是范围 [1, 20] 之内
     */
    public final static void cut2(String srcImageFile, String descDir,
                                  int rows, int cols) {
        try {
            if(rows<=0||rows>20) rows = 2; // 切片行数
            if(cols<=0||cols>20) cols = 2; // 切片列数
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // 源图宽度
            int srcHeight = bi.getWidth(); // 源图高度
            if (srcWidth > 0 && srcHeight > 0) {
                Image img;
                ImageFilter cropFilter;
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                int destWidth = srcWidth; // 每张切片的宽度
                int destHeight = srcHeight; // 每张切片的高度
                // 计算切片的宽度和高度
                if (srcWidth % cols == 0) {
                    destWidth = srcWidth / cols;
                } else {
                    destWidth = (int) Math.floor(srcWidth / cols) + 1;
                }
                if (srcHeight % rows == 0) {
                    destHeight = srcHeight / rows;
                } else {
                    destHeight = (int) Math.floor(srcWidth / rows) + 1;
                }
                // 循环建立切片
                // 改进的想法:是否可用多线程加快切割速度
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        // 四个参数分别为图像起点坐标和宽高
                        // 即: CropImageFilter(int x,int y,int width,int height)
                        cropFilter = new CropImageFilter(j * destWidth, i * destHeight,
                                destWidth, destHeight);
                        img = Toolkit.getDefaultToolkit().createImage(
                                new FilteredImageSource(image.getSource(),
                                        cropFilter));
                        BufferedImage tag = new BufferedImage(destWidth,
                                destHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics g = tag.getGraphics();
                        g.drawImage(img, 0, 0, null); // 绘制缩小后的图
                        g.dispose();
                        // 输出为文件
                        ImageIO.write(tag, "JPEG", new File(descDir
                                + "_r" + i + "_c" + j + ".jpg"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 图像切割（指定切片的宽度和高度）
     * @param srcImageFile 源图像地址
     * @param descDir 切片目标文件夹
     * @param destWidth 目标切片宽度。默认200
     * @param destHeight 目标切片高度。默认150
     */
    public final static void cut3(String srcImageFile, String descDir,
                                  int destWidth, int destHeight) {
        try {
            if(destWidth<=0) destWidth = 200; // 切片宽度
            if(destHeight<=0) destHeight = 150; // 切片高度
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // 源图宽度
            int srcHeight = bi.getWidth(); // 源图高度
            if (srcWidth > destWidth && srcHeight > destHeight) {
                Image img;
                ImageFilter cropFilter;
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                int cols = 0; // 切片横向数量
                int rows = 0; // 切片纵向数量
                // 计算切片的横向和纵向数量
                if (srcWidth % destWidth == 0) {
                    cols = srcWidth / destWidth;
                } else {
                    cols = (int) Math.floor(srcWidth / destWidth) + 1;
                }
                if (srcHeight % destHeight == 0) {
                    rows = srcHeight / destHeight;
                } else {
                    rows = (int) Math.floor(srcHeight / destHeight) + 1;
                }
                // 循环建立切片
                // 改进的想法:是否可用多线程加快切割速度
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        // 四个参数分别为图像起点坐标和宽高
                        // 即: CropImageFilter(int x,int y,int width,int height)
                        cropFilter = new CropImageFilter(j * destWidth, i * destHeight,
                                destWidth, destHeight);
                        img = Toolkit.getDefaultToolkit().createImage(
                                new FilteredImageSource(image.getSource(),
                                        cropFilter));
                        BufferedImage tag = new BufferedImage(destWidth,
                                destHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics g = tag.getGraphics();
                        g.drawImage(img, 0, 0, null); // 绘制缩小后的图
                        g.dispose();
                        // 输出为文件
                        ImageIO.write(tag, "JPEG", new File(descDir
                                + "_r" + i + "_c" + j + ".jpg"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 图像类型转换：GIF->JPG、GIF->PNG、PNG->JPG、PNG->GIF(X)、BMP->PNG
     * @param srcImageFile 源图像地址
     * @param formatName 包含格式非正式名称的 String：如JPG、JPEG、GIF等
     * @param destImageFile 目标图像地址
     */
    public final static void convert(String srcImageFile, String formatName, String destImageFile) {
        try {
            File f = new File(srcImageFile);
            f.canRead();
            f.canWrite();
            BufferedImage src = ImageIO.read(f);
            ImageIO.write(src, formatName, new File(destImageFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 彩色转为黑白
     * @param srcImageFile 源图像地址
     * @param destImageFile 目标图像地址
     */
    public final static void gray(String srcImageFile, String destImageFile) {
        try {
            BufferedImage src = ImageIO.read(new File(srcImageFile));
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            ColorConvertOp op = new ColorConvertOp(cs, null);
            src = op.filter(src, null);
            ImageIO.write(src, "JPEG", new File(destImageFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 给图片添加文字水印
     * @param pressText 水印文字
     * @param srcImageFile 源图像地址
     * @param destImageFile 目标图像地址
     * @param fontName 水印的字体名称
     * @param fontStyle 水印的字体样式
     * @param color 水印的字体颜色
     * @param fontSize 水印的字体大小
     * @param x 修正值
     * @param y 修正值
     * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
     */
    public final static void pressText(String pressText,
                                       String srcImageFile, String destImageFile, String fontName,
                                       int fontStyle, Color color, int fontSize,int x,
                                       int y, float alpha) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            g.setColor(color);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            // 在指定坐标绘制水印文字
            g.drawString(pressText, (width - (getLength(pressText) * fontSize))
                    / 2 + x, (height - fontSize) / 2 + y);
            g.dispose();
            ImageIO.write((BufferedImage) image, "JPEG", new File(destImageFile));// 输出到文件流
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 给图片添加文字水印
     * @param pressText 水印文字
     * @param srcImageFile 源图像地址
     * @param destImageFile 目标图像地址
     * @param fontName 字体名称
     * @param fontStyle 字体样式
     * @param color 字体颜色
     * @param fontSize 字体大小
     * @param x 修正值
     * @param y 修正值
     * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
     */
    public final static void pressText2(String pressText, String srcImageFile,String destImageFile,
                                        String fontName, int fontStyle, Color color, int fontSize, int x,
                                        int y, float alpha) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            g.setColor(color);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            // 在指定坐标绘制水印文字
            g.drawString(pressText, (width - (getLength(pressText) * fontSize))
                    / 2 + x, (height - fontSize) / 2 + y);
            g.dispose();
            ImageIO.write((BufferedImage) image, "JPEG", new File(destImageFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 给图片添加图片水印
     * @param pressImg 水印图片
     * @param srcImageFile 源图像地址
     * @param destImageFile 目标图像地址
     * @param x 修正值。 默认在中间
     * @param y 修正值。 默认在中间
     * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
     */
    public final static void pressImage(String pressImg, String srcImageFile,String destImageFile,
                                        int x, int y, float alpha) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);
            // 水印文件
            Image src_biao = ImageIO.read(new File(pressImg));
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            g.drawImage(src_biao, (wideth - wideth_biao) / 2,
                    (height - height_biao) / 2, wideth_biao, height_biao, null);
            // 水印文件结束
            g.dispose();
            ImageIO.write((BufferedImage) image,  "JPEG", new File(destImageFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 计算text的长度（一个中文算两个字符）
     * @param text
     * @return
     */
    public final static int getLength(String text) {
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            if (new String(text.charAt(i) + "").getBytes().length > 1) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length / 2;
    }

    /**
     * 图片旋转
     *
     * @param imagePath
     *            源图片路径
     * @param newPath
     *            处理后图片路径
     * @param degree
     *            旋转角度
     */
    public static boolean rotate(String imagePath, String newPath, double degree) {
        boolean flag = false;
        try {
            // 1.将角度转换到0-360度之间
            degree = degree % 360;
            if (degree <= 0) {
                degree = 360 + degree;
            }
            IMOperation op = new IMOperation();
            op.addImage(imagePath);
            op.rotate(degree);
            op.addImage(newPath);
            ConvertCmd cmd = new ConvertCmd(true);
            cmd.run(op);
            flag = true;
        } catch (Exception e) {
            flag = false;
            System.out.println("图片旋转失败!");
        }
        return flag;
    }

    /**
     * 程序入口：用于测试
     * @param args
     */
    public static void main(String[] args) throws Exception {
        for(double i = 20; i < 360; i = i + 20) {
            ImageUtil.rotate("/Users/zorro/Desktop/1.jpg", "/Users/zorro/Desktop/1_"+i+".jpg", i);
        }
//        ImageUtil.scaleAndCut("/Users/zw/Desktop/o.jpg","/Users/zw/Desktop/new1.jpg",590,370,0,0.88f,true);
        /*
        1-缩放图像：
        方法一：按比例缩放
        ImageUtil.scale("e:/abc.jpg", "e:/abc_scale.jpg", 2, true);//测试OK
        // 方法二：按高度和宽度缩放
        ImageUtil.scale2("e:/abc.jpg", "e:/abc_scale2.jpg", 500, 300, true);//测试OK


        // 2-切割图像：
        // 方法一：按指定起点坐标和宽高切割
        ImageUtil.cut("e:/abc.jpg", "e:/abc_cut.jpg", 0, 0, 400, 400 );//测试OK
        // 方法二：指定切片的行数和列数
        ImageUtil.cut2("e:/abc.jpg", "e:/", 2, 2 );//测试OK
        // 方法三：指定切片的宽度和高度
        ImageUtil.cut3("e:/abc.jpg", "e:/", 300, 300 );//测试OK


        // 3-图像类型转换：
        ImageUtil.convert("e:/abc.jpg", "GIF", "e:/abc_convert.gif");//测试OK


        // 4-彩色转黑白：
        ImageUtil.gray("e:/abc.jpg", "e:/abc_gray.jpg");//测试OK


        // 5-给图片添加文字水印：
        // 方法一：
        ImageUtil.pressText("我是水印文字","e:/abc.jpg","e:/abc_pressText.jpg","宋体",Font.BOLD,Color.white,80, 0, 0, 0.5f);//测试OK
        // 方法二：
        ImageUtil.pressText2("我也是水印文字", "e:/abc.jpg","e:/abc_pressText2.jpg", "黑体", 36, Color.white, 80, 0, 0, 0.5f);//测试OK

        // 6-给图片添加图片水印：
        ImageUtil.pressImage("e:/abc2.jpg", "e:/abc.jpg","e:/abc_pressImage.jpg", 0, 0, 0.5f);//测试OK
        */
    }
}

