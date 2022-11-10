package com.techdeity.noteapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.techdeity.noteapp.databinding.FragmentRegisterBinding
import com.techdeity.noteapp.models.UserRequest
import com.techdeity.noteapp.utils.NetworkResult
import com.techdeity.noteapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding:FragmentRegisterBinding?=null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel> ()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)

        if(tokenManager.getToken()!= null){
            findNavController().navigate(R.id.action_register_to_main)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signup.setOnClickListener {
           val validationResult = validateUserInput()
            if(validationResult.first){
                authViewModel.registerUser(getUserRequest())


            }else{

                binding.txtError.text = validationResult.second
            }

        }

        binding.btnLogin.setOnClickListener {
             findNavController().navigate(R.id.action_register_to_login)


        }

        bindObservers()

         }

    private fun getUserRequest():UserRequest{
        val emailAddress = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        val username = binding.txtUsername.text.toString()

        return UserRequest(emailAddress,password,username)

    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return authViewModel.validateCredentials(userRequest.username,userRequest.email,userRequest.password,false)
    }
    private fun bindObservers() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success ->{

                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_register_to_main)

                }
                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}