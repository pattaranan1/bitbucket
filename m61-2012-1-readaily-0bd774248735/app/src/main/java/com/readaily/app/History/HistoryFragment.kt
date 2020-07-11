package com.readaily.app.History

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.readaily.app.DBHandler
import com.readaily.app.R
import com.readaily.app.ToDoList.ToDoitem
import kotlinx.android.synthetic.main.fragment_history.*
import java.util.*
import java.util.concurrent.TimeUnit


class HistoryFragment : Fragment() {


    lateinit var dbHandler: DBHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        dbHandler = DBHandler(requireContext())

        val historylist = arrayListOf<ToDoitem>()
        for(i in dbHandler.getToDoitems()){
            if (i.totaltimeItem != 0){
                historylist.add(i)
            }
        }
        val layoutManager = LinearLayoutManager(requireContext())
        history_view.layoutManager = layoutManager

        val adapter = CustomAdapter(this, historylist)
        history_view.adapter = adapter
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
    }
}

class CustomAdapter(val activity: HistoryFragment, val list: MutableList<ToDoitem>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(activity.requireContext()).inflate(
                R.layout.history_child,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.topic.text = list[position].nameItem
        holder.subject.text = list[position].subjectItem
        holder.date.text = list[position].dateItem
        holder.time.text = messagetimeString((list[position].totaltimeItem).toLong()*1000)
    }


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val subject: TextView = v.findViewById(R.id.subject)
        val topic: TextView = v.findViewById(R.id.topic)
        val date: TextView = v.findViewById(R.id.date)
        val time: TextView = v.findViewById(R.id.timer)


    }

    private fun messagetimeString(millis: Long): String {
        var millisUntilFinished: Long = millis

        val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
        millisUntilFinished -= TimeUnit.HOURS.toMillis(hours)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
        millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes)

        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)

        // Format the string
        return String.format(
            Locale.getDefault(),
            "%d hours %d minutes %d seconds",
            hours, minutes, seconds
        )
    }
}
