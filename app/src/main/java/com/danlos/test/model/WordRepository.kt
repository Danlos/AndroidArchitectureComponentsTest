package com.danlos.test.model

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class WordRepository(private val wordDao: WordDao) {
    val allWords: LiveData<List<Word>> = wordDao.getAllWords()

    @WorkerThread
    fun insert(word: Word){
        wordDao.insert(word)
    }
}