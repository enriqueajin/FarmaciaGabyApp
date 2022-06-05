package com.farmaciagaby.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.farmaciagaby.R


class LoadingDialog : DialogFragment() {

    companion object {
        const val TAG = "LoadingDialog"
    }

    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.loading_dialog, container, false)
        imageView = view.findViewById<ImageView>(R.id.iv_loading_icon)
        return view
    }

    override fun onStart() {
        super.onStart()
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val d = dialog
        if (d != null) {
            Glide
                .with(this)
                .asGif()
                .load(R.drawable.loader)
                .into(imageView)
        }
    }
}