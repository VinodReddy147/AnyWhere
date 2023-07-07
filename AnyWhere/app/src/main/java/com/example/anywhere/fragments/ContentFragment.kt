package com.example.anywhere.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.anywhere.R
import com.example.anywhere.api.model.RelatedTopicModel
import com.example.anywhere.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_content.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL

class ContentFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        toolbar = view.findViewById(R.id.toolbar1)
        toolbar.setNavigationIcon(R.drawable.arrow_back_24)
        toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
        toolbar.title = "Fragment 2"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        viewModel.selectedContent.observe(viewLifecycleOwner) {
            setUpData(it)
        }
        viewModel.selectedContentTitle.observe(viewLifecycleOwner) {
            if(it?.isNotEmpty() == true) {
                toolbar.title = it
                contentTitle.text = it
            } else {
                contentTitle.text = "Title goes here"
            }
        }
    }

    private fun setUpData(relatedTopicModel: RelatedTopicModel) {
        contentText.text = relatedTopicModel.Text
        if (relatedTopicModel.Icon.URL.isNotEmpty()) {
            val url = relatedTopicModel.FirstURL + relatedTopicModel.Icon.URL
            CoroutineScope(Dispatchers.IO).launch {
                val input = URL(url).readBytes()
                Log.d("safas",input.decodeToString())
                val bitmap = BitmapFactory.decodeByteArray(input, 0 , input.size)
                withContext(Dispatchers.Main){
                    if (bitmap == null) {
                        imageView?.setImageResource(R.drawable.android_image)
                    } else {
                        imageView?.setImageBitmap(bitmap)
                    }
                }
            }
        } else {
            imageView?.setImageResource(R.drawable.android_image)
        }
    }
}