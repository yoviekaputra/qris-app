package yovi.putra.qrisapp.module.qrcodeimage;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

/***
 * author yovi.putra
 */
class QRCodeImageProcessor extends AsyncTask<Bitmap, String, QRCodeImageResultProcess> {

    private QRCodeImageListener listener;

    QRCodeImageProcessor(QRCodeImageListener listener) {
        this.listener = listener;
    }

    @Override
    protected QRCodeImageResultProcess doInBackground(Bitmap... bitmaps) {
        Bitmap bitmap = bitmaps[0];
        int[] intArray = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(intArray, 0, bitmap.getWidth(),
                0, 0,
                bitmap.getWidth(), bitmap.getHeight());

        try {
            Reader reader = new MultiFormatReader();
            LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), intArray);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = reader.decode(binaryBitmap);
            return new QRCodeImageResultProcess(result.getText(), null);
        } catch (NotFoundException | ChecksumException | FormatException e) {
            return new QRCodeImageResultProcess(null, e);
        }
    }

    @Override
    protected void onPostExecute(QRCodeImageResultProcess s) {
        super.onPostExecute(s);
        if (s.error == null && s.data != null) {
            listener.onResult(s.data);
        } else {
            listener.onFailed(s.error);
        }
    }
}
