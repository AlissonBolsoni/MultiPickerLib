package br.com.alissontfb.multifilepicker.loader

import android.content.Context
import br.com.alissontfb.multifilepicker.model.ObFileView
import br.com.alissontfb.multifilepicker.utils.FilesCache
import java.io.File

class FileViewLoader(context: Context, private val root: File) :
        BaseViewLoader(context) {

    override fun loadInBackground(): ObFileView {
        obFileView = FilesCache.getImages(root)
        obFileView = FilesCache.getVideos(root)
        obFileView = FilesCache.getAudios(root)

        return obFileView!!
    }

}