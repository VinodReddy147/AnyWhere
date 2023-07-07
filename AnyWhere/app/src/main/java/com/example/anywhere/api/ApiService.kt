package com.example.anywhere.api

import com.example.anywhere.api.model.ContentsResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET
    suspend fun getContentsList(@Url path: String): ContentsResponse
}