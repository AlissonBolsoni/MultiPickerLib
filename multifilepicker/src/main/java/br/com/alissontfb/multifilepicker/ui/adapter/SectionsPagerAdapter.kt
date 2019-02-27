package br.com.alissontfb.multifilepicker.ui.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import br.com.alissontfb.multifilepicker.R
import br.com.alissontfb.multifilepicker.ui.fragment.*

class SectionsPagerAdapter(
        private val context: Context,
        tabs: ArrayList<String>,
        fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val tabsTitles = tabs

    override fun getItem(position: Int): FilePickerFragment {
        return when {
            tabsTitles[position] == context.getString(R.string.images_tab) -> ImagesFragment()
            tabsTitles[position] == context.getString(R.string.videos_tab) -> VideoFragment()
            tabsTitles[position] == context.getString(R.string.audios_tab) -> AudioFragment()
            else -> FilesFragment()
        }
    }

    override fun getCount(): Int {
        return tabsTitles.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabsTitles[position]
    }
}