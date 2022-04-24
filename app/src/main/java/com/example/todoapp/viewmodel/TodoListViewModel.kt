package com.example.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.todoapp.model.Todo
import com.example.todoapp.model.TodoDatabase
import com.example.todoapp.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class TodoListViewModel(application: Application) : AndroidViewModel(application), CoroutineScope{
    val todoLD = MutableLiveData<List<Todo>>()
    val todoLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()

    private  var job  = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    fun refresh(){
        todoLoadErrorLD.value = false
        loadingLD.value = true
        launch {
            val db = buildDb(getApplication())
            todoLD.value = db.todoDatabase().selectAllTodo()
        }
    }

    fun clearTask(todo: Todo){
        launch {
            val db = Room.databaseBuilder(getApplication(), TodoDatabase::class.java,"newtododb").build()
            db.todoDatabase().deleteTodo(todo)
            todoLD.value = db.todoDatabase().selectAllTodo()
        }
    }

    fun doneTask(todo: Todo){
        launch {
            val db = Room.databaseBuilder(getApplication(), TodoDatabase::class.java,"newtododb").build()
            db.todoDatabase().doneTask(todo.uuid, 1)
            todoLD.value = db.todoDatabase().selectAllTodo()
        }
    }
}