package br.com.alissontfb.multifilepicker.config

import android.app.Activity
import android.content.Intent
import br.com.alissontfb.multifilepicker.R
import br.com.alissontfb.multifilepicker.model.FilePickerParams
import br.com.alissontfb.multifilepicker.preferences.Preferences
import br.com.alissontfb.multifilepicker.ui.activity.FilePickerTabActivity
import br.com.alissontfb.multifilepicker.utils.INTENT_TO_ACTIVITY_PARAM
import br.com.alissontfb.multifilepicker.utils.REQUEST_CODE_TO_RESULT

class FilePicker private constructor(
        private val builder: FilePicker.Builder
) {

    init {
        //TODO - fazer a criação do FilePicker

        builder.params.tabList = builder.tabList

        val intent = Intent(builder.context, FilePickerTabActivity::class.java)
        intent.putExtra(INTENT_TO_ACTIVITY_PARAM, builder.params)
        builder.context.startActivityForResult(intent, REQUEST_CODE_TO_RESULT)
    }


    open class Builder(internal val context: Activity) {

        internal val params = FilePickerParams()
        internal val tabList: ArrayList<String> = ArrayList()

        fun showFiles(bool: Boolean): Builder {
            if (bool)
                tabList.add(context.getString(R.string.files_tab))

            return this
        }

        fun showAudios(bool: Boolean): Builder {
            if (bool)
                tabList.add(context.getString(R.string.audios_tab))

            return this
        }

        fun showImages(bool: Boolean): Builder {
            if (bool)
                tabList.add(context.getString(R.string.images_tab))

            return this
        }

        fun showVideos(bool: Boolean): Builder {
            if (bool)
                tabList.add(context.getString(R.string.videos_tab))

            return this
        }

        fun saveAudios(bool: Boolean): Builder {
            params.saveAudio = bool

            return this
        }

        fun saveImages(bool: Boolean): Builder {
            params.saveImages = bool

            return this
        }

        fun saveVideos(bool: Boolean): Builder {
            params.saveVideo = bool

            return this
        }

        fun maxFiles(max: Int): Builder {
            params.max = max
            return this
        }

        fun build(): FilePicker{

            if(tabList.isEmpty())
                tabList.add(context.getString(R.string.files_tab))

            return FilePicker(this)
        }

        fun setProvider(provider: String = ""): Builder {
            Preferences(context).setProvider(provider)
            return this
        }
    }
}