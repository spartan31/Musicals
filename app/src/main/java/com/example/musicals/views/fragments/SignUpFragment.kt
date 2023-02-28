package com.example.musicals.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.musicals.R
import com.example.musicals.databinding.FragmentSignUpBinding
import com.example.musicals.models.roomdatabase.User
import com.example.musicals.viewmodels.SignUpViewFactory
import com.example.musicals.viewmodels.SignUpViewModel

class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignUpBinding
    lateinit var viewModel: SignUpViewModel
    lateinit var contex: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, SignUpViewFactory(contex))[SignUpViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        signupSplashLauncher()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contex = context
    }

    private fun signupSplashLauncher() {
        binding.signupButton.setOnClickListener {
            val isValid = isValidSignup()
            if (isValid == 0) {
                binding.name.error = null
                binding.userId.error = null
                binding.userPassword.error = null
                binding.confirmPassword.error = null

                val isUniqueUserId = viewModel.insertUser(
                    User(
                        binding.userId.text.toString(),
                        binding.name.text.toString(),
                        binding.userPassword.text.toString(),
                        "android.resource://${context?.packageName}/${R.drawable.default_user_image}"
                    )
                )
                if (isUniqueUserId) {
                    val signupSplash = activity?.supportFragmentManager?.beginTransaction()
                    signupSplash?.replace(R.id.signupSplashContainer, SignUpWelcomeSplash())
                    signupSplash?.commit()
                } else {
                    binding.userId.error = "User Id Must Be Unique"
                }

            } else {
                when (isValid) {
                    1 -> {
                        binding.name.error = "Name Required"
                    }

                    2 -> {
                        binding.userId.error = "UserId Required"
                    }
                    3 -> {
                        binding.userPassword.error = "Password Required"
                    }
                    4 -> {
                        binding.confirmPassword.error = "Password Not Matched"
                    }
                }
            }
        }
    }


    private fun isValidSignup(): Int {
        return if (binding.name.text.isEmpty()) {
            1
        } else if (binding.userId.text.isEmpty()) {
            2
        } else if (binding.userPassword.text.isEmpty()) {
            3
        } else if (binding.userPassword.text.toString() != binding.confirmPassword.text.toString()) {
            4
        } else {
            0
        }
    }

}