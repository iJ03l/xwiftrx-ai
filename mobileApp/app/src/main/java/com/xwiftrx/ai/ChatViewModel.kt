package com.xwiftrx.ai

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val repository = ChatRepository()

    val aiResponse: MutableLiveData<String?> = MutableLiveData() // AI response
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false) // Loading state

    fun sendMessageToAI(message: String) {
        viewModelScope.launch {
            isLoading.postValue(true) // Set loading to true
            val response = repository.sendMessageToAI(message)
            if (response != null) {
                aiResponse.postValue(response)
            } else {
                aiResponse.postValue("Error: Could not connect to Xwiftrx AI.")
            }
            isLoading.postValue(false) // Set loading to false
        }
    }
}
