package com.example.pestbusters.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pestbusters.ChatBotActivity
import com.example.pestbusters.DetailActivity
import com.example.pestbusters.DetailActivityFyi
import com.example.pestbusters.Fyi
import com.example.pestbusters.ListFyiAdapter
import com.example.pestbusters.R
import com.example.pestbusters.TutorialActivity
import com.example.pestbusters.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var rvFyi: RecyclerView
    private val list = ArrayList<Fyi>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.chatbotCard.setOnClickListener {
            val intent = Intent(requireContext(), ChatBotActivity::class.java)
            startActivity(intent)
        }

        binding.tutorialCard.setOnClickListener {
            val intent = Intent(requireContext(), TutorialActivity::class.java)
            startActivity(intent)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFyi = binding.rvFyi
        rvFyi.setHasFixedSize(true)

        list.clear()
        list.addAll(getListFyi())
        showRecyclerList()
    }

    private fun getListFyi(): ArrayList<Fyi> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val listFyi = ArrayList<Fyi>()
        for (i in dataName.indices) {
            val fyi = Fyi(dataName[i], dataDescription[i], dataPhoto.getResourceId(i, -1))
            listFyi.add(fyi)
        }
        dataPhoto.recycle()
        return listFyi
    }

    private fun showRecyclerList() {
        rvFyi.layoutManager = LinearLayoutManager(context)
        val listFyiAdapter = ListFyiAdapter(list)
        rvFyi.adapter = listFyiAdapter
        listFyiAdapter.setOnItemClickCallback(object : ListFyiAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Fyi) {
                showSelectedPlace(data)
            }
        })
    }

    private fun showSelectedPlace(place: Fyi) {
        val intent = Intent(context, DetailActivityFyi::class.java)
        intent.putExtra(DetailActivityFyi.EXTRA_NAME, place.name)
        intent.putExtra(DetailActivityFyi.EXTRA_DESCRIPTION, place.description)
        intent.putExtra(DetailActivityFyi.EXTRA_PHOTO, place.photo)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}