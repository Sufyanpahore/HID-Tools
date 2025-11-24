package com.sufyan.hidtools.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val context: Context
        get() = getApplication()

    fun clearScripts() {
        viewModelScope.launch(Dispatchers.IO) {
            val scriptsDir = File(context.filesDir, "scripts")
            if (scriptsDir.exists()) {
                val deleted = scriptsDir.deleteRecursively()
                Log.d("SettingsViewModel", "Scripts directory deleted: $deleted")
            }
        }
    }

    fun exportScripts() {
        viewModelScope.launch(Dispatchers.IO) {
            val scriptsDir = File(context.filesDir, "scripts")
            if (!scriptsDir.exists() || scriptsDir.listFiles()?.isEmpty() == true) {
                // Don't export if there are no scripts
                return@launch
            }

            val zipFile = File(context.cacheDir, "exported_scripts.zip")
            try {
                ZipOutputStream(FileOutputStream(zipFile)).use { zipOut ->
                    scriptsDir.listFiles()?.forEach { file ->
                        zipOut.putNextEntry(ZipEntry(file.name))
                        file.inputStream().use { it.copyTo(zipOut) }
                        zipOut.closeEntry()
                    }
                }

                val authority = "${context.packageName}.provider"
                val uri = FileProvider.getUriForFile(context, authority, zipFile)

                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, uri)
                    type = "application/zip"
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                val chooser = Intent.createChooser(shareIntent, "Export Scripts")
                chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(chooser)

            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Failed to export scripts", e)
            }
        }
    }
}