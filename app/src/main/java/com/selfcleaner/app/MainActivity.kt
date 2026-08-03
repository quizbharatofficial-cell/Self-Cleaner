package com.selfcleaner.app

import android.app.ActivityManager
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var storageText: TextView
    private lateinit var ramText: TextView
    private lateinit var statusText: TextView
    private lateinit var storageProgress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        storageText = findViewById(R.id.storageText)
        ramText = findViewById(R.id.ramText)
        statusText = findViewById(R.id.statusText)
        storageProgress = findViewById(R.id.storageProgress)

        findViewById<Button>(R.id.scanButton).setOnClickListener {
            statusText.text = "Scanning device..."
            updateDeviceInfo()
            statusText.text = "Scan completed"
            Toast.makeText(this, "Device scan completed", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.junkButton).setOnClickListener {
            featureMessage("Junk Cleaner")
        }

        findViewById<Button>(R.id.largeButton).setOnClickListener {
            featureMessage("Large Files")
        }

        findViewById<Button>(R.id.downloadButton).setOnClickListener {
            featureMessage("Downloads Cleaner")
        }

        findViewById<Button>(R.id.duplicateButton).setOnClickListener {
            featureMessage("Duplicate Finder")
        }

        findViewById<Button>(R.id.ramButton).setOnClickListener {
            updateDeviceInfo()
            Toast.makeText(this, "RAM information refreshed", Toast.LENGTH_SHORT).show()
        }

        updateDeviceInfo()
    }

    private fun updateDeviceInfo() {

        val stat = StatFs(Environment.getDataDirectory().path)

        val totalStorage =
            stat.blockCountLong * stat.blockSizeLong

        val freeStorage =
            stat.availableBlocksLong * stat.blockSizeLong

        val usedStorage =
            totalStorage - freeStorage

        val storagePercent =
            if (totalStorage > 0)
                ((usedStorage * 100) / totalStorage).toInt()
            else 0

        storageText.text =
            "Storage: ${gb(usedStorage)} GB used / ${gb(totalStorage)} GB  ($storagePercent%)"

        storageProgress.progress = storagePercent

        val activityManager =
            getSystemService(ACTIVITY_SERVICE) as ActivityManager

        val memoryInfo =
            ActivityManager.MemoryInfo()

        activityManager.getMemoryInfo(memoryInfo)

        val usedRam =
            memoryInfo.totalMem - memoryInfo.availMem

        val ramPercent =
            if (memoryInfo.totalMem > 0)
                ((usedRam * 100) / memoryInfo.totalMem).toInt()
            else 0

        ramText.text =
            "RAM: ${gb(memoryInfo.availMem)} GB available / ${gb(memoryInfo.totalMem)} GB  • $ramPercent% used"
    }

    private fun featureMessage(feature: String) {
        statusText.text = "$feature selected"
        Toast.makeText(
            this,
            "$feature module will use Android-safe file access.",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun gb(bytes: Long): String {
        return String.format(
            Locale.US,
            "%.1f",
            bytes / 1073741824.0
        )
    }
}
