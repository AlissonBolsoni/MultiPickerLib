package br.com.alissontfb.multifilepicker.audio

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.view.View
import android.widget.TextView
import java.io.File
import java.io.IOException


class AudioRecorder(private val play: View?,
                    private val rec: View,
                    private val stop: View,
                    private val outputFile: String,
                    private val save: (File) -> Unit,
                    private val timer: (String) -> Unit) {

    private var myAudioRecorder: MediaRecorder?

    init {
        myAudioRecorder = MediaRecorder()
        val time: TimerAsync
        try {
            myAudioRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            myAudioRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            myAudioRecorder!!.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
            myAudioRecorder!!.setOutputFile(outputFile)
            stop.visibility = View.GONE
            play?.visibility = View.GONE
            time = TimerAsync {
                timer(it)
            }
        } catch (e: Exception) {
            throw e
        }

        rec.setOnClickListener {
            try {
                time.start()

                myAudioRecorder!!.prepare()
                myAudioRecorder!!.start()

            } catch (ise: IllegalStateException) {
                throw ise
            } catch (ioe: IOException) {
                throw ioe
            }
            rec.visibility = View.GONE
            stop.visibility = View.VISIBLE
        }

        stop.setOnClickListener {
            myAudioRecorder!!.stop()
            myAudioRecorder!!.release()
            myAudioRecorder = null
            rec.visibility = View.VISIBLE
            stop.visibility = View.GONE
            play?.visibility = View.VISIBLE
            time.stop()
            save(getFile())
        }

        play?.setOnClickListener {
            val mediaPlayer = MediaPlayer()
            try {
                mediaPlayer.setDataSource(outputFile)
                mediaPlayer.prepare()
                mediaPlayer.start()
            } catch (e: Exception) {
                throw e
            }
        }
    }

    private fun getFile() = File(outputFile)

}