package com.example.SimpleFFmpegExample.views

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.DialogFragment
import com.example.SimpleFFmpegExample.BuildConfig
import com.example.SimpleFFmpegExample.R
import java.lang.IllegalStateException

class VideoProgressDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            builder.setView(inflater.inflate(R.layout.dialog_permission, null))


            builder.create()
        } ?: throw IllegalStateException("VideoProgressDialog cannot be null")
    }

}