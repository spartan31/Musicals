package com.example.musicals.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.musicals.databinding.FragmentSignUpWelcomeSplashBinding
import com.example.musicals.views.activity.LoginScreen

class SignUpWelcomeSplash : Fragment() {
    private lateinit var binding: FragmentSignUpWelcomeSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignUpWelcomeSplashBinding.inflate(inflater, container, false)

        onButtonClick()
        return binding.root
    }

    fun onButtonClick() {
        binding.homeButton.setOnClickListener {
            Intent(activity, LoginScreen::class.java).also {
                activity?.finish()
                startActivity(it)
            }
        }
    }
}