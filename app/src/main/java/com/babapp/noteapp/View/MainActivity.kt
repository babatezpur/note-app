package com.babapp.noteapp.View

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchUIUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.babapp.noteapp.Adapters.NoteAdapter
import com.babapp.noteapp.Model.Note
import com.babapp.noteapp.NoteApplication
import com.babapp.noteapp.R
import com.babapp.noteapp.ViewModel.NoteViewModel
import com.babapp.noteapp.ViewModel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var noteViewModel: NoteViewModel

    lateinit var addActivityResultLauncher: ActivityResultLauncher<Intent>
    lateinit var updateActivityResultLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView : RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val noteAdapter = NoteAdapter(this)
        recyclerView.adapter = noteAdapter

        registerActivityResultLauncher()

        val viewModelFactory = NoteViewModelFactory((application as NoteApplication).repository)

        noteViewModel = ViewModelProvider(this, viewModelFactory).get(NoteViewModel::class.java)

        noteViewModel.myAllNotes.observe(this, Observer {notes->

            noteAdapter.setNote(notes)

        })


        //For when notes are swiped, got two callback functions
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0
        , ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                //Does Nothing
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel.delete(noteAdapter.getNote(viewHolder.adapterPosition))    //posn of a note

            }

        }).attachToRecyclerView(recyclerView)
    }

    fun registerActivityResultLauncher(){
            addActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
            , ActivityResultCallback {  resultAddNode ->
                    val resultCode = resultAddNode.resultCode
                    val data = resultAddNode.data

                    if(resultCode == RESULT_OK && data != null){
                        val noteTitle : String = data.getStringExtra("title").toString()
                        val noteDescription : String = data.getStringExtra("description").toString()

                        val note = Note(noteTitle,noteDescription)
                        noteViewModel.insert(note)
                }

                })
        updateActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
            , ActivityResultCallback {  resultUpdateNode ->
                val resultCode = resultUpdateNode.resultCode
                val data = resultUpdateNode.data

                if(resultCode == RESULT_OK && data != null){
                    val updatedTitle : String = data.getStringExtra("updatedTitle").toString()
                    val updatedDescription : String = data.getStringExtra("updatedDescription").toString()
                    val noteId = data.getIntExtra("noteId", -1)

                    val newNote = Note(updatedTitle,updatedDescription)
                    newNote.id = noteId
                    noteViewModel.update(newNote)
                }

            })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.new_menu, menu)
        return true
    }

    //For when something on the menu bar is selected (clicked)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when(item.itemId){
                R.id.item_add_note ->{
                    val intent = Intent(this, NoteAddActivity::class.java)
                    addActivityResultLauncher.launch(intent)
                }
                R.id.item_delete_all_notes ->{
                    showDialogMessage()
                }

            }
        return true
    }

    fun showDialogMessage(){
        val dialogMessage = AlertDialog.Builder(this)
        dialogMessage.setTitle("Delete all nodes")
        dialogMessage.setMessage("If want to delete all msgs, click yes")
        dialogMessage.setNegativeButton("No", DialogInterface.OnClickListener{dialog, which->
            dialog.cancel()
        })
        dialogMessage.setPositiveButton("Yes", DialogInterface.OnClickListener{dialog, which ->
            noteViewModel.deleteAllNodes()
        })
        dialogMessage.create().show()
    }

}