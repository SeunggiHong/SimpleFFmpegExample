package com.example.SimpleFFmpegExample.views

import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.SimpleFFmpegExample.R
import com.example.SimpleFFmpegExample.utils.Constants.TAG
import com.example.SimpleFFmpegExample.utils.RunOnUiThread
import com.video.trimmer.interfaces.OnTrimVideoListener
import com.video.trimmer.interfaces.OnVideoListener
import kotlinx.android.synthetic.main.activity_trim.*

class TrimActivity : AppCompatActivity(), OnTrimVideoListener, OnVideoListener {

    private val progressDialog: VideoProgressDialog by lazy {
        VideoProgressDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trim)
    }

    override fun getResult(uri: Uri) {
        RunOnUiThread(this).safely {
            Toast.makeText(this, "Video saved at ${uri.path}", Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
            val mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(this, uri)
            val duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
            val width = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toLong()
            val height = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toLong()
            val values = ContentValues()
            values.put(MediaStore.Video.Media.DATA, uri.path)
            values.put(MediaStore.Video.VideoColumns.DURATION, duration)
            values.put(MediaStore.Video.VideoColumns.WIDTH, width)
            values.put(MediaStore.Video.VideoColumns.HEIGHT, height)
            val id = ContentUris.parseId(contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)!!)
            Log.e("VIDEO ID", id.toString())
        }
        
    }

    override fun onTrimStarted() {
        Log.d(TAG, "TrimActivity - onTrimStarted() called")

        RunOnUiThread(this).safely {
            Toast.makeText(this, "Started Trimming", Toast.LENGTH_SHORT).show()
            showProgressDialog()
        }
    }

    override fun cancelAction() {
        Log.d(TAG, "TrimActivity - cancelAction() called")

        RunOnUiThread(this).safely {
            videoTrimmer.destroy()
            finish()
        }
    }

    override fun onVideoPrepared() {
        Log.d(TAG, "TrimActivity - onVideoPrepared() called")

        RunOnUiThread(this).safely {
            Toast.makeText(this, "onVideoPrepared", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onError(message: String) {
        Log.e(TAG, "TrimActivity onError: ${message}", )
    }

    private fun showProgressDialog() {
        val dlgPermission = VideoProgressDialog()
        dlgPermission.show(supportFragmentManager, "progress")
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

}