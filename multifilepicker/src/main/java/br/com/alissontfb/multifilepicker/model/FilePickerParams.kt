package br.com.alissontfb.multifilepicker.model

import java.io.Serializable

class FilePickerParams: Serializable{

    var max: Int = 0
    var showAudio = false
    var showImages = false
    var showVideo = false
    var saveAudio = false
    var saveImages = false
    var saveVideo = false
    var folder = "PikerLib"
    var tabList: ArrayList<String> = ArrayList()

}