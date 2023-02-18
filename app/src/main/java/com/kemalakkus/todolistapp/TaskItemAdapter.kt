package com.kemalakkus.todolistapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.os.Build
import android.os.Build.VERSION_CODES
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kemalakkus.todolistapp.databinding.TaskItemRowBinding
import java.time.format.DateTimeFormatter


class TaskItemAdapter: RecyclerView.Adapter<TaskItemAdapter.TaskItemViewHolder>() {

    class TaskItemViewHolder(val binding: TaskItemRowBinding, val context: Context):RecyclerView.ViewHolder(binding.root)

    @RequiresApi(VERSION_CODES.O)
    val timeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")


    private val differCallBack = object : DiffUtil.ItemCallback<TaskItem>() {

        override fun areItemsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean {
            return oldItem.id == newItem.id

        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean {
            return oldItem == newItem

        }

    }
    val differ = AsyncListDiffer(this, differCallBack)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder {

        val view = TaskItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return TaskItemViewHolder(view, parent.context)
    }

    @RequiresApi(VERSION_CODES.O)
    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {

        //holder.bindTaskItem(taskItems[position])
        val todoList = differ.currentList[position]!!


            holder.binding.name.text = todoList.name

            if (todoList.isCompleted()){
                holder.binding.name.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                holder.binding.dueTime.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }

        holder.binding.completeButton.setImageResource(todoList.imageResource())
        holder.binding.completeButton.setColorFilter(todoList.imageColor(holder.context))


        holder.binding.completeButton.setOnClickListener{
                onClickComplete?.let { complete ->
                    complete.invoke(todoList)
                }
            }

        holder.binding.taskCellContainer.setOnClickListener{
                onClickEdit?.let { edit ->
                    edit.invoke(todoList)
                }
            }

            if (todoList.dueTime() != null){
                holder.binding.dueTime.text = timeFormat.format(todoList.dueTime())
            }else{
                holder.binding.dueTime.text = ""
            }
    }


    var onClickComplete:((TaskItem)-> Unit)? = null
    var onClickEdit:((TaskItem)-> Unit)? = null

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}