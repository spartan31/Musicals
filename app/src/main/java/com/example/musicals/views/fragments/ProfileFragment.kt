package com.example.musicals.views.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.musicals.databinding.FragmentProfileBinding
import com.example.musicals.viewmodels.ProfileViewModel
import com.example.musicals.viewmodels.ProfileViewModelFactory
import com.example.musicals.views.activity.LoginScreen
import java.io.ByteArrayOutputStream


private const val REQUEST_CODE_GALLERY = 100
   private const val REQUEST_CODE_CAMERA = 101
class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var activityContext: Context
    lateinit var pref: SharedPreferences
    var flag = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        connection(inflater, container)
        clickManager()
        return binding.root
    }

    private fun clickManager() {

        binding.logoutButton.setOnClickListener {
            val editor = pref?.edit()
            editor?.putBoolean("flag", false)
            editor?.apply()
            val intent = Intent(activity, LoginScreen::class.java)
            activity?.finish()
            startActivity(intent)
        }


        binding.updateButton.setOnClickListener {
            if (flag == 0) {
                binding.editTextName.isEnabled = true
                binding.logoutButton.isVisible = false
                binding.updateButton.text = "Done"
                binding.passwordContainer.isVisible = true
                binding.editTextPassword.isEnabled = true;
                binding.updateMessage.isVisible = false
                binding.floatingActionButton.isVisible = true
                binding.floatingActionButton.isClickable = true
                flag = 1
            } else {
                viewModel.updateData()
                binding.updateMessage.isVisible = true
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        binding.updateMessage.isVisible = false
                    }, 2000
                )
                binding.userName.text = "Hello ${viewModel.userName.value}"
                binding.editTextName.isEnabled = false
                binding.logoutButton.isVisible = true
                binding.updateButton.text = "Update"
                binding.editTextPassword.isEnabled = false;
                binding.passwordContainer.isVisible = false
                binding.floatingActionButton.isVisible = false
                binding.floatingActionButton.isClickable = false
                flag = 0
            }
        }

        binding.floatingActionButton.setOnClickListener {

            val builder = AlertDialog.Builder(activityContext)
            builder.setMessage("Choose picture")
                .setPositiveButton("Gallery",
                    DialogInterface.OnClickListener { dialog, id ->
                        openGalleryForImage()
                    })
                .setNegativeButton("Camera",
                    DialogInterface.OnClickListener { dialog, id ->
                        openCameraForImage()
                    })
            builder.create().show()
        }
    }

    private fun openCameraForImage() {
        var takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, REQUEST_CODE_CAMERA)
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_CODE_CAMERA ->{
                    if (data != null) {
                        val bitmapImage = data.extras?.get("data") as Bitmap
                        binding.circleImageView.setImageBitmap(bitmapImage)
                        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, ByteArrayOutputStream())
                        val path = MediaStore.Images.Media.insertImage(context?.contentResolver, bitmapImage, "Image Title", "Description")
                        val uri = Uri.parse(path)
                        viewModel.userProfilePic.value = uri
                    }
                }
                REQUEST_CODE_GALLERY -> {
                    binding.circleImageView.setImageURI(data?.data)
                    viewModel.userProfilePic.value = data?.data
                }
            }

        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityContext = context
    }

    fun connection(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(
            this, ProfileViewModelFactory(activityContext)
        ).get(ProfileViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        pref = requireActivity().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)

        val userId = pref.getString("currentUserId", "Guest")
        val userInfo = viewModel.readLoginData(userId!!)
        viewModel.setImagefromDatabase(binding.circleImageView)

        binding.updateMessage.isVisible = false
        binding.userName.text = "Hello ${userInfo.user_Name.toString()}"

    }
}