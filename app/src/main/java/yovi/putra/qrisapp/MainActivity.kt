package yovi.putra.qrisapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.PointF
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import kotlinx.android.synthetic.main.frame_qrcode.*
import yovi.putra.qrisapp.module.ImageUtils
import yovi.putra.qrisapp.module.qrcodeimage.QRCodeImageListener
import yovi.putra.qrisapp.module.qrcodeimage.QRCodeImageService
import java.io.File

class MainActivity : AppCompatActivity(),
    QRCodeReaderView.OnQRCodeReadListener,
    ImageUtils.ImageAttachmentListener,
    QRCodeImageListener {
    private lateinit var imageUtils: ImageUtils
    private lateinit var qrCodeImageService: QRCodeImageService

    private var PERMISSIONS = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupData()
        setupUI()
    }

    override fun onStart() {
        super.onStart()
        qrcode_reader_view.isEnabled = true
        qrcode_reader_view.startCamera()
        points_overlay_view.setPoints(null)
    }

    override fun onPause() {
        qrcode_reader_view?.stopCamera()
        super.onPause()
    }

    private fun setupData() {
        imageUtils = ImageUtils(this)
        qrCodeImageService = QRCodeImageService(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1)
        }
    }

    private fun setupUI() {
        setupScannerView()
        tv_import.setOnClickListener { imageUtils.launchGallery(1) }
    }

    private fun setupScannerView() {
        qrcode_reader_view?.apply {
            this.setAutofocusInterval(2000L)
            this.setOnQRCodeReadListener(this@MainActivity)
            this.setBackCamera()
            this.isEnabled = true
            this.startCamera()
        }
        flashlight_checkbox.setOnCheckedChangeListener { _, isChecked ->
            qrcode_reader_view.setTorchEnabled(isChecked)
        }
    }

    /* Scan QRCode handling */
    override fun onQRCodeRead(text: String?, points: Array<out PointF>?) {
        qrcode_reader_view?.let {
            if (it.isEnabled) {
                it.isEnabled = false
                it.stopCamera()
                points_overlay_view.setPoints(points)
                onResult(text)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.let {
            imageUtils.onActivityResult(requestCode, resultCode, data)
        } ?: run {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun image_attachment(from: Int, filename: String?, file: Bitmap?, uri: Uri?) {
        qrCodeImageService.read(File(imageUtils.getPath(uri)))
    }

    override fun onResult(data: String?) {
        startActivity(Intent(this, ResultActivity::class.java).apply {
            putExtra("data", data)
        })
    }

    override fun onFailed(e: Exception?) {
        Toast.makeText(this, "QRCode format invalid", Toast.LENGTH_SHORT).show()
    }

    /* PERMISSION */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty()) {
                if (!allPermissionGranted()) {
                    if (isNeverAskChecked) {
                        ActivityCompat.requestPermissions(this, PERMISSIONS, 1)
                    } else {
                        Toast.makeText(
                            this,
                            "Silakan aktifkan perizinan aplikasi ini melalui manajer aplikasi di menu pengaturan",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun allPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (PERMISSION in PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(this, PERMISSION) == PackageManager.PERMISSION_DENIED) {
                    return false
                }
            }
        }
        return true
    }

    private val isNeverAskChecked: Boolean
        get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (PERMISSION in PERMISSIONS) {
                    if (shouldShowRequestPermissionRationale(PERMISSION)) {
                        return true
                    }
                }
            }
            return false
        }
}