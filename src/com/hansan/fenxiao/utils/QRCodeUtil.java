//package com.hansan.fenxiao.utils;
//
//import java.awt.AlphaComposite;
//import java.awt.Color;
//import java.awt.Font;
//import java.awt.Graphics2D;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.util.HashMap;
//import java.util.Hashtable;
//import java.util.Map;
//
//import javax.imageio.ImageIO;
//
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.EncodeHintType;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.QRCodeWriter;
//import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
//
///**
// * 二维码生成工具
// * @author xinyu
// * @date 2019年6月30日
// */
//public class QRCodeUtil {
//
//	/**
//     * 生成包含字符串信息的二维码图片
//     * @param content 二维码携带信息
//     * @param qrCodeSize 二维码图片大小
//     * @param imageFormat 二维码的格式
//     * @param outputStream 输出流
//     * @throws WriterException 
//     * @throws IOException 
//     */
//    public static boolean createQRCode(String content, int qrCodeSize, String imageFormat, OutputStream output) throws WriterException, IOException{  
//            //设置二维码纠错级别ＭＡＰ
//            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();  
//            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);  // 矫错级别  
//            //hintMap.put(EncodeHintType.CHARACTER_SET, "utf-8");
//            QRCodeWriter qrCodeWriter = new QRCodeWriter();  
//            //创建比特矩阵(位矩阵)的QR码编码的字符串  
//            BitMatrix byteMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hintMap);  
//            // 使BufferedImage勾画QRCode  (matrixWidth 是行二维码像素点)
//            int matrixWidth = byteMatrix.getWidth();  
//            BufferedImage image = new BufferedImage(matrixWidth-200, matrixWidth-200, BufferedImage.TYPE_INT_RGB);  
//            image.createGraphics();  
//            Graphics2D graphics = (Graphics2D) image.getGraphics();  
//            graphics.setColor(Color.WHITE);  
//            graphics.fillRect(0, 0, matrixWidth, matrixWidth);  
//            // 使用比特矩阵画并保存图像
//            graphics.setColor(Color.BLACK);  
//            for (int i = 0; i < matrixWidth; i++){
//                for (int j = 0; j < matrixWidth; j++){
//                    if (byteMatrix.get(i, j)){
//                        graphics.fillRect(i-100, j-100, 1, 1);  
//                    }
//                }
//            }
//            return ImageIO.write(image, imageFormat, output);  //把二维码写到response输出流
//    }
//    
//    
//    /**
//     * 生成二维码+背景图片
//     * @param content 二维码包含信息
//     * @param qrCodeSize 二维码大小
//     * @param imageFormat 图片类型
//     * @param output 图片输出流
//     * @param bgImgFile 背景图片
//     * @param imagesX 距左边尺寸
//     * @param imagesY 距上边尺寸
//     * @return boolean 
//     * @throws WriterException
//     * @throws IOException
//     */
//    public static boolean createQRCodeWithImg(String content, int qrCodeSize, String imageFormat, OutputStream output, File bgImgFile, int imagesX, int imagesY ) throws WriterException, IOException{  
//        //设置二维码纠错级别ＭＡＰ
//    	Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
//        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);//纠错级别
//        hints.put(EncodeHintType.MARGIN,0);//设置二维码白边的大小   
//        
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();  
//        //创建比特矩阵(位矩阵)的QR码编码的字符串  
//        BitMatrix byteMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hints);  
//        // 使BufferedImage勾画QRCode  (matrixWidth 是行二维码像素点)
//        int matrixWidth = byteMatrix.getWidth();  
//        BufferedImage image = new BufferedImage(matrixWidth-200, matrixWidth-200, BufferedImage.TYPE_INT_RGB);  
//        image.createGraphics();  
//        Graphics2D graphics = (Graphics2D) image.getGraphics();  
//        graphics.setColor(Color.WHITE);  
//        graphics.fillRect(0, 0, matrixWidth, matrixWidth);  
//        // 使用比特矩阵画并保存图像
//        graphics.setColor(Color.BLACK);  
//        for (int i = 0; i < matrixWidth; i++){
//            for (int j = 0; j < matrixWidth; j++){
//                if (byteMatrix.get(i, j)){
//                    graphics.fillRect(i-100, j-100, 1, 1);  
//                }
//            }
//        }
//       
//        //添加背景图片
//        BufferedImage backgroundImage = ImageIO.read(bgImgFile);
//        int bgWidth=backgroundImage.getWidth(); 
//        int qrWidth=image.getWidth();
//        //距离背景图片左边的距离
//        int disx=(bgWidth-qrWidth)-imagesX;
//        //距离下边距离
//        int disy=imagesY;
//        backgroundImage.createGraphics();
//        Graphics2D rng=(Graphics2D)backgroundImage.getGraphics();
//        
//        rng.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP));
//        boolean drawResult = rng.drawImage(image,disx,disy,qrCodeSize,qrCodeSize,null);//将二维码画到背景图片指定位置
//        System.out.println(drawResult);
//        return ImageIO.write(backgroundImage, imageFormat, output);  //把二维码写到response输出流
//}
//    
//
// 
//}
