package br.com.alissontfb.multifilepicker.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import br.com.alissontfb.multifilepicker.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File


object UtilsFile {

    fun getFolderName(path: String): String {
        val split = path.split("/")
        return split[split.size - 2]
    }

    fun getFileName(path: String): String {
        val split = path.split("/")
        return split[split.size - 1]
    }

    fun getExtension(path: String): String {
        val split = path.split(".")
        return split[split.size - 1]
    }

    fun setImageFromExtension(context: Context, path: String, imageView: ImageView) {
        val ext = getExtension(path)
        var id =
                when (ext) {
                    "ppt", "pptx" -> R.drawable.ic_ppt
                    "doc", "docx" -> R.drawable.ic_word
                    "xls", "xlsx" -> R.drawable.ic_excel
                    "pdf" -> R.drawable.ic_pdf
                    "txt" -> R.drawable.ic_txt
                    "wav", "weba", "aac", "mid", "midi", "mp3" -> R.drawable.ic_audio
                    "rar", "zip" -> R.drawable.ic_zip
                    else -> {
                        0
                    }
                }

        if (id == 0) {
            when (ext) {
                "jpeg", "jpg", "png", "tif", "tiff" -> setImage(context, File(path), imageView)
                "mpeg", "webm", "avi", "mp4" -> {
                    try {
                        val bitmap = FileManageUtils.getThumbnail(context, path)
                        setImage(context, bitmap, imageView)
                    } catch (e: Throwable) {
                        val file = ContextCompat.getDrawable(context, R.drawable.ic_video)
                        setImage(context, file, imageView)
                    }
                }
                else -> {
                    val file = ContextCompat.getDrawable(context, R.drawable.ic_file)
                    setImage(context, file, imageView)
                }
            }
        } else {
            val file = ContextCompat.getDrawable(context, id)
            setImage(context, file, imageView)
        }
    }

    fun setImage(context: Context, drawable: Drawable?, imageView: ImageView) {
        Glide
                .with(context)
                .load(drawable)
                .apply(RequestOptions().placeholder(R.drawable.ic_file)
                        .error(R.drawable.ic_file))
                .into(imageView)
    }

    fun setImage(context: Context, file: File, imageView: ImageView) {
        (imageView.parent as View).setBackgroundColor(Color.TRANSPARENT)
        Glide
                .with(context)
                .load(file)
                .apply(RequestOptions().placeholder(R.drawable.ic_file)
                        .error(R.drawable.ic_file))
                .into(imageView)
                .view.scaleType = ImageView.ScaleType.CENTER_CROP
    }

    fun setImage(context: Context, file: Bitmap?, imageView: ImageView) {
        (imageView.parent as View).setBackgroundColor(Color.TRANSPARENT)
        Glide
                .with(context)
                .load(file)
                .apply(RequestOptions().placeholder(R.drawable.ic_file)
                        .error(R.drawable.ic_file))
                .into(imageView)
                .view.scaleType = ImageView.ScaleType.CENTER_CROP
    }
}
