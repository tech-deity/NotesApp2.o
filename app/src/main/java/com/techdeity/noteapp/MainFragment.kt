package com.techdeity.noteapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.techdeity.noteapp.databinding.FragmentMainBinding
import com.techdeity.noteapp.models.NoteResponse
import com.techdeity.noteapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : Fragment() {


    private  lateinit var adapter :NoteAdapter
    private var _binding :FragmentMainBinding ?= null
    private val binding get() = _binding!!
    private val noteViewModel by viewModels<NoteViewModel> ()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        _binding = FragmentMainBinding.inflate(inflater,container, false)
        adapter = NoteAdapter(::onNoteClicked)


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObserver()
        noteViewModel.getNote()
        binding.noteRv.layoutManager= StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.noteRv.adapter = adapter
        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_main_to_note)
        }
    }

    private fun bindObserver() {

        noteViewModel.notesLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    adapter.submitList(it.data)

                }
                is NetworkResult.Error -> {

                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
    }
private  fun onNoteClicked(noteResponse: NoteResponse){
    val bundle = Bundle()
    bundle.putString("note",Gson().toJson(noteResponse))
    findNavController().navigate(R.id.action_main_to_note,bundle)


}
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}