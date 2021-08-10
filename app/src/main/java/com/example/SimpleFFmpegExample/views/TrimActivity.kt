package com.example.SimpleFFmpegExample.views


import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.media.*
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.SimpleFFmpegExample.R
import com.example.SimpleFFmpegExample.utils.Constants.INTENT_VIDEO_PATH
import com.example.SimpleFFmpegExample.utils.Constants.TAG
import com.example.SimpleFFmpegExample.utils.FontsConstants
import com.example.SimpleFFmpegExample.utils.FontsHelper
import com.example.SimpleFFmpegExample.utils.RunOnUiThread
import com.video.trimmer.interfaces.OnTrimVideoListener
import com.video.trimmer.interfaces.OnVideoListener

import kotlinx.android.synthetic.main.activity_trim.*
import java.io.File

class TrimActivity : AppCompatActivity(), OnTrimVideoListener, OnVideoListener {
    private val progressDialog: VideoProgressDialog by lazy { VideoProgressDialog(this, "Cropping video. Please wait...") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trim)
        Log.d(TAG, "TrimActivity - onCreate() called")

        setupPermissions {
            val extraIntent = intent
            var path = ""
            if (extraIntent != null) path = extraIntent.getStringExtra(INTENT_VIDEO_PATH)
            videoTrimmer.setTextTimeSelectionTypeface(FontsHelper[this, FontsConstants.SEMI_BOLD])
                    .setOnTrimVideoListener(this)
                    .setOnVideoListener(this)
                    .setVideoURI(Uri.parse(path))
                    .setVideoInformationVisibility(true)
                    .setMaxDuration(10)
                    .setMinDuration(2)
                    .setDestinationPath(Environment.getExternalStorageDirectory().toString() + File.separator + "test" + File.separator + "Videos" + File.separator)
        }
    }

    override fun onTrimStarted() {
        Log.d(TAG, "TrimActivity - onTrimStarted() called") 
        RunOnUiThread(this).safely {
            Toast.makeText(this, "Started Trimming", Toast.LENGTH_SHORT).show()
            progressDialog.show()
        }
    }
    
    override fun getResult(uri: Uri) {
        Log.d(TAG, "TrimActivity - getResult() called")
        RunOnUiThread(this).safely {
            Toast.makeText(this, "Video saved at ${uri.path}", Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
            val mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(this, uri)
            val duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()
            val width = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH).toLong()
            val height = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT).toLong()
            val values = ContentValues()
            values.put(MediaStore.Video.Media.DATA, uri.path)
            values.put(MediaStore.Video.VideoColumns.DURATION, duration)
            values.put(MediaStore.Video.VideoColumns.WIDTH, width)
            values.put(MediaStore.Video.VideoColumns.HEIGHT, height)
            val id = ContentUris.parseId(contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values))
            Log.e("VIDEO ID", id.toString())
        }
    }

    override fun onVideoPrepared() {
        Log.d(TAG, "TrimActivity - onVideoPrepared() called") 
        RunOnUiThread(this).safely {
            Toast.makeText(this, "onVideoPrepared", Toast.LENGTH_SHORT).show()
        }
    }

    override fun cancelAction() {
        Log.d(TAG, "TrimActivity - cancelAction() called") 
        RunOnUiThread(this).safely {
            videoTrimmer.destroy()
            finish()
        }
    }

    override fun onError(message: String) {
        Log.e(TAG, "Trim onError {$message}")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            R.id.item_save -> {
                videoTrimmer.onSaveClicked()
                return true
            }

            R.id.item_back -> {
                videoTrimmer.onCancelClicked()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "TrimActivity - onStart() called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "TrimActivity - onRestart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "TrimActivity - onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "TrimActivity - onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "TrimActivity - onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "TrimActivity - onDestroy() called")
    }

    lateinit var doThis: () -> Unit
    private fun setupPermissions(doSomething: () -> Unit) {
        val writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        doThis = doSomething
        if (writePermission != PackageManager.PERMISSION_GRANTED && readPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 101)
        } else doThis()
    }

}