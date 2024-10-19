package com.aarafrao.test.viewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aarafrao.test.model.ItemModelItem
import com.aarafrao.test.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListViewModel(private val repository: Repository) : ViewModel() {
    private val _products = MutableStateFlow<List<ItemModelItem>>(emptyList())
    val products: StateFlow<List<ItemModelItem>> = _products

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            val productList = repository.getProducts()
            Log.d(TAG, "fetchProducts: $productList")
            _products.value = productList
        }
    }
}