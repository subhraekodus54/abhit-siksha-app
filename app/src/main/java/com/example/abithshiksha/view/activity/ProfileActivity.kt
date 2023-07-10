package com.example.abithshiksha.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityProfileBinding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.helper.click_listener.UploadDocListener
import com.example.abithshiksha.model.network.ApiConstants
import com.example.abithshiksha.model.pojo.get_orders.Course
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.adapter.ProfileCoursesAdapter
import com.example.abithshiksha.view.fragment.ImagePreviewFragment
import com.example.abithshiksha.view_model.GetOrdersViewModel
import com.example.abithshiksha.view_model.GetProfileViewModel
import com.example.abithshiksha.view_model.UploadProfilePicViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.user.caregiver.*
import id.zelory.compressor.Compressor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class ProfileActivity : AppCompatActivity(), UploadDocListener {
    private lateinit var binding: ActivityProfileBinding
    private val mGetProfileViewModel: GetProfileViewModel by viewModel()
    private val mUploadProfilePicViewModel: UploadProfilePicViewModel by viewModel()
    private val mGetOrdersViewModel: GetOrdersViewModel by viewModel()

    private lateinit var accessToken: String
    private var name:String? = null
    private var address:String? = null
    private var academic:String? = null

    private var imageUri: Uri? = null
    private var mImeiId: String? = null
    private var grantedOtherPermissions: Boolean = false
    private val PICK_IMAGE = 100

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.white)

        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()
        binding.profileMainLay.gone()
        binding.profileShimmerView.startShimmer()

        binding.backArrow.setOnClickListener {
            finish()
        }

        binding.editBtn.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            intent.putExtra("name",name)
            intent.putExtra("address",address)
            intent.putExtra("academic",academic)
            startActivity(intent)
        }

        binding.addImgBtn.setOnClickListener {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    Log.e("click", " granted")
                    dispatchGalleryIntent()
                }else{
                    Log.e("click", "not granted 1")
                    requestPermission()
                }
            }else{
                Log.e("click", "not granted 2")
                requestPermission()
            }
        }
    }

    private fun fillCoursesRecyclerView(list: List<Course>) {
        val gridLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.coursesRecycler.apply {
            layoutManager = gridLayoutManager
            adapter = ProfileCoursesAdapter(list,this@ProfileActivity)
        }
    }

    override fun onResume() {
        super.onResume()
        if(isConnectedToInternet()){
            getProfile(accessToken)
            getOrders(accessToken)
        }else{
            Toast.makeText(this,"No internet connection.",Toast.LENGTH_SHORT).show()
        }
    }

    private fun dispatchGalleryIntent() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE)
    }

    fun getRealPathFromUri(contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = this.contentResolver.query(contentUri!!, proj, null, null, null)
            assert(cursor != null)
            val columnIndex: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(columnIndex)
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }

    private fun showImageDialog(absolutePath: String,uri: String) {
        val bundle = Bundle()
        bundle.putString("path", absolutePath)
        bundle.putString("uri",uri)
        val dialogFragment = ImagePreviewFragment(this)
        dialogFragment.arguments = bundle
        dialogFragment.show(this.supportFragmentManager, "signature")
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager())
            {
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.addCategory("android.intent.category.DEFAULT")
                    intent.data =
                        Uri.parse(String.format("package:%s", applicationContext.packageName))
                    startActivityForResult(intent, 2296)
                } catch (e: java.lang.Exception) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    startActivityForResult(intent, 2296)
                }
            }else{
                dispatchGalleryIntent()
            }
        } else {
            requestStoragePermission()
        }
    }

    private fun requestStoragePermission() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {

                @SuppressLint("MissingPermission")
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        // info("onPermissionsChecked: All permissions are granted!")
                        val telephonyManager =
                            getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                        mImeiId =
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                                try {
                                    telephonyManager.imei
                                } catch (e: SecurityException) {
                                    e.printStackTrace()
                                    "mxmxmxmxmxmxmxm"
                                }
                            } else {
                                "mxmxmxmxmxmxmxm"
                            }

                        grantedOtherPermissions = true
                    }

                    // check for permanent denial of any permission
                    /* if (report.isAnyPermissionPermanentlyDenied) {
                         // show alert dialog navigating to Settings
                         showSettingsDialog()
                     }*/
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .onSameThread()
            .check()
    }

    override fun uploadFile(path: String) {

        try {
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    val file = File(path)
                    val compressedImageFile = Compressor.compress(this@ProfileActivity, file)
                    val imagePart = createMultiPart("image", compressedImageFile)
                    if(isConnectedToInternet()){
                        uploadProfilePic(imagePart)
                        Log.d("img",imagePart.toString())
                    }else{
                        Toast.makeText(this@ProfileActivity,"No internet connection.", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun uploadProfilePic(
        imagePart: MultipartBody.Part
    ) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading...")
        progressDialog.setMessage("Your Picture is on process, please wait")
        progressDialog.show()

        mUploadProfilePicViewModel.uploadProfilePic(imagePart,accessToken).observe(this) { outcome ->
            progressDialog.dismiss()
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        Toast.makeText(this, outcome.data.result.message, Toast.LENGTH_SHORT).show()
                        val imgUrl = ApiConstants.PUBLIC_URL + outcome.data.result.path
                        Glide.with(this)
                            .load(imgUrl) // image url
                            .placeholder(R.color.color_grey) // any placeholder to load at start
                            .centerCrop()
                            .into(binding.userImg)

                    } else {
                        Toast.makeText(this, outcome.data.result.message, Toast.LENGTH_SHORT).show()
                    }
                }

                is Outcome.Failure<*> -> {
                    Toast.makeText(this, R.string.network_issue, Toast.LENGTH_SHORT).show()

                    Log.i("statusMsg", outcome.e.message.toString())

                    outcome.e.printStackTrace()
                    Log.i("status", outcome.e.cause.toString())
                }
            }
        }
    }

    private fun getProfile(
        token: String
    ){
        mGetProfileViewModel.getProfile(token).observe(this) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.profileMainLay.visible()
                        binding.profileShimmerView.stopShimmer()
                        binding.profileShimmerView.gone()
                        outcome.data.result.result.user_details.name?.let {
                            binding.userNameTv.text = it
                            name = it
                        }
                        outcome.data.result.result.user_details.email?.let {
                            binding.mailTv.text = it
                        }
                        outcome.data.result.result.user_details.phone?.let {
                            binding.phoneTv.text = it.toString()
                        }

                        if(outcome.data.result.result.user_details.address != null){
                            binding.addressTv.text = outcome.data.result.result.user_details.address
                            address = outcome.data.result.result.user_details.address

                        }else{
                            binding.addressTv.text = "Update your address"
                            address = null
                        }

                        if(outcome.data.result.result.user_details.education != null){
                            binding.academicTv.text = outcome.data.result.result.user_details.education.toString()
                            academic = outcome.data.result.result.user_details.education.toString()

                        }else{
                            binding.academicTv.text = "Update your education qualification"
                            academic = null
                        }

                        Glide.with(this)
                            .load(ApiConstants.PUBLIC_URL+outcome.data.result.result.user_details.image) // image url
                            .placeholder(R.color.color_grey) // any placeholder to load at start
                            .centerCrop()
                            .into(binding.userImg)
                    } else {
                        Toast.makeText(this, outcome.data.result.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Outcome.Failure<*> -> {
                    Toast.makeText(this, R.string.network_issue, Toast.LENGTH_SHORT).show()

                    Log.i("statusMsg", outcome.e.message.toString())

                    outcome.e.printStackTrace()
                    Log.i("status", outcome.e.cause.toString())
                }
            }
        }
    }

    private fun getOrders(
        token: String
    ){
        binding.courseShimmerView.visible()
        binding.courseShimmerView.startShimmer()
        binding.coursesRecycler.gone()
        binding.noDataLot.gone()
        binding.noDataTv.gone()
        mGetOrdersViewModel.getOrder(token).observe(this) { outcome ->
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        binding.courseShimmerView.gone()
                        binding.courseShimmerView.stopShimmer()
                        if(outcome.data.result.courses.size != 0 && outcome.data.result.courses != null){
                            binding.coursesRecycler.visible()
                            binding.noDataLot.gone()
                            binding.noDataTv.gone()
                            fillCoursesRecyclerView(outcome.data.result.courses)
                        }else{
                            binding.noDataLot.visible()
                            binding.coursesRecycler.gone()
                            binding.noDataTv.visible()
                        }
                    } else {
                        Toast.makeText(this, outcome.data.result.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Outcome.Failure<*> -> {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT)
                        .show()
                    Log.i("statusMsg", outcome.e.message.toString())

                    outcome.e.printStackTrace()
                    Log.i("status", outcome.e.cause.toString())
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == PICK_IMAGE) {
            try {
                imageUri = data?.data
                val path = getRealPathFromUri(imageUri)
                val imageFile = File(path!!)
                showImageDialog(imageFile.absolutePath,imageUri.toString())

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (requestCode == 2296) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                    dispatchGalleryIntent()
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

}