package br.com.alissontfb.multifilepicker.utils

import br.com.alissontfb.multifilepicker.model.ObFileItemView
import br.com.alissontfb.multifilepicker.model.ObFileView
import java.io.File
import java.io.FilenameFilter


class FindFiles {
    companion object {

        val EXTENSIONS_IMAGES = arrayOf("jpeg", "jpg", "png", "tif", "tiff")
        val EXTENSIONS_VIDEOS = arrayOf("mpeg", "webm", "avi", "mp4")
        val EXTENSIONS_AUDIOS = arrayOf("wav", "weba", "aac", "mid", "midi", "mp3")

        const val IMAGE = 10
        const val VIDEO = 20
        const val AUDIO = 30
        const val FILES = 40
    }

    private val IMAGE_FILTER: FilenameFilter = object : FilenameFilter {
        override fun accept(dir: File, name: String): Boolean {
            for (ext in EXTENSIONS_IMAGES) {
                if (name.endsWith(".$ext")) {
                    return true
                }
            }
            return false
        }
    }

    private val AUDIO_FILTER: FilenameFilter = object : FilenameFilter {
        override fun accept(dir: File, name: String): Boolean {
            for (ext in EXTENSIONS_AUDIOS) {
                if (name.endsWith(".$ext")) {
                    return true
                }
            }
            return false
        }
    }

    private val VIDEO_FILTER: FilenameFilter = object : FilenameFilter {
        override fun accept(dir: File, name: String): Boolean {
            for (ext in EXTENSIONS_VIDEOS) {
                if (name.endsWith(".$ext")) {
                    return true
                }
            }
            return false
        }
    }

    fun execute(dir: File, fileType: Int, returnList: ArrayList<File> = ArrayList()): ArrayList<File> {
        var list = dir.listFiles(IMAGE_FILTER)
        if (fileType == VIDEO)
            list = dir.listFiles(VIDEO_FILTER)
        if (fileType == AUDIO)
            list = dir.listFiles(AUDIO_FILTER)

        if (!list.isNullOrEmpty()) {
            for (f in list) {
                returnList.add(f)
            }
        }

        for (path in dir.listFiles()) {
            if (path.isDirectory){
                execute(path, fileType, returnList)
            }
        }
        return returnList
    }


}