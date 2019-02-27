package br.com.alissontfb.multifilepicker.model

import java.io.File
import java.util.*

class ObFileView(
        var root: File,
        var itemsInPath: ArrayList<ObFileItemView>
        )

class ObFileItemView(
        val item: File
){
        var checked = false
}
