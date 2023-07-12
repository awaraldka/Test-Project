package com.swipetest.ui

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.swipetest.Adapter.ProductTypeOpenDialog
import com.swipetest.R
import com.swipetest.Utils.DialogUtils
import com.swipetest.Utils.ImageRotation
import com.swipetest.Utils.Progresss
import com.swipetest.Utils.Resource
import com.swipetest.ViewModel.ProductListViewModel
import com.swipetest.databinding.ActivityAddProductBinding
import com.swipetest.extension.androidExtension
import com.swipetest.interfaces.FinishActivity
import com.swipetest.interfaces.PopupItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream


@AndroidEntryPoint
class AddProductActivity : AppCompatActivity(), PopupItemClickListener,FinishActivity {

    private lateinit var binding: ActivityAddProductBinding


    lateinit var scaledBitmap : Bitmap
    lateinit var image: Uri


    private lateinit var dialog: Dialog
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: ProductTypeOpenDialog

    var productType : List<String> = listOf()


    private val viewModel: ProductListViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade


        RequestPermission.requestMultiplePermissions(this)


        binding.backButton.setOnClickListener {
            finish()
        }

        productType = productType + listOf(
            "Mobiles", "Food", "Cloths", "Electronics", "Books",
            "Furniture", "Beauty", "Toys", "Sports", "Jewelry",
            "Appliances", "Home Decor", "Health", "Automotive", "Music",
            "Movies", "Gaming", "Tools", "Pet Supplies", "Office Supplies"
        )



        binding.llProductType.setOnClickListener {
            openPopUp()
        }


        binding.addCertificate.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
               Toast.makeText(this,"Please Allow Permissions",Toast.LENGTH_SHORT)
            } else {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                pickImagesGallery.launch(intent)

            }
        }


        binding.certificateCross.setOnClickListener{
            binding.addCertificate.isVisible = true
            binding.certificate.isVisible = false

        }

        productListResponse()


        binding.addProduct.setOnClickListener {

            val name  =  binding.etProductName.text.toString()
            val type  =  binding.etProductType.text.toString()
            val tax  =  binding.etTax.text.toString()
            val price  = binding.etPrice.text.toString()

            val scaledImageFile = createImageFile(scaledBitmap)


            val mimeType =  ImageRotation.getMimeType(scaledImageFile.toString())


            val surveyBody = scaledImageFile.asRequestBody(mimeType?.toMediaTypeOrNull())
            val  requestMultiImagesAndVideos = MultipartBody.Part.createFormData("files", scaledImageFile.name, surveyBody)


            viewModel.addProductApi(name.toRequestBody(),type.toRequestBody(),price.toRequestBody(),tax.toRequestBody(),requestMultiImagesAndVideos)


        }




    }











    private val pickImagesGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                image = result.data?.data!!
                resizeImage(image)
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
    }



    private fun resizeImage(selectedImageUri: Uri?) {
        val originalBitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(selectedImageUri!!))

        val targetWidth = 500 // Desired width for the 1:1 ratio
        val targetHeight = 500 // Desired height for the 1:1 ratio

        val scaleFactor =
            (originalBitmap.width.toFloat() / targetWidth).coerceAtMost(originalBitmap.height.toFloat() / targetHeight)
        val scaledWidth = (originalBitmap.width / scaleFactor).toInt()
        val scaledHeight = (originalBitmap.height / scaleFactor).toInt()

        scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, scaledWidth, scaledHeight, true)

        // If you want to display the resized image in an ImageView

        binding.progressBar.isVisible = false
        binding.add.isVisible = true
        binding.addCertificate.isVisible = false
        binding.certificate.isVisible = true

        binding.certificateImage.setImageBitmap(scaledBitmap)






    }
    private fun createImageFile(bitmap: Bitmap): File {
        val cachePath = File(applicationContext.cacheDir, "temp_images")
        cachePath.mkdirs()

        val file = File(cachePath, "temp_image.jpg")
        file.createNewFile()

        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        return file
    }


    private fun productListResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._addProductData.collect { response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            androidExtension.alertBoxFinish(response.data!!.message, this@AddProductActivity,this@AddProductActivity)

                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            response.message?.let { message ->
                                androidExtension.alertBox(message, this@AddProductActivity)
                            }
                        }

                        is Resource.Loading -> {
                            Progresss.start(this@AddProductActivity)
                        }

                        is Resource.Empty -> {
                        }

                    }

                }

            }
        }
    }


    fun openPopUp() {

        try {
            val binding = LayoutInflater.from(this).inflate(R.layout.pop_lists, null)
            dialog = DialogUtils().createDialog(this, binding.rootView, 0)!!
            recyclerView = binding.findViewById(R.id.popup_recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)


            val dialogTitle = binding.findViewById<TextView>(R.id.popupTitle)
            val dialogBackButton = binding.findViewById<ImageView>(R.id.BackButton)
            dialogBackButton.setOnClickListener { dialog.dismiss() }
            dialogTitle.text = "Product Type"

            val searchEditText = binding.findViewById<EditText>(R.id.search_bar_edittext_popuplist)


            setProductTypeAdapter()


            searchEditText.addTextChangedListener(productTypeWatcher)



            dialog.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }



    private fun setProductTypeAdapter() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProductTypeOpenDialog(this, productType, this)
        recyclerView.adapter = adapter
    }

    override fun getProductTypeData(name: String) {
        binding.etProductType.text=  name
        dialog.dismiss()
    }


    private val productTypeWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            filterData(s.toString())

        }
    }



    private fun filterData(searchText: String) {
        val filteredList = productType.filter { item ->
            try {
                item.contains(searchText, ignoreCase = true)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
        adapter.filterList(filteredList)
    }


    override fun finishActivity() {
        finish()
    }


}