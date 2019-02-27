package br.com.alissontfb.multifilepicker.utils

import br.com.alissontfb.multifilepicker.model.ObFileItemView
import br.com.alissontfb.multifilepicker.model.ObFileView
import java.io.File

object FilesCache {

    private var obFileViewImages: ObFileView? = null
    private var obFileViewVideos: ObFileView? = null
    private var obFileViewAudios: ObFileView? = null

    private var reloadImage = true
    private var reloadAudio = true
    private var reloadVideo = true

    fun getImages(root: File): ObFileView {
        if (obFileViewImages != null && !reloadImage) return obFileViewImages!!

        val findFiles = FindFiles()
        val files = findFiles.execute(root, FindFiles.IMAGE)

        val list = ArrayList<ObFileItemView>()

        for (file in files) {
            list.add(ObFileItemView(file))
        }

        list.sortByDescending {
            it.item.lastModified()
        }

        obFileViewImages = ObFileView(root, list)
        reloadImage = false
        return obFileViewImages!!
    }

    fun getVideos(root: File): ObFileView {
        if (obFileViewVideos != null && !reloadVideo) return obFileViewVideos!!

        val findFiles = FindFiles()
        val files = findFiles.execute(root, FindFiles.VIDEO)

        val list = ArrayList<ObFileItemView>()

        for (file in files) {
            list.add(ObFileItemView(file))
        }

        obFileViewVideos = ObFileView(root, list)
        reloadVideo = false
        return obFileViewVideos!!
    }

    fun getAudios(root: File): ObFileView {
        if (obFileViewAudios != null && !reloadAudio) return obFileViewAudios!!

        val findFiles = FindFiles()
        val files = findFiles.execute(root, FindFiles.AUDIO)

        val list = ArrayList<ObFileItemView>()

        for (file in files) {
            list.add(ObFileItemView(file))
        }

        obFileViewAudios = ObFileView(root, list)
        reloadAudio = false
        return obFileViewAudios!!
    }

    fun reset() {
        obFileViewImages = null
        obFileViewVideos = null
        obFileViewAudios = null

        reloadImage = true
        reloadAudio = true
        reloadVideo = true
    }

    fun getFiles(root: File): ObFileView {
        val files = root.listFiles()
        val list = ArrayList<ObFileItemView>()

        for (file in files){
            list.add(ObFileItemView(file))
        }

        return ObFileView(root, list)
    }
}