package com.example.abithshiksha.view.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.FragmentCoursesBinding
import com.example.abithshiksha.databinding.FragmentImagePreviewBinding
import com.example.abithshiksha.helper.click_listener.UploadDocListener
import java.io.InputStream

class ImagePreviewFragment(uploadDocListener: UploadDocListener) : DialogFragment() {
    private var _binding: FragmentImagePreviewBinding? = null
    private val binding get() = _binding!!

    private val uploadListener = uploadDocListener
    private var imagePath: String? = null
    private var imageuri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        imagePath = arguments?.getString("path")
        imageuri = arguments?.getString("uri")
        // Inflate the layout for this fragment
        _binding = FragmentImagePreviewBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uri = imageuri?.toUri()
        val imageStream: InputStream = uri?.let {
            requireActivity().getContentResolver().openInputStream(
                it
            )
        }!!
        val selectedImage: Bitmap = BitmapFactory.decodeStream(imageStream)
        binding.docPrevImageView.setImageBitmap(selectedImage)

        binding.backBtn.setOnClickListener {
            dismiss()
        }

        binding.docUploadBtn.setOnClickListener {
            imagePath?.let { path ->
                uploadListener?.uploadFile(path)
            }
            dismiss()
        }
    }
}