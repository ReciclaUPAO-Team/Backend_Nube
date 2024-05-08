package com.upao.recicla.infra.email;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class QrCodeGenerator {
    public static byte[] generateQRCodeImage(String text, int width, int height) throws Exception {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 1);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
        return baos.toByteArray();
    }
}
