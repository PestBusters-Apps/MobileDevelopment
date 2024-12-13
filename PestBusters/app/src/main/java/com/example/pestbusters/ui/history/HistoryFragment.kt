package com.example.pestbusters.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pestbusters.data.AppDatabase
import com.example.pestbusters.data.HistoryAdapter
import com.example.pestbusters.databinding.FragmentHistoryBinding
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewHistory.layoutManager = LinearLayoutManager(requireContext())
        try {
            val database = AppDatabase.getDatabase(requireContext())
            val historyDao = database.historyDao()

            lifecycleScope.launch {
                try {
                    historyDao.getAllHistory().observe(viewLifecycleOwner) { historyList ->
                        if (historyList != null && historyList.isNotEmpty()) {
                            Log.d("HistoryFragment", "History list size: ${historyList.size}")
                            val adapter = HistoryAdapter(historyList)
                            binding.recyclerViewHistory.adapter = adapter
//                            binding.tvEmptyState.visibility = View.GONE
                            binding.recyclerViewHistory.visibility = View.VISIBLE
                        } else {
                            Log.d("HistoryFragment", "History is empty.")
//                            binding.tvEmptyState.visibility = View.VISIBLE
                            binding.recyclerViewHistory.visibility = View.GONE
                        }
                    }
                } catch (e: Exception) {
                    Log.e("HistoryFragment", "Error fetching data: ${e.message}")
                }
            }
        } catch (e: Exception) {
            Log.e("HistoryFragment", "Error initializing database: ${e.message}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
