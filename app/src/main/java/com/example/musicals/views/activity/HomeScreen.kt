package com.example.musicals.views.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.musicals.R
import com.example.musicals.databinding.ActivityHomeScreenBinding
import com.example.musicals.views.adapter.HomeFragmentAdapter
import com.example.musicals.views.fragments.DownloaderFragment
import com.example.musicals.views.fragments.OnlinePlayerFragment
import com.example.musicals.views.fragments.ProfileFragment


class HomeScreen : AppCompatActivity() {
    private val READ_STORAGE_PERMISSION_CODE = 101
    private val READ_STORAGE_PERMISSION_FINAL = 103
    lateinit var binding: ActivityHomeScreenBinding
    lateinit var adapter: HomeFragmentAdapter
    private val requiredPermissionsArray = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen)

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkingPermissions(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    READ_STORAGE_PERMISSION_CODE
                )
            }
            fragmentConnector()

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }


    override fun onBackPressed() = if (binding.pager.currentItem == 0) {
        super.onBackPressed()
    } else {
        binding.pager.currentItem = 0
    }

    private fun fragmentConnector() {
        adapter = HomeFragmentAdapter(supportFragmentManager).apply {
            this.addFragment(OnlinePlayerFragment(), "Home")
            this.addFragment(DownloaderFragment(), "Downloads")
            this.addFragment(ProfileFragment(), "Profile")
        }

        binding.pager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.pager)
    }


    private fun checkingPermissions(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            requiredPermissionsArray.add(permission)
            ActivityCompat.requestPermissions(
                this,
                requiredPermissionsArray.toTypedArray(),
                READ_STORAGE_PERMISSION_FINAL
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thank You ", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Need Permission to continue", Toast.LENGTH_SHORT).show()
                requiredPermissionsArray.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(
                    this,
                    requiredPermissionsArray.toTypedArray(),
                    READ_STORAGE_PERMISSION_FINAL
                )
            }
        }
    }

    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
    }
}