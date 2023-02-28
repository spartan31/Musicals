package com.example.musicals.views.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.musicals.R
import com.example.musicals.databinding.ActivityLoginScreenBinding
import com.example.musicals.viewmodels.LoginViewModel
import com.example.musicals.viewmodels.LoginViewModelFactory
import com.example.musicals.views.fragments.SignUpFragment

class LoginScreen : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityLoginScreenBinding
    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_screen)
        viewModel = ViewModelProvider(this, LoginViewModelFactory(this))[LoginViewModel::class.java]
        binding.logInModel = viewModel
        binding.lifecycleOwner = this

        try {
            onButtonClick()

        } catch (e: Exception) {

        }
    }


    override fun onClick(p: View?) {
        when (p) {
            binding.createAccountButton -> {
                binding.userId.error = null
                binding.userPassword.error = null
                val signupFragment = supportFragmentManager.beginTransaction()
                signupFragment.replace(R.id.loginScreenView, SignUpFragment())
                signupFragment.addToBackStack("Fragment SignUp")
                signupFragment.commit()
            }

            binding.loginButton ->{
                val result = viewModel.isValidLoginData(binding.userId.text.toString() , binding.userPassword.text.toString())
                Log.i("Login status"," result ${result}")
                if(result == -1){
                    val pref = getSharedPreferences("login", MODE_PRIVATE)
                    val editor = pref.edit()
                    editor.putBoolean("flag",true)
                    editor.apply()
                    val intent = Intent(this, HomeScreen::class.java)
                    finish()
                    startActivity(intent)
                }else{
                   binding.userId.setError("Wrong Id or Password")
                   binding.userPassword.setError("Wrong Id or Password")
                }
            }
        }
    }

    private fun onButtonClick() {
        binding.createAccountButton.setOnClickListener(this)
        binding.loginButton.setOnClickListener(this)
    }


}