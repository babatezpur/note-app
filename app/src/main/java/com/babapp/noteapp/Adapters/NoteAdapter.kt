package com.babapp.noteapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.babapp.noteapp.Model.Note
import com.babapp.noteapp.R

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>(){

    var notes : List<Note> = ArrayList()

    class NoteViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewDescription : TextView = itemView.findViewById(R.id.textViewDescription)

        val cardView : CardView = itemView.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.note_item,parent, false)

        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        var currentNote : Note = notes[position]
        holder.textViewTitle.text =  currentNote.title
        holder.textViewDescription.text = currentNote.description
    }

    override fun getItemCount(): Int {
        return notes.size

    }

    fun setNote(myNotes : List<Note>){
        this.notes = myNotes
        notifyDataSetChanged()
    }

}