package br.com.alissontfb.multifilepicker.ui.activity

import android.Manifest
import android.app.Activity
import android.app.LoaderManager
import android.content.Intent
import android.content.Loader
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import br.com.alissontfb.multifilepicker.BuildConfig
import br.com.alissontfb.multifilepicker.R
import br.com.alissontfb.multifilepicker.audio.AudioRecorder
import br.com.alissontfb.multifilepicker.config.CheckPermissions
import br.com.alissontfb.multifilepicker.loader.FileViewLoader
import br.com.alissontfb.multifilepicker.model.FilePickerParams
import br.com.alissontfb.multifilepicker.model.ObFileView
import br.com.alissontfb.multifilepicker.preferences.Preferences
import br.com.alissontfb.multifilepicker.ui.adapter.SectionsPagerAdapter
import br.com.alissontfb.multifilepicker.ui.adapter.filesAdpter.BaseListAdapter
import br.com.alissontfb.multifilepicker.ui.delegate.FilePickerItemsDelegate
import br.com.alissontfb.multifilepicker.ui.fragment.FilePickerFragment
import br.com.alissontfb.multifilepicker.utils.*
import kotlinx.android.synthetic.main.activity_file_picker_tab.*
import kotlinx.android.synthetic.main.audio_rec_layout.*
import kotlinx.android.synthetic.main.file_picker_toolbar.*
import java.io.File


class FilePickerTabActivity : AppCompatActivity(), FilePickerItemsDelegate {

    companion object {
        const val LOADER_FILES_ID = 1
    }

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private lateinit var params: FilePickerParams
    private val selectedFiles = HashMap<String, String>()
    private var picsPath: String = ""
    private var audiosPath: String = ""
    private var moviesPath: String = ""
    private lateinit var currentAdapter: HashMap<Int, BaseListAdapter>
    private var obFileView: ObFileView? = null
    private var savedInstance: Bundle? = null

    private val fileViewCallback = object : LoaderManager.LoaderCallbacks<ObFileView> {

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<ObFileView> {
            return FileViewLoader(this@FilePickerTabActivity, Environment.getExternalStorageDirectory())
        }

        override fun onLoadFinished(loader: Loader<ObFileView>?, data: ObFileView?) {
            if (data != null) {
                obFileView = data

                mSectionsPagerAdapter =
                    SectionsPagerAdapter(this@FilePickerTabActivity, params.tabList, supportFragmentManager)
                container.adapter = mSectionsPagerAdapter
                container.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

                    override fun onPageSelected(position: Int) {
                        selectedFiles.clear()
                        val frag = mSectionsPagerAdapter!!.getItem(position)
                        setUpMenus(frag.type)
                        updateQuantityText(0, params.max)
                    }

                    override fun onPageScrollStateChanged(position: Int) {}
                })
                main_tabs.setupWithViewPager(container)

                main_loader.visibility = View.GONE

            }
        }

        override fun onLoaderReset(loader: Loader<ObFileView>?) {
            obFileView = null
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_picker_tab)
        setSupportActionBar(file_picker_toolbar)

        if (supportActionBar != null) supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        savedInstance = savedInstanceState

        setUpView()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun finish() {
        FilesCache.reset()
        super.finish()
    }

    private fun setUpView() {
        menu_ic_done.setOnClickListener {
            if (selectedFiles.values.isNotEmpty()) {
                val paths = ArrayList<String>()
                paths.addAll(selectedFiles.values)
                val intent = Intent()
                intent.putExtra(PARAM_RESULT_ITEMS_PATHS, paths)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                onBackPressed()
            }
        }

        currentAdapter = HashMap()

        params = FilePickerParams()
        if (intent.hasExtra(INTENT_TO_ACTIVITY_PARAM)) {
            params = intent.getSerializableExtra(INTENT_TO_ACTIVITY_PARAM) as FilePickerParams
            updateQuantityText(0, params.max)
            if (savedInstance == null) {
                loaderManager.initLoader(LOADER_FILES_ID, null, fileViewCallback)
            } else {
                loaderManager.restartLoader(LOADER_FILES_ID, null, fileViewCallback)
            }
        }
    }

    private fun configureTabAdapter() {
        mSectionsPagerAdapter = SectionsPagerAdapter(this, params.tabList, supportFragmentManager)
        container.adapter = mSectionsPagerAdapter
        container.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                if (params.tabList.size == 1 && p0 == p2) {

                    val frag = mSectionsPagerAdapter!!.getItem(p0)
                    setUpMenus(frag.type)
                }
            }

            override fun onPageSelected(position: Int) {
                selectedFiles.clear()
                val frag = mSectionsPagerAdapter!!.getItem(position)
                setUpMenus(frag.type)
                updateQuantityText(0, params.max)
            }

            override fun onPageScrollStateChanged(position: Int) {}
        })
        main_tabs.setupWithViewPager(container)
    }

    override fun setMicOf() {
        menu_ic_mic.visibility = View.GONE
    }

    override fun setUpMenus(type: Int) {
        menu_ic_camera.visibility = View.GONE
        menu_ic_mic.visibility = View.GONE
        main_audio_layout.visibility = View.GONE
        menu_ic_movie.visibility = View.GONE

        menu_ic_camera.setOnClickListener(null)
        menu_ic_mic.setOnClickListener(null)
        menu_ic_movie.setOnClickListener(null)

        if (type == FilePickerFragment.IMAGE_TYPE && params.saveImages) {
            menu_ic_camera.visibility = View.VISIBLE
            menu_ic_camera.setOnClickListener {

                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                val path = File(Environment.getExternalStorageDirectory(), params.folder)

                picsPath = "${path.path}/Images/${System.currentTimeMillis()}.jpg"
                val picFile = File(picsPath)

                var authority = Preferences(this).getProfile() ?: ""
                if (authority.isEmpty())
                    authority = BuildConfig.APPLICATION_ID + ".provider"

                intent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    FileProvider.getUriForFile(
                        this,
                        authority, picFile
                    )
                )

                startActivityForResult(intent, CAMERA_REQUEST)
            }
        } else if (type == FilePickerFragment.AUDIO_TYPE && params.saveAudio) {
            menu_ic_mic.visibility = View.VISIBLE
            menu_ic_mic.setOnClickListener {
                if (main_audio_layout.visibility == View.VISIBLE) {
                    main_audio_layout.visibility = View.GONE
                } else {
                    main_audio_layout.visibility = View.VISIBLE
                    val path = File(Environment.getExternalStorageDirectory(), params.folder)

                    audiosPath = "${path.path}/Audios/${System.currentTimeMillis()}.mp3"

                    try {
                        AudioRecorder(main_fab_play, main_fab_rec, main_fab_stop, audiosPath, {
                            FilesCache.reset()
                            this.currentAdapter[FindFiles.AUDIO]?.putFile(it)
                            val adapter = this.currentAdapter[FindFiles.AUDIO]
                            val select = adapter?.getSelectedFiles()?.size ?: 0
                            updateQuantityText(select, params.max)
                        }, {
                            runOnUiThread {
                                main_time_audio.text = it
                            }
                        })
                    } catch (e: Exception) {
                        Toast.makeText(this, getString(R.string.not_enable_mic), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else if (type == FilePickerFragment.VIDEO_TYPE && params.saveVideo) {
            menu_ic_movie.visibility = View.VISIBLE
            menu_ic_movie.setOnClickListener {

                val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                val path = File(Environment.getExternalStorageDirectory(), params.folder)

                moviesPath = "${path.path}/Videos/${System.currentTimeMillis()}.mp4"
                val picFile = File(moviesPath)

                var authority = Preferences(this).getProfile() ?: ""
                if (authority.isEmpty())
                    authority = BuildConfig.APPLICATION_ID + ".provider"

                intent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    FileProvider.getUriForFile(
                        this,
                        authority, picFile
                    )
                )

                startActivityForResult(intent, MOVIE_REQUEST)
            }
        } else {
            menu_ic_camera.visibility = View.GONE
            menu_ic_mic.visibility = View.GONE
            main_audio_layout.visibility = View.GONE
            menu_ic_movie.visibility = View.GONE

            menu_ic_camera.setOnClickListener(null)
            menu_ic_mic.setOnClickListener(null)
            menu_ic_movie.setOnClickListener(null)
        }
    }

    override fun addItemsSelected(item: File) {
        if (selectedFiles.values.size <= params.max) {
            selectedFiles.getOrPut(item.path) {
                item.path
            }
            updateQuantity(selectedFiles.values.size, params.max)
        } else
            Toast.makeText(this, getString(R.string.max_reached), Toast.LENGTH_SHORT).show()

    }

    override fun removeItemsSelected(item: File) {
        selectedFiles.remove(item.path)
        updateQuantity(selectedFiles.values.size, params.max)
    }

    override fun getParams() = params

    private fun updateQuantityText(min: Int, max: Int) {
        file_picker_qnt_files.text = getString(R.string.selected_items, min, max)
    }

    override fun updateQuantity(min: Int, max: Int) {
        updateQuantityText(min, max)
    }

    override fun getSelectedItems() = selectedFiles

    override fun setAdapter(adapter: BaseListAdapter, key: Int) {
        this.currentAdapter[key] = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (Activity.RESULT_OK == resultCode) {

            if (requestCode == CAMERA_REQUEST) {
                FilesCache.reset()
                this.currentAdapter[FindFiles.IMAGE]?.putFile(File(picsPath))
                val adapter = this.currentAdapter[FindFiles.IMAGE]
                val select = adapter?.getSelectedFiles()?.size ?: 0
                updateQuantityText(select, params.max)
            } else if (requestCode == MOVIE_REQUEST) {
                FilesCache.reset()
                this.currentAdapter[FindFiles.VIDEO]?.putFile(File(moviesPath))
                val adapter = this.currentAdapter[FindFiles.VIDEO]
                val select = adapter?.getSelectedFiles()?.size ?: 0
                updateQuantityText(select, params.max)
            } else if (requestCode == AUDIO_REQUEST) {
                FilesCache.reset()
                this.currentAdapter[FindFiles.AUDIO]?.putFile(File(audiosPath))
            }
        }
    }

}
