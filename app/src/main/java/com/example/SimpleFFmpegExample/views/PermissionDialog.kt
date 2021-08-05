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

class PermissionDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.dialog_permission, null))
                // Add action buttons
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()!!.cancel()
                    })
                .setPositiveButton("권한설정 이동",
                    DialogInterface.OnClickListener { dialog, id ->
                        // sign in the user ...
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + BuildConfig.APPLICATION_ID))
                        this.startActivity(intent)
                        dismiss()
                    })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


}