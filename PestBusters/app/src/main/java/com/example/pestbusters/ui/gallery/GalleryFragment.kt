package com.example.pestbusters.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pestbusters.PestAdapter
import com.example.pestbusters.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)

        setupRecyclerView()
        observeViewModel()

        galleryViewModel.fetchAllPests()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewPest.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        galleryViewModel.pests.observe(viewLifecycleOwner) { pests ->
            if (pests.isNotEmpty()) {
                binding.recyclerViewPest.adapter = PestAdapter(pests)
            } else {
                showToast("No pests found!")
            }
        }

        galleryViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            showToast(errorMessage)
            Log.e("GalleryFragment", errorMessage)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
