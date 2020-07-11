package com.readaily.app

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.readaily.app.ToDoList.ToDo
import com.readaily.app.ToDoList.ToDoitem


class DBHandler (val context: Context): SQLiteOpenHelper(context,
    DB_NAME,null,
    DB_VERSION
){

    override fun onCreate(db: SQLiteDatabase) {

        val createToDoTable = "  CREATE TABLE $TABLE_TODO (" +
                "$COL_ID integer PRIMARY KEY AUTOINCREMENT," +
                "$COL_DATE varchar," +
                "$COL_NAME varchar,"+
                "$COL_COMMENT varchar,"+
                "$COL_SUBJECT varchar,"+
                "$COL_IS_COMPLETED integer);"

        val createToDoItemTable = "  CREATE TABLE $TABLE_TODO_ITEM (" +
                "$COL_ID_ITEM integer PRIMARY KEY AUTOINCREMENT," +
                "$COL_DATE_ITEM varchar," +
                "$COL_NAME_ITEM varchar,"+
                "$COL_COMMENT_ITEM varchar,"+
                "$COL_SUBJECT_ITEM varchar,"+
                "$COL_IS_COMPLETED_ITEM integer,"+
                "$COL_TOTAL_TIME_ITEM integer);"

        db.execSQL(createToDoTable)
        db.execSQL(createToDoItemTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }

    fun addToDo(toDo: ToDo): Boolean {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME, toDo.name)
        cv.put(COL_COMMENT, toDo.comment)
        cv.put(COL_SUBJECT, toDo.subject)
        cv.put(COL_DATE, toDo.date)

        val result = db.insert(TABLE_TODO, null, cv)
        return result != (-1).toLong()
    }

    fun addToDoItem(toDoitem: ToDoitem): Boolean {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME_ITEM, toDoitem.nameItem)
        cv.put(COL_COMMENT_ITEM, toDoitem.commentItem)
        cv.put(COL_SUBJECT_ITEM, toDoitem.subjectItem)
        cv.put(COL_DATE_ITEM, toDoitem.dateItem)
        cv.put(COL_TOTAL_TIME_ITEM,toDoitem.totaltimeItem)

        val result = db.insert(TABLE_TODO_ITEM, null, cv)
        return result != (-1).toLong()
    }

    fun updateToDo(toDo: ToDo) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME, toDo.name)
        cv.put(COL_COMMENT, toDo.comment)
        cv.put(COL_SUBJECT, toDo.subject)
        cv.put(COL_DATE, toDo.date)
        cv.put(COL_IS_COMPLETED, toDo.isCompleted)

        db.update(TABLE_TODO,cv,"$COL_ID=?" , arrayOf(toDo.id.toString()))
    }

    fun updateToDoitem(toDoitem: ToDoitem) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME_ITEM, toDoitem.nameItem)
        cv.put(COL_COMMENT_ITEM, toDoitem.commentItem)
        cv.put(COL_SUBJECT_ITEM, toDoitem.subjectItem)
        cv.put(COL_DATE_ITEM, toDoitem.dateItem)
        cv.put(COL_IS_COMPLETED_ITEM, toDoitem.isCompletedItem)
        cv.put(COL_TOTAL_TIME_ITEM,toDoitem.totaltimeItem)

        db.update(TABLE_TODO_ITEM,cv,"$COL_ID_ITEM=?" , arrayOf(toDoitem.idItem.toString()))
    }

    fun getToDos(): MutableList<ToDo> {
        val result: MutableList<ToDo> = ArrayList()
        val db = readableDatabase
        val queryResult = db.rawQuery("SELECT * FROM $TABLE_TODO", null)
        if (queryResult.moveToFirst()) {
            do {
                val todo = ToDo()
                todo.id = queryResult.getLong(queryResult.getColumnIndex(COL_ID))
                todo.name = queryResult.getString(queryResult.getColumnIndex(COL_NAME))
                todo.comment = queryResult.getString(queryResult.getColumnIndex(COL_COMMENT))
                todo.subject = queryResult.getString(queryResult.getColumnIndex(COL_SUBJECT))
                todo.date = queryResult.getString(queryResult.getColumnIndex(COL_DATE))
                todo.isCompleted = queryResult.getInt(queryResult.getColumnIndex(COL_IS_COMPLETED)) == 1
                result.add(todo)
            } while (queryResult.moveToNext())
        }
        queryResult.close()
        return result
    }

    fun getToDoitems(): MutableList<ToDoitem> {
        val result: MutableList<ToDoitem> = ArrayList()
        val db = readableDatabase
        val queryResult = db.rawQuery("SELECT * FROM $TABLE_TODO_ITEM", null)
        if (queryResult.moveToFirst()) {
            do {
                val todoitem = ToDoitem()
                todoitem.idItem = queryResult.getLong(queryResult.getColumnIndex(COL_ID_ITEM))
                todoitem.nameItem = queryResult.getString(queryResult.getColumnIndex(COL_NAME_ITEM))
                todoitem.commentItem = queryResult.getString(queryResult.getColumnIndex(COL_COMMENT_ITEM))
                todoitem.subjectItem = queryResult.getString(queryResult.getColumnIndex(COL_SUBJECT_ITEM))
                todoitem.dateItem = queryResult.getString(queryResult.getColumnIndex(COL_DATE_ITEM))
                todoitem.isCompletedItem = queryResult.getInt(queryResult.getColumnIndex(COL_IS_COMPLETED_ITEM)) == 1
                todoitem.totaltimeItem = queryResult.getInt(queryResult.getColumnIndex(COL_TOTAL_TIME_ITEM))
                result.add(todoitem)
            } while (queryResult.moveToNext())
        }
        queryResult.close()
        return result
    }

    fun deleteToDo(itemId : Long){
        val db = writableDatabase
        db.delete(TABLE_TODO,"$COL_ID=?" , arrayOf(itemId.toString()))
    }

    fun selectToDoItem(name:String):ToDoitem{
        val toDoitem = ToDoitem()
        val db = readableDatabase
        val queryResult = db.rawQuery("SELECT * FROM $TABLE_TODO_ITEM WHERE $COL_NAME_ITEM = ?", arrayOf(name))
        if(queryResult.moveToFirst()){
            do {
                toDoitem.idItem = queryResult.getLong(queryResult.getColumnIndex(COL_ID_ITEM))
                toDoitem.nameItem = queryResult.getString(queryResult.getColumnIndex(COL_NAME_ITEM))
                toDoitem.commentItem = queryResult.getString(queryResult.getColumnIndex(COL_COMMENT_ITEM))
                toDoitem.subjectItem = queryResult.getString(queryResult.getColumnIndex(COL_SUBJECT_ITEM))
                toDoitem.dateItem = queryResult.getString(queryResult.getColumnIndex(COL_DATE_ITEM))
                toDoitem.isCompletedItem = queryResult.getInt(queryResult.getColumnIndex(COL_IS_COMPLETED_ITEM)) == 1
                toDoitem.totaltimeItem = queryResult.getInt(queryResult.getColumnIndex(COL_TOTAL_TIME_ITEM))
            }while (queryResult.moveToNext())
        }
        queryResult.close()
        return toDoitem
    }


}

