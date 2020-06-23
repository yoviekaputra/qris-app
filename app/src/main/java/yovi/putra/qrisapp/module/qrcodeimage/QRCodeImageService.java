package yovi.putra.qrisapp.module.qrcodeimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.File;

/***
 * author yovi.putra
 */
public class QRCodeImageService {
    private QRCodeImageListener listener;

    public QRCodeImageService(QRCodeImageListener listener) {
        this.listener = listener;
    }

    public void read(File file) {
        new DecodeFile(listener).execute(file);
    }

    public void read(Bitmap bitmap) {
        new QRCodeImageProcessor(listener).execute(bitmap);
    }

    static class DecodeFile extends AsyncTask<File, Void, Bitmap> {
        private QRCodeImageListener listener;

        DecodeFile(QRCodeImageListener listener) {
            this.listener = listener;
        }

        @Override
        protected Bitmap doInBackground(File... files) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            return BitmapFactory.decodeFile(files[0].getAbsolutePath(), options);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            new QRCodeImageProcessor(listener).execute(bitmap);
        }
    }
}
