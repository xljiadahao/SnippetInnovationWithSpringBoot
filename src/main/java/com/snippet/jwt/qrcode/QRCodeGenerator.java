package com.snippet.jwt.qrcode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.snippet.utils.ByteUtil;

public class QRCodeGenerator {

    private static final String QRCODE_DIR = "qrcode" + File.separator;
    private static final String lOGO_DIR = "qrcode" + File.separator + "paypal_logo.png";
    
    private static final int QRCOLOR = 0xFF000000;
    private static final int BGWHITE = 0xFFFFFFFF;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    
    private static Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
    
    static {
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 0);
    }

    public static String generateQRCode(File logoFile, String note, String qrCodeContent) 
            throws WriterException, IOException {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bm = multiFormatWriter.encode(qrCodeContent, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                image.setRGB(x, y, bm.get(x, y) ? QRCOLOR : BGWHITE);
            }
        }
        int width = image.getWidth();
        int height = image.getHeight();
        if (logoFile == null || !logoFile.exists()) {
            logoFile = new File(lOGO_DIR);
        }
        Graphics2D g = image.createGraphics();
        System.out.println("debug logo file full path: " + logoFile.getAbsolutePath());
        BufferedImage logo = ImageIO.read(logoFile);
        g.drawImage(logo, width * 2 / 5, height * 2 / 5, width * 2 / 10, height * 2 / 10, null);
        g.dispose();
        logo.flush();
        if (StringUtils.isNotEmpty(note)) {
            BufferedImage outImage = new BufferedImage(400, 445, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D outg = outImage.createGraphics();
            outg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
            outg.setColor(Color.BLACK);
            outg.setFont(new Font("ITALIC", Font.BOLD, 30));
            int strWidth = outg.getFontMetrics().stringWidth(note);
            if (strWidth < 400) {
                outg.drawString(note, 200 - strWidth / 2, height + (outImage.getHeight() - height) / 2 + 12);
            }
            outg.dispose();
            outImage.flush();
            image = outImage;
        }
        image.flush();
        File qrCodeFile = null;
        StringBuilder qrCodeFilePath = new StringBuilder(QRCODE_DIR);
        if (StringUtils.isNotBlank(note)) {
            qrCodeFilePath.append(note);
        }
        qrCodeFilePath.append(new Date().getTime()).append(".png");
        qrCodeFile = new File(qrCodeFilePath.toString());
        ImageIO.write(image, "png", qrCodeFile);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        byte[] qrCodeRawBytes = out.toByteArray();
        return ByteUtil.parseByte2HexStr(qrCodeRawBytes);
    }

}
