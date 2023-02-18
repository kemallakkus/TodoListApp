package com.kemalakkus.todolistapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kemalakkus.todolistapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var todoAdapter: TaskItemAdapter
    private lateinit var todo: TaskItem
    private val taskViewModel: TaskViewModel by viewModels {
        TaskItemModelFactory((application as TodoApplication).repository)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        binding.newTaskButton.setOnClickListener {
            NewTaskSheetFragment(null).show(supportFragmentManager, "newTaskTag")
        }
        setRecyclerView()
        delete()
        editTaskItem()
        completeTaskItem()
        //delete()


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun delete(){
        binding.delete.setOnClickListener {
            todoAdapter.differ.currentList.forEach{ taskModel ->
                if (taskModel.isCompleted()){
                    taskViewModel.delete(taskModel)
                    Toast.makeText(this,"Deleted succesfully", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }



    private fun setRecyclerView()
    {
        todoAdapter = TaskItemAdapter()

            binding.rvTodoList.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = todoAdapter
        }
        taskViewModel.taskItems.observe(this, Observer {
            todoAdapter.differ.submitList(it)
        })
    }

    fun editTaskItem()
    {
        todoAdapter.onClickEdit={
            NewTaskSheetFragment(it).show(supportFragmentManager,"newTaskTag")

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun completeTaskItem()
    {
        todoAdapter.onClickComplete={
            taskViewModel.setCompleted(it)

        }
    }



    /*private fun delete(){
        binding.delete.setOnClickListener {
            todoAdapter.taskItems.forEach(){
                todo = todoAdapter.differ.currentList[it.id]
                taskViewModel.delete(todo)
                showIconDelete(false)
            }

            //Snackbar.make(view,"Delete Successfully", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun showIconDelete(show:Boolean){
        binding.delete.isVisible = show
    }*/
}