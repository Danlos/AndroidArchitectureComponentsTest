package com.danlos.test.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Word::class), version = 1)
abstract class WordRoomDataBase: RoomDatabase() {
    abstract fun wordDao():WordDao
    companion object {
        @Volatile
        private var INSTANCE: WordRoomDataBase? = null

        fun getDatabase(context: Context,
                        scope: CoroutineScope):WordRoomDataBase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDataBase::class.java,
                    "Word_database"
                ).addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class WordDatabaseCallback(private val scope: CoroutineScope)
        : RoomDatabase.Callback(){
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let{database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.wordDao())
                }
            }
        }

        fun populateDatabase(wordDao: WordDao){
            wordDao.deleteAll()
            var word = Word("Hey!")
            wordDao.insert(word)
            word = Word("DAYUMN!")
            wordDao.insert(word)
        }
    }
}

