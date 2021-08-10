package com.example.SimpleFFmpegExample.views

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.example.SimpleFFmpegExample.R
import com.example.SimpleFFmpegExample.utils.FontsConstants
import com.example.SimpleFFmpegExample.utils.FontsHelper
import kotlinx.android.synthetic.main.dialog_progress.*

class VideoProgressDialog(private var ctx: Context, private var message: String) : Dialog(ctx) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        setCanceledOnTouchOutside(false)

        messageLabel.text = message
        messageLabel.typeface = FontsHelper[ctx, FontsConstants.SEMI_BOLD]
    }

    fun setProgress(progress: Float) {
        pieProgress.setProgress(progress)
    }
}