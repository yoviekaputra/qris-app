package yovi.putra.qrisapp

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import kotlinx.android.synthetic.main.activity_result.*
import yovi.putra.qrisapp.module.qriscoreparser.QRISCore
import yovi.putra.qrisapp.module.qriscoreparser.QRISCoreLogListener

class ResultActivity : AppCompatActivity(), QRISCoreLogListener {
    private lateinit var qrisCore: QRISCore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        actionBar?.setDisplayHomeAsUpEnabled(true)

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}