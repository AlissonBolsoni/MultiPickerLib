package br.com.alissontfb.multifilepicker.audio

import java.text.SimpleDateFormat
import java.util.*

internal class TimerAsync(private val update:(String) -> Unit):Runnable {
    companion object {
        const val RUNNING = 10
        const val STOP = 20
        const val STOPPING = 30
        val SDF = SimpleDateFormat("mm:ss", Locale.getDefault())
    }

    private var status = STOP
    private var time = Calendar.getInstance()
    private var dif = 0L

    fun start(){
        time = Calendar.getInstance()
        if (status == STOP){
            status = RUNNING
            Thread(this).start()
        }else{
            status = RUNNING
        }
    }

    fun stop(){
        status = STOPPING
        dif = 0L
        update(SDF.format(Date(dif)))
    }

    override fun run() {
        while (status == RUNNING){
            update(SDF.format(Date(dif)))
            Thread.sleep(1_000)
            dif += 1000
        }
        status = STOP
    }


}