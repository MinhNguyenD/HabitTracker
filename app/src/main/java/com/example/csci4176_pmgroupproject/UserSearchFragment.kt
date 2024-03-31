package com.example.csci4176_pmgroupproject

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class UserSearchViewModel(private val userRepository: UserFriendRepository) : ViewModel() {

    // LiveData to hold the search results
    val searchResults = MutableLiveData<List<User>>()

    fun searchUser(query: String) {
        userRepository.getUserByUsername(query) { user ->
            user?.let { searchResults.value = listOf(it) }
        }
    }
    fun addFriend(friendUid: String) {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        currentUserUid?.let {
            userRepository.addFriend(it, friendUid)
            // Update UI accordingly
        }
    }

    fun removeFriend(friendUid: String) {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        currentUserUid?.let {
            userRepository.removeFriend(it, friendUid)
            // Update UI accordingly
        }
    }

}
