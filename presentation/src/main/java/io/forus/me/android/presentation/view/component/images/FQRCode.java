package io.forus.me.android.presentation.view.component.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

public class FQRCode {
    public static float logoPercentage = 0.23f;


    public static Bitmap generateFromVectorImgResId(Context context, String content, int logoResId, int qrSize) {

        Bitmap bitmap = getBitmapFromVectorDrawable(context, logoResId);
        return generate(content, bitmap, qrSize);
    }

    public static Bitmap generateFromPixelImgResId(Context context, String content, int logoResId, int qrSize) {

        Bitmap bitmap = getBitmapFromResource(context, logoResId);
        return generate(content, bitmap, qrSize);
    }

    public static Bitmap generate(String content, Bitmap iconBitmap, int qrSize) {

        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = null;

        try {

            bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, qrSize, qrSize, hints);


            int height = bitMatrix.getHeight();
            int width = bitMatrix.getWidth();
            Bitmap qrImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    qrImage.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }


            if (logoPercentage > 0.5 || logoPercentage <= 0) logoPercentage = 0.17f;

            float logoSize = qrImage.getWidth() * logoPercentage;

            Bitmap combined = Bitmap.createBitmap(qrImage.getWidth(),
                    qrImage.getHeight(), qrImage.getConfig());
            Canvas canvas = new Canvas(combined);

            canvas.drawBitmap(qrImage, 0, 0, null);

            if (iconBitmap != null) {

                Bitmap overly = getResizedBitmap(iconBitmap, (int) logoSize, (int) logoSize);

                canvas.drawBitmap(overly, (int) ((width - logoSize) / 2), (int) ((height - logoSize) / 2),
                        null);

            }
            return combined;

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Bitmap getBitmapFromResource(Context context, int logoResId) {
        return BitmapFactory.decodeResource(context.getResources(),
                logoResId);
    }

    private static boolean isVectorDrawable(@NonNull Drawable d) {
        return d instanceof VectorDrawableCompat;
    }


    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


    private static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }


}
