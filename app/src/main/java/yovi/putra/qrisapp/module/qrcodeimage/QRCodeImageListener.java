package yovi.putra.qrisapp.module.qrcodeimage;

/***
 * author yovi.putra
 */
public interface QRCodeImageListener {
    void onResult(String data);

    void onFailed(Exception e);
}
