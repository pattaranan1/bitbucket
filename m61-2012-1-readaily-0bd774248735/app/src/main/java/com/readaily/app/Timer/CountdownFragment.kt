package com.readaily.app.Timer

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.readaily.app.DBHandler
import com.readaily.app.R
import com.readaily.app.ToDoList.ToDoitem
import kotlinx.android.synthetic.main.fragment_countdown.*
import java.util.*
import java.util.concurrent.TimeUnit


class CountdownFragment : Fragment() {

    lateinit var dbHandler: DBHandler

    enum class TimerState {
        Stopped, Paused, Running
    }

    private var timerState = TimerState.Stopped


    var totaltimerlength: Long = 0
    val countDownInterval: Long = 1000
    var namelist = arrayListOf<String>()

    lateinit var spinner_adapter: ArrayAdapter<String>

    var timesetting = 0

    var toDoitem: ToDoitem = ToDoitem()
    var isPause = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_countdown, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHandler = DBHandler(requireContext())
        hour.maxValue = 23
        hour.minValue = 0
        minute.maxValue = 59
        minute.minValue = 0
        second.maxValue = 59
        second.minValue = 0

        select_time_view.isVisible = true
        running_time_view.isVisible = false



        namelist = arrayListOf()
        for (i in dbHandler.getToDos()) {
            namelist.add(i.name)
        }

        spinner_adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, namelist)

        select_list_countdown.adapter = spinner_adapter
        spinner_adapter.notifyDataSetChanged()



        select_list_countdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selected_item = namelist.get(p2)
                toDoitem = dbHandler.selectToDoItem(selected_item)
            }
        }

        btn_play_pause.setOnClickListener {


            if (timerState == TimerState.Stopped) {//Stop -> Running

                setNewTimerLength()


                if (totaltimerlength.toInt() != 0) {


                    select_list_countdown.isEnabled = false

                    timesetting = (totaltimerlength / 1000).toInt()


                    select_time_view.isVisible = false
                    running_time_view.isVisible = true
                    btn_play_pause.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            android.R.drawable.ic_media_pause
                        )
                    )
                    timerState = TimerState.Running

                    timer(totaltimerlength, countDownInterval).start()
                } else {
                    Toast.makeText(requireContext(), "Please Set Your Time", Toast.LENGTH_SHORT)
                        .show()
                }

            } else if (timerState == TimerState.Running) { //Running -> Paused
                btn_play_pause.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        android.R.drawable.ic_media_play
                    )
                )
                timerState = TimerState.Paused

            } else if (timerState == TimerState.Paused) { //Pause -> Running
                btn_play_pause.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        android.R.drawable.ic_media_pause
                    )
                )
                timerState = TimerState.Running
                timer(totaltimerlength, countDownInterval).start()
            }


        }

        btn_stop.setOnClickListener {


            select_list_countdown.isEnabled = true
            timerState = TimerState.Stopped

            if(isPause){
                Toast.makeText(requireContext(), messagetimeString(timesetting.toLong()*1000 - (totaltimerlength)), Toast.LENGTH_LONG).show()
                toDoitem.totaltimeItem = toDoitem.totaltimeItem + (timesetting - (totaltimerlength / 1000).toInt())
                dbHandler.updateToDoitem(toDoitem)
                isPause = false
            }

            totaltimerlength = 0

            select_time_view.isVisible = true
            running_time_view.isVisible = false

            btn_play_pause.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    android.R.drawable.ic_media_play
                )
            )

            hour.value = 0
            minute.value = 0
            second.value = 0

        }


    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
    }

    private fun setNewTimerLength() {

        val hourlength = hour.value
        val minutelength = minute.value
        val secondlength = second.value


        totaltimerlength = (hourlength * 3600 + minutelength * 60 + secondlength).toLong() * 1000

    }

    private fun timer(millisInFuture: Long, countDownInterval: Long): CountDownTimer {
        return object : CountDownTimer(millisInFuture, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                val timeRemaining = timeString(millisUntilFinished)
                if (timerState == TimerState.Paused || timerState == TimerState.Stopped) {

                    cancel()

                    totaltimerlength = millisUntilFinished

                    if(timerState == TimerState.Stopped){
                        Toast.makeText(requireContext(), messagetimeString(timesetting.toLong()*1000 - (totaltimerlength)), Toast.LENGTH_LONG).show()
                        toDoitem.totaltimeItem = toDoitem.totaltimeItem + (timesetting - (totaltimerlength / 1000).toInt())
                        dbHandler.updateToDoitem(toDoitem)
                    }
                    else{
                        isPause = true
                    }



                } else {
                    running_time.text = timeRemaining
                }
            }

            override fun onFinish() {
                Toast.makeText(requireContext(), messagetimeString(timesetting.toLong()*1000), Toast.LENGTH_LONG).show()
                timerState = TimerState.Stopped

                toDoitem.totaltimeItem = toDoitem.totaltimeItem + timesetting
                totaltimerlength = 0
                timesetting = 0

                dbHandler.updateToDoitem(toDoitem)

                select_list_countdown.isEnabled = true
                select_time_view.isVisible = true
                running_time_view.isVisible = false

                btn_play_pause.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        android.R.drawable.ic_media_play
                    )
                )

                hour.value = 0
                minute.value = 0
                second.value = 0
            }
        }
    }

    private fun timeString(millis: Long): String {
        var millisUntilFinished: Long = millis

        val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
        millisUntilFinished -= TimeUnit.HOURS.toMillis(hours)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
        millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes)

        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)

        // Format the string
        return String.format(
            Locale.getDefault(),
            "%02d:%02d:%02d",
            hours, minutes, seconds
        )

    }

    private fun messagetimeString(millis:Long):String{
        var millisUntilFinished:Long = millis

        val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
        millisUntilFinished -= TimeUnit.HOURS.toMillis(hours)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
        millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes)

        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)

        // Format the string
        return String.format(
            Locale.getDefault(),
            "Total time: %d hours %d minutes %d seconds",
            hours, minutes,seconds
        )

    }
}






