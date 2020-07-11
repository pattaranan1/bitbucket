package com.readaily.app.Timer

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.readaily.app.DBHandler
import com.readaily.app.R
import com.readaily.app.ToDoList.ToDoitem
import kotlinx.android.synthetic.main.fragment_stopwatch.*
import java.util.*
import java.util.concurrent.TimeUnit

class StopwatchFragment : Fragment() {

    internal var MillisecondTime: Long = 0
    internal var StartTime: Long = 0
    internal var TimeBuff: Long = 0
    internal var UpdateTime = 0L


    var handler: Handler? = null

    lateinit var dbHandler: DBHandler
    enum class TimerState{
        Stopped, Paused, Running
    }

    private var timerState = TimerState.Stopped

    lateinit var spinner_adapter: ArrayAdapter<String>
    var namelist = arrayListOf<String>()
    var toDoitem: ToDoitem = ToDoitem()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stopwatch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHandler = DBHandler(requireContext())

        namelist = arrayListOf()
        for(i in dbHandler.getToDos()){
            namelist.add(i.name)
        }

        spinner_adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,namelist)

        select_list_stopwatch.adapter = spinner_adapter
        spinner_adapter.notifyDataSetChanged()




        select_list_stopwatch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selected_item = namelist.get(p2)
                toDoitem = dbHandler.selectToDoItem(selected_item)
            }
        }

        btn_play_pause2.setOnClickListener {

            if(timerState == TimerState.Stopped) {//Stop -> Running
                select_list_stopwatch.isEnabled = false

                btn_play_pause2.setImageDrawable(ContextCompat.getDrawable(requireContext(),android.R.drawable.ic_media_pause))
                StartTime = SystemClock.uptimeMillis()
                handler?.postDelayed(runnable, 0)
                timerState = TimerState.Running
            }
            else if (timerState == TimerState.Running){ //Running -> Paused
                btn_play_pause2.setImageDrawable(ContextCompat.getDrawable(requireContext(),android.R.drawable.ic_media_play))
                handler?.removeCallbacks(runnable)
                TimeBuff = UpdateTime
                timerState = TimerState.Paused

            }
            else if (timerState == TimerState.Paused){ //Pause -> Running
                btn_play_pause2.setImageDrawable(ContextCompat.getDrawable(requireContext(),android.R.drawable.ic_media_pause))
                StartTime = SystemClock.uptimeMillis()
                handler?.postDelayed(runnable, 0)
                timerState = TimerState.Running
            }

        }

        handler = Handler()

        btn_stop2.setOnClickListener {
            timerState = TimerState.Stopped
            btn_play_pause2.setImageDrawable(ContextCompat.getDrawable(requireContext(),android.R.drawable.ic_media_play))

            toDoitem.totaltimeItem = toDoitem.totaltimeItem + UpdateTime.toInt() / 1000
            dbHandler.updateToDoitem(toDoitem)
            Toast.makeText(requireContext(),messagetimeString(UpdateTime),Toast.LENGTH_LONG).show()
            TimeBuff = 0
            handler?.removeCallbacks(runnable)
            select_list_stopwatch.isEnabled = true
   
            stopwatch_running_time.text = "00:00:00"
        }
    }

    var runnable:Runnable = object : Runnable {

        override fun run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime

            UpdateTime = TimeBuff + MillisecondTime

            stopwatch_running_time?.text = timeString(UpdateTime)
            handler?.postDelayed(this, 0)
        }

    }

    private fun timeString(millis:Long):String{
        var millisUntilFinished:Long = millis

        val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
        millisUntilFinished -= TimeUnit.HOURS.toMillis(hours)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
        millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes)

        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)

        // Format the string
        return String.format(
            Locale.getDefault(),
            "%02d:%02d:%02d",
            hours, minutes,seconds
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

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
    }

}