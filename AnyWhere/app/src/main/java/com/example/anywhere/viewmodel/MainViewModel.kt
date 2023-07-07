package com.example.anywhere.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anywhere.BuildConfig
import com.example.anywhere.api.RetrofitHelper
import com.example.anywhere.api.model.ContentsResponse
import com.example.anywhere.api.model.RelatedTopicModel
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    val myResponse: MutableLiveData<ContentsResponse> = MutableLiveData()
    var selectedContent: MutableLiveData<RelatedTopicModel> = MutableLiveData()
    var selectedContentTitle: MutableLiveData<String> = MutableLiveData()
    var isTablet: MutableLiveData<Boolean> = MutableLiveData()

    fun getContentsData() {
        viewModelScope.launch {
            myResponse.value = RetrofitHelper.retrofit.getContentsList(BuildConfig.END_POINT)
        }
    }

    fun setSelectedContent(content: RelatedTopicModel){
        selectedContent.value = content
    }

    fun setHeading(heading: String) {
        selectedContentTitle.value = heading
    }

    fun setIsTab(isTab: Boolean) {
        isTablet.value = isTab
    }

}