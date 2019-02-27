package br.com.alissontfb.multifilepicker.ui.fragment

import br.com.alissontfb.multifilepicker.model.ObFileView
import br.com.alissontfb.multifilepicker.utils.FilesCache
import br.com.alissontfb.multifilepicker.utils.FindFiles

class VideoFragment: FilePickerFragment(FilePickerFragment.VIDEO_TYPE) {

    override fun initAdapter(): ObFileView {
        return FilesCache.getVideos(root)
    }

    override fun setAdapter() {
        delegate.setAdapter(adapter!!, FindFiles.VIDEO)
    }
}