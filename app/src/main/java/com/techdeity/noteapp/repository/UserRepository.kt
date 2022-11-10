package com.techdeity.noteapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.techdeity.noteapp.api.UserAPI
import com.techdeity.noteapp.models.UserRequest
import com.techdeity.noteapp.models.UserResponse
import com.techdeity.noteapp.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {

    //this is mutable live data
    //we can cange data in mutable one
    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()

    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData

    suspend fun registerUser(userRequest: UserRequest) {


        _userResponseLiveData.postValue(NetworkResult.Loading())

        val response = userAPI.signup(userRequest)
        handleResponse(response)

    }

    suspend fun loginUser(userRequest: UserRequest) {

        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.signin(userRequest)
        handleResponse(response)
    }

    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {

            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))

        } else if (response.errorBody() != null) {

            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

            _userResponseLiveData.postValue(NetworkResult.Error((errorObj.getString("message"))))
        } else {

            _userResponseLiveData.postValue(NetworkResult.Error("Something Horrible Went Wrong"))

        }
    }


}