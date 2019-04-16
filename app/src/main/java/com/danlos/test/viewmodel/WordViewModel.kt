package com.danlos.test.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.danlos.test.model.Word
import com.danlos.test.model.WordRepository
import com.danlos.test.model.WordRoomDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class WordViewModel(application: Application): AndroidViewModel(application) {
    private val repository: WordRepository
    val allWords: LiveData<List<Word>>
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    init {
        val wordDao = WordRoomDataBase.getDatabase(application, scope).wordDao()
        repository = WordRepository(wordDao)
        allWords = repository.allWords
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun insert(word: Word) = scope.launch(Dispatchers.IO) { repository.insert(word) }
}