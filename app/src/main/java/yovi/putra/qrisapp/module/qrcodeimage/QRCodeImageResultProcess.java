package yovi.putra.qrisapp.module.qrcodeimage;

/***
 * author yovi.putra
 */
class QRCodeImageResultProcess {
    Exception error;
    String data;

    QRCodeImageResultProcess(String data, Exception error) {
        this.data = data;
        this.error = error;
    }
}
