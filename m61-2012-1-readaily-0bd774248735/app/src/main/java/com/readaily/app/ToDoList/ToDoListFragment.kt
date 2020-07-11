package com.readaily.app.ToDoList

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.readaily.app.DBHandler
import com.readaily.app.R
import kotlinx.android.synthetic.main.fragment_to_do_list.*

class ToDoListFragment : Fragment() {

    var todoId: Long = -1
    lateinit var dbHandler: DBHandler
    lateinit var  option: Spinner
    lateinit var  result : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_to_do_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHandler = DBHandler(requireContext())
        to_do_list_rv.layoutManager = LinearLayoutManager(requireContext())
        fab_btn.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
            val view = layoutInflater.inflate(R.layout.dialog_to_do_list, null)
            val toDoName = view.findViewById<EditText>(R.id.editText_todo_name)
            val toDoComment = view.findViewById<EditText>(R.id.commentText)
            val toDoCalendar = view.findViewById<EditText>(R.id.calendarText)

            dialog.setView(view)

            val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
            calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
                //Note that months are indexed from 0. So, 0 means january, 1 means February, 2 means march etc.
                val msg = "Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year
                toDoCalendar.setText(dayOfMonth.toString() + "/" + (month + 1) + "/" + year)
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }

            val option =  view.findViewById<Spinner>(R.id.spinner_subject)
            var result =  ""

            val options = arrayListOf("Mathematics","Physics","Chemistry","Biology","Computer","English","Thai","Social","History")
            option.adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1,options)
            option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(p0: AdapterView<*>?) {
//                    result.text = "Please Select Options"
                }
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    result = options.get(p2)
                }

            }

            dialog.setPositiveButton("Add") { _: DialogInterface, _: Int ->
                if (toDoName.text.isNotEmpty()) {
                    val toDo = ToDo()
                    val toDoitem = ToDoitem()

                    toDo.name = toDoName.text.toString()
                    toDo.comment = toDoComment.text.toString()
                    toDo.subject = result
                    toDo.date = toDoCalendar.text.toString()

                    toDoitem.nameItem = toDoName.text.toString()
                    toDoitem.commentItem = toDoComment.text.toString()
                    toDoitem.subjectItem = result
                    toDoitem.dateItem = toDoCalendar.text.toString()
                    toDoitem.totaltimeItem = 0

                    dbHandler.addToDo(toDo)
                    dbHandler.addToDoItem(toDoitem)
                    refreshList()

                }
            }
            dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->
            }
            dialog.show()
        }
    }


    fun updateToDo(toDo: ToDo,toDoitem:ToDoitem){
        val dialog = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_to_do_list, null)
        val toDoName = view.findViewById<EditText>(R.id.editText_todo_name)
        val toDoComment = view.findViewById<EditText>(R.id.commentText)
        val toDoCalendar = view.findViewById<EditText>(R.id.calendarText)
        toDoName.setText(toDo.name)
        toDoComment.setText(toDo.comment)
        toDoCalendar.setText(toDo.date)
        dialog.setView(view)

        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            //Note that months are indexed from 0. So, 0 means january, 1 means February, 2 means march etc.
            val msg = "Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year
            toDoCalendar.setText(dayOfMonth.toString() + "/" + (month + 1) + "/" + year)
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }

        val option =  view.findViewById<Spinner>(R.id.spinner_subject)
        var result =  ""

        val options = arrayListOf("Mathematics","Physics","Chemistry","Biology","Computer","English","Thai","Social","History")
        option.adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1,options)
        val spinnerPosition = options.indexOf(toDo.subject)
        option.setSelection(spinnerPosition)
        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
//                    result.text = "Please Select Options"
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                result = options.get(p2)
            }
        }

        dialog.setPositiveButton("Update") { _: DialogInterface, _: Int ->
            if (toDoName.text.isNotEmpty()) {
                toDo.name = toDoName.text.toString()
                toDo.comment = toDoComment.text.toString()
                toDo.subject = result
                toDo.date = toDoCalendar.text.toString()

                toDoitem.nameItem = toDoName.text.toString()
                toDoitem.commentItem = toDoComment.text.toString()
                toDoitem.subjectItem = result
                toDoitem.dateItem = toDoCalendar.text.toString()

                dbHandler.updateToDo(toDo)
                dbHandler.updateToDoitem(toDoitem)
                refreshList()

            }
        }
        dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->
        }
        dialog.show()

    }

    override fun onResume() {
        refreshList()
        super.onResume()
    }

    private fun refreshList(){
        to_do_list_rv.adapter = ToDoListAdapter(this,dbHandler.getToDos())
    }

    class ToDoListAdapter(val activity: ToDoListFragment, val list: MutableList<ToDo>) :
        RecyclerView.Adapter<ToDoListAdapter.ViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(activity.requireContext()).inflate(R.layout.to_do_list_child, p0, false))
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
            holder.toDoSubject.text = list[p1].subject
            holder.toDoName.text = list[p1].name
            holder.toDoDate.text = list[p1].date
            holder.toDoCheckBox.isChecked = list[p1].isCompleted
            holder.toDoCheckBox.setOnClickListener {
                list[p1].isCompleted = !list[p1].isCompleted
                activity.dbHandler.updateToDo(list[p1])
            }

            holder.delete.setOnClickListener {
                val dialog = AlertDialog.Builder(it.context)
                dialog.setTitle("Are you sure")
                dialog.setMessage("Do you want to delete this item ?")
                dialog.setPositiveButton("Continue") { _: DialogInterface, _: Int ->
                    activity.dbHandler.deleteToDo(list[p1].id)
                    activity.refreshList()
                }
                dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->

                }
                dialog.show()
            }





            holder.edit.setOnClickListener {

                val temp = activity.dbHandler.selectToDoItem(list[p1].name)
                activity.updateToDo(list[p1],temp)
            }
        }

        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val toDoName : TextView = v.findViewById(R.id.tvchild_name)
            val toDoSubject : TextView = v.findViewById(R.id.tvchild_subject)
            val toDoDate : TextView = v.findViewById(R.id.tvchild_date)
            val toDoCheckBox: CheckBox = v.findViewById(R.id.cb_item)
            val edit:ImageView =v.findViewById(R.id.iv_edit)
            val delete:ImageView =v.findViewById(R.id.iv_delete)
        }
    }
}


