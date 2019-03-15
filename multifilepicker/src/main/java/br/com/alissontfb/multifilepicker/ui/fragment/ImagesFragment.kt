package br.com.alissontfb.multifilepicker.ui.fragment

import br.com.alissontfb.multifilepicker.model.ObFileView
import br.com.alissontfb.multifilepicker.utils.FilesCache.getImages
import br.com.alissontfb.multifilepicker.utils.FindFiles

class ImagesFragment : FilePickerFragment(FilePickerFragment.IMAGE_TYPE) {

    override fun initAdapter(): ObFileView {
        return getImages()
    }

    override fun setAdapter() {
        delegate.setAdapter(adapter!!, FindFiles.IMAGE)
    }
}