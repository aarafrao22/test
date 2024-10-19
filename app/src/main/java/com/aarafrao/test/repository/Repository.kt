package com.aarafrao.test.repository

import com.aarafrao.test.model.ItemModelItem
import com.aarafrao.test.network.ApiService

class Repository(private val apiService: ApiService) {

    suspend fun getProducts(): List<ItemModelItem> {
        return apiService.getProducts()
    }
}