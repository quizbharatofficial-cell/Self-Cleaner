package com.selfcleaner.app

import android.app.ActivityManager
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var storageText: TextView
    private lateinit var ramText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        storageText = findViewById(R.id.storageText)
        ramText = findViewById(R.id.ramText)

        findViewById<Button>(R.id.scanButton).setOnClickListener {
            updateDeviceInfo()
        }
        updateDeviceInfo()
    }

    private fun updateDeviceInfo() {
        val stat = StatFs(Environment.getDataDirectory().path)
        val total = stat.blockCountLong * stat.blockSizeLong
        val free = stat.availableBlocksLong * stat.blockSizeLong
        val used = total - free
        storageText.text = "Storage: ${gb(used)} GB used / ${gb(total)} GB"

        val am = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val info = ActivityManager.MemoryInfo()
        am.getMemoryInfo(info)
        ramText.text = "RAM: ${gb(info.availMem)} GB available / ${gb(info.totalMem)} GB"
    }

    private fun gb(bytes: Long): String =
        String.format(Locale.US, "%.1f", bytes / 1073741824.0)
}
