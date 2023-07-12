package com.swipetest.ui

import RequestPermission
import android.content.Intent
import android.os.Build
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.swipetest.Adapter.ProductListAdapter
import com.swipetest.R
import com.swipetest.Utils.Progresss
import com.swipetest.Utils.Resource
import com.swipetest.ViewModel.ProductListViewModel
import com.swipetest.api.response.Products
import com.swipetest.databinding.ActivityProductListBinding
import com.swipetest.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding

    private val viewModel: ProductListViewModel by viewModels()
    lateinit var productAdapter: ProductListAdapter
    var data: List<Products>  = listOf()

    var loaderFlag =  false

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        RequestPermission.requestMultiplePermissions(this)

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.listCategoryApi()
            binding.productSerach.setText("")
            binding.swipeRefresh.isRefreshing = false
        }



        binding.addproductClick.setOnClickListener {
            startActivity(Intent(this@ProductListActivity, AddProductActivity::class.java))
        }

        binding.productSerach.addTextChangedListener(productListWatcher)


        productListResponse()

    }

    private fun productListResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._listCategoryData.collect { response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            loaderFlag = true
                            data = emptyList()
                            data = response.data!!
                            setProductAdapter()

                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            loaderFlag = false
                            response.message?.let { message ->
                                androidExtension.alertBox(message, this@ProductListActivity)
                            }
                        }

                        is Resource.Loading -> {
                            if (!loaderFlag){
                                Progresss.start(this@ProductListActivity)
                            }
                        }

                        is Resource.Empty -> {
                        }

                    }

                }

            }
        }
    }


    private fun setProductAdapter() {
        binding.ProductListRecycler.layoutManager = GridLayoutManager(this,2)
        productAdapter = ProductListAdapter(this, data)
        binding.ProductListRecycler.adapter = productAdapter
    }


    private val productListWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            filterData(s.toString())

        }
    }



    private fun filterData(searchText: String) {
        val filteredList = data.filter { item ->
            try {
                item.product_name.contains(searchText, ignoreCase = true) ||
                        item.product_type.contains(searchText, ignoreCase = true)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
        productAdapter.filterList(filteredList)
    }


    override fun onStart() {
        super.onStart()
        viewModel.listCategoryApi()
    }


}