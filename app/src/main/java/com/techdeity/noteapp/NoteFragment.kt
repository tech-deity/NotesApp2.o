package com.techdeity.noteapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.techdeity.noteapp.databinding.FragmentNoteBinding
import com.techdeity.noteapp.models.NoteRequest
import com.techdeity.noteapp.models.NoteResponse
import com.techdeity.noteapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var _binding :FragmentNoteBinding ?= null
    private val binding get() = _binding!!
    private val noteViewModel by viewModels<NoteViewModel>()
    private var note:NoteResponse? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        _binding = FragmentNoteBinding.inflate(inflater,container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialData()
        bindHandlers()
        bindObservers()

    }

    private fun bindHandlers() {

        binding.btnDelete.setOnClickListener {
            note?.let {
                noteViewModel.deleteNote(it!!._id)
            }
        }
        binding.btnSubmit.setOnClickListener {
            val title = binding.txtTitle.text.toString()
            val description = binding.txtDescription.text.toString()
            val noteRequest = NoteRequest(description,title)
            if(note == null)
            {
                noteViewModel.createNote(noteRequest)
            }
            else{
                noteViewModel.updateNote(note!!._id,noteRequest)
            }
        }
    }

    private fun bindObservers() {
        noteViewModel.statusLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    findNavController().popBackStack()
                }
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {}
            }
        }
    }

    private fun setInitialData() {

        val jsonNote = arguments?.getString("note")
        if(jsonNote != null){
            note = Gson().fromJson(jsonNote, NoteResponse::class.java)
            note?.let{
                binding.txtTitle.setText(it.title)
                binding.txtDescription.setText(it.description)
            }
        }
        else{
            binding.addEditText.text = "Add Note "
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}