package com.example.anywhere.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anywhere.R
import com.example.anywhere.adapter.MyItemRecyclerViewAdapter
import com.example.anywhere.api.model.RelatedTopicModel
import com.example.anywhere.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_content_list.*

/**
 * A fragment representing a list of Items.
 */
class ListFragment : Fragment(), MyItemRecyclerViewAdapter.OnItemClickListener, SearchView.OnQueryTextListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MyItemRecyclerViewAdapter
    private lateinit var toolbar: Toolbar
    private lateinit var responseList: List<RelatedTopicModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_content_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        toolbar = view.findViewById(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.arrow_back_24)
        toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
        toolbar.title = "Fragment 1"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        viewModel.myResponse.observe(viewLifecycleOwner, Observer { it ->
            responseList = it.RelatedTopics
            val charactersList = mutableListOf<String>()
            it.RelatedTopics.forEach { model ->
                charactersList.add(model.Result.split(">", "<").map { it.trim() }[2])
            }
            setUpAdapter(charactersList)
            initializeSearchView(charactersList)
            responseList.forEachIndexed { index, model ->
                model.characterName = charactersList[index]
            }
            viewModel.setHeading(it.Heading)
            if (it.Heading.isNotEmpty()) {
                toolbar.title = it.Heading
                viewModel.setHeading(it.Heading)
            }
        })
        viewModel.getContentsData()
    }

    private fun initializeSearchView(itemsList: List<String>) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (itemsList.contains(query)) {
                    adapter.filter.filter(query)
                } else {
                    Toast.makeText(requireContext(), "No character found..", Toast.LENGTH_SHORT).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (this@ListFragment::adapter.isInitialized) {
                    adapter.filter.filter(newText)
                }
                return false
            }
        })
    }

    private fun setUpAdapter(itemsList: List<String>) {
        adapter = MyItemRecyclerViewAdapter(
            requireContext(),
            this
        )
        adapter.setUpData(itemsList)
        rvContent.layoutManager = LinearLayoutManager(requireContext())
        rvContent.setHasFixedSize(true)
        rvContent.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        rvContent.adapter = adapter
    }

    override fun onItemClicked(name: String) {
        viewModel.setSelectedContent(responseList.filter { it.characterName == name }[0])
        val contentFragment = requireActivity().supportFragmentManager.findFragmentByTag(ContentFragment::class.simpleName)

        if (contentFragment == null) {
            val layout = if (viewModel.isTablet.value == true) {
                R.id.frameLayout_content
            } else R.id.frameLayout_list
            requireActivity().supportFragmentManager.beginTransaction()
                .add(layout, ContentFragment(), ContentFragment::class.simpleName)
                .addToBackStack(ContentFragment::class.simpleName)
                .commit()
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        adapter.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter.filter(newText)
        return false
    }
}