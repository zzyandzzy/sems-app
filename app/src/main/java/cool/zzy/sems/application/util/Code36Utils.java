package cool.zzy.sems.application.util;

import android.graphics.*;

import java.util.Random;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/2/6 11:16
 * @since 1.0
 */
public class Code36Utils {
    private static final String TAG = Code36Utils.class.getSimpleName();
    // 图像宽度
    private static final int DEFAULT_IMAGE_WIDTH = 207;
    // 图像高度
    private static final int DEFAULT_IMAGE_HEIGHT = 98;
    // CODE36每个字符9位
    private static final int CODE_36_CHAR_BIT_LEN = 9;
    // CODE36开始字符
    private static final byte[] CODE_36_CODE_START_CHAR = {1, 1, 1, 0, 0, 0, 1, 1, 1};
    // CODE36结束字符
    private static final byte[] CODE_36_CODE_STOP_CHAR = {0, 0, 0, 1, 1, 1, 1, 1, 1};
    private static final byte[][] CODE_36_CHAR_BIT_PATTERN = {
            // 0 - 9
            {0, 0, 0, 1, 0, 0, 1, 1, 1},
            {0, 0, 0, 1, 1, 1, 0, 0, 1},
            {0, 0, 0, 1, 0, 0, 0, 0, 1},
            {0, 0, 0, 1, 1, 1, 0, 1, 1},
            {0, 0, 1, 0, 0, 0, 0, 1, 1},
            {0, 1, 1, 1, 0, 0, 0, 1, 1},
            {0, 0, 1, 0, 0, 1, 1, 1, 1},
            {0, 1, 1, 1, 0, 1, 1, 1, 1},
            {0, 0, 0, 1, 0, 0, 0, 1, 1},
            {0, 0, 1, 0, 0, 0, 1, 1, 1},
            // A - Z
            {0, 0, 0, 1, 1, 0, 1, 1, 1},
            {0, 0, 0, 1, 1, 0, 0, 0, 1},
            {0, 0, 0, 1, 1, 0, 0, 1, 1},
            {0, 0, 0, 1, 1, 1, 1, 0, 1},
            {0, 1, 1, 1, 1, 0, 0, 0, 1},
            {0, 0, 1, 1, 0, 1, 1, 1, 1},
            {0, 1, 1, 1, 1, 0, 1, 1, 1},
            {0, 0, 0, 0, 1, 0, 0, 1, 1},
            {0, 0, 0, 0, 1, 1, 1, 0, 1},
            {0, 0, 1, 1, 0, 0, 0, 1, 1},
            {0, 0, 0, 0, 1, 0, 0, 0, 1},
            {0, 0, 1, 1, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 1, 0, 1, 1, 1},
            {0, 1, 1, 1, 0, 0, 1, 1, 1},
            {0, 0, 1, 1, 1, 1, 0, 1, 1},
            {0, 0, 0, 1, 0, 1, 1, 1, 1},
            {0, 1, 1, 1, 0, 0, 0, 0, 1},
            {0, 1, 1, 1, 1, 0, 0, 1, 1},
            {0, 0, 1, 1, 1, 0, 0, 0, 1},
            {0, 0, 1, 1, 1, 0, 0, 1, 1},
            {0, 1, 0, 0, 0, 0, 1, 1, 1},
            {0, 0, 1, 1, 1, 1, 0, 0, 1},
            {0, 0, 1, 1, 0, 0, 1, 1, 1},
            {0, 0, 0, 0, 1, 1, 0, 1, 1},
            {0, 0, 1, 1, 1, 0, 1, 1, 1},
            {0, 0, 0, 0, 1, 1, 0, 0, 1},
            // OTHER
            {0, 1, 1, 0, 0, 1, 1, 1, 1},
            {0, 1, 1, 0, 0, 0, 1, 1, 1},
            {0, 1, 1, 0, 0, 0, 0, 1, 1},
            {0, 1, 0, 0, 0, 1, 1, 1, 1}
    };

    public static String randomCode() {
        String code = getRandom(16);
        return code;
    }

    public static String getRandom(int len) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (int i = 0; i < len; i++) {
            if (isFirst) {
                sb.append(random.nextInt(8) + 1);
                isFirst = false;
            } else {
                sb.append(random.nextInt(9));
            }
        }
        return sb.toString();
    }

    public static Bitmap drawCode36Code(String code) {
        return drawCode36Code(code, DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
    }

    public static Bitmap drawCode36Code(String code, int width, int height) {
        if (code == null) {
            throw new IllegalArgumentException("EAN 13 code format error.");
        }
        return createBitmap(code, width, height);
    }

    private static Bitmap createBitmap(String code, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        // 画布颜色：白色
        canvas.drawColor(Color.WHITE);
        // 计算校验位
//        code = generateCheckCode(code);
        // 生成二进制条形码
        byte[] barcodeCharArray = generateBarcode(code);
        // 画条形码
        drawCode(canvas, code, barcodeCharArray, width, height);
        return bitmap;
    }

//    private static String generateCheckCode(String code) {
//        int codeChar;
//        int checkSum = 0;
//        // 先把校验位算出来
//        for (int i = 1; i < code.length(); i++) {
//            codeChar = code.charAt(i) - '0';
//            checkSum += codeChar * EAN_13_CHECK_DIGIT[i - 1];
//        }
//        checkSum += code.charAt(0) - '0';
//        int currentCheckDigit = 10 - (checkSum % 10);
//        if (currentCheckDigit == 10) {
//            currentCheckDigit = 0;
//        }
//        return code + currentCheckDigit;
//    }

    private static byte[] generateBarcode(String code) {
        int currentLength = 0;
        byte[] barcodeCharArray = new byte[code.length() * CODE_36_CHAR_BIT_LEN + 2 * CODE_36_CHAR_BIT_LEN];
        // 开始字符
        System.arraycopy(CODE_36_CODE_START_CHAR, 0, barcodeCharArray, currentLength, CODE_36_CODE_START_CHAR.length);
        currentLength += CODE_36_CODE_START_CHAR.length;
        int codeChar;
        for (int i = 0; i < code.length(); i++) {
            codeChar = code.charAt(i) - '0';
            System.arraycopy(CODE_36_CHAR_BIT_PATTERN[codeChar], 0,
                    barcodeCharArray, currentLength, CODE_36_CHAR_BIT_LEN);
            currentLength += CODE_36_CHAR_BIT_LEN;
        }
        // 结束字符和开始字符一样
        System.arraycopy(CODE_36_CODE_STOP_CHAR, 0,
                barcodeCharArray, currentLength, CODE_36_CODE_STOP_CHAR.length);
        return barcodeCharArray;
    }

    /**
     * @param canvas           画布
     * @param code             条形码
     * @param barcodeCharArray 条形码二进制数据
     */
    private static void drawCode(Canvas canvas, String code, byte[] barcodeCharArray, int width, int height) {
        Paint paint = new Paint();
        // 设置颜色：黑色
        paint.setColor(Color.BLACK);
        Paint textPaint = new Paint();
        // 设置颜色：黑色
        textPaint.setColor(Color.BLACK);
        float textSize = height * 0.2F - (height * 0.015F);
        // 设置字体大小
        textPaint.setTextSize(textSize);
        // 设置字体风格：粗体
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        // 设置字体抗锯齿
        textPaint.setAntiAlias(true);
        float tempWidth = width * 0.92F;
        float prefixWidth = width - width * 0.92F;
        // 每个块的宽度
        float rectWidth = tempWidth / barcodeCharArray.length;
        // 每个块的高度
        float rectHeight = height - (height * 0.2F);
        // 画块
        for (int i = 0; i < barcodeCharArray.length; i++) {
            if (barcodeCharArray[i] == 1) {
                float left = prefixWidth + i * rectWidth;
                float right = left + rectWidth;
                float top = 0F;
                float bottom = rectHeight;
                RectF rect = new RectF(left, top, right, bottom);
                canvas.drawRect(rect, paint);
            }
        }
        // 画字体
        for (int i = 0; i < code.length(); i++) {
            int x = 0;
            x = (int) (prefixWidth + CODE_36_CHAR_BIT_LEN * rectWidth + (i - 1) * rectWidth * 7);
            canvas.drawText(String.valueOf(code.charAt(i)), x, rectHeight + textSize, textPaint);
        }
    }
}
