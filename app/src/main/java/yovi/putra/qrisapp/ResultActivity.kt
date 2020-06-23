package yovi.putra.qrisapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_result.*
import yovi.putra.qrisapp.module.qriscoreparser.QRISCore
import yovi.putra.qrisapp.module.qriscoreparser.QRISCoreLogListener

class ResultActivity : AppCompatActivity(), QRISCoreLogListener {
    private lateinit var qrisCore: QRISCore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val data = intent.getStringExtra("data")
        qrisCore = QRISCore(this, this)
        val fields = qrisCore.parsing(data)
        qrisCore.print(fields)
    }

    override fun logging(log: String?) {
        var temp = tv_result.text
        temp = "$temp$log\n"
        tv_result.text = temp
    }
}