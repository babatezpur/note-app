package com.babapp.noteapp.Room

import android.content.Context
import android.provider.ContactsContract.Data
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.babapp.noteapp.Model.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase :  RoomDatabase(){

    abstract fun getNoteDao() : NoteDAO

    //singleton structure

    companion object{

        @Volatile
        private var INSTANCE : NoteDatabase? = null

        fun getDatabase(context: Context, scope:CoroutineScope) : NoteDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                NoteDatabase::class.java, "note_database")
                    .addCallback(NoteDatabaseCallback(scope))
                    .build()

                INSTANCE = instance

                instance
            }

        }
    }

    private class NoteDatabaseCallback(private val scope : CoroutineScope) : RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

            INSTANCE?.let{ database->
                //database.getNoteDao().insert("t")   -- not allowed
                scope.launch {
                    val noteDao = database.getNoteDao()
                    noteDao.insert(Note("Groceries", "Potato, chocos, juice."))
                    noteDao.insert(Note("Lotus temple", "Lotus temple belongs to Baha'i faith."))
                }
            }
        }
    }

}