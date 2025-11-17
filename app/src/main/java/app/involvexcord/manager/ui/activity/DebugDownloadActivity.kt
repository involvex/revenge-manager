package app.involvexcord.manager.ui.activity

import androidx.activity.ComponentActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import app.involvexcord.manager.domain.manager.DownloadManager
import app.involvexcord.manager.domain.manager.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class DebugDownloadActivity : ComponentActivity() {

    private val downloadManager: DownloadManager by inject()
    private val prefs: PreferenceManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Start download in background
        lifecycleScope.launch {
            try {
                val outFile = filesDir.resolve("debug_mod.apk")
                val result = withContext(Dispatchers.IO) {
                    downloadManager.downloadMod(outFile) { progress ->
                        // Could update a notification or UI; we just log
                        Log.d("DebugDownload", "progress=$progress")
                    }
                }

                when (result) {
                    is app.involvexcord.manager.domain.manager.DownloadResult.Success -> {
                        Toast.makeText(this@DebugDownloadActivity, "Download succeeded: ${outFile.absolutePath}", Toast.LENGTH_LONG).show()
                        Log.i("DebugDownload", "Download succeeded: ${outFile.absolutePath}")
                    }

                    is app.involvexcord.manager.domain.manager.DownloadResult.Error -> {
                        Toast.makeText(this@DebugDownloadActivity, "Download failed: ${result.debugReason}", Toast.LENGTH_LONG).show()
                        Log.e("DebugDownload", "Download failed: ${result.debugReason}")
                    }

                    is app.involvexcord.manager.domain.manager.DownloadResult.Cancelled -> {
                        Toast.makeText(this@DebugDownloadActivity, "Download cancelled", Toast.LENGTH_LONG).show()
                        Log.w("DebugDownload", "Download cancelled")
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@DebugDownloadActivity, "Download exception: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("DebugDownload", "Exception", e)
            } finally {
                finish()
            }
        }
    }
}
