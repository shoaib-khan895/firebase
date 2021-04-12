package com.example.firebase.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.firebase.MainActivity
import com.example.firebase.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_splash.*

class SplashFragment : Fragment() {

    lateinit var firebaseAnalytics: FirebaseAnalytics
    private val USER_TYPE = "user_type"
    private val EXISTING_USER = "existing_user"
    private val NO_USER = "no_user"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Initialise Firebase analytics
        firebaseAnalytics = Firebase.analytics
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logScreenEvent()
        setListeners()
    }

    private fun logScreenEvent() {
        // custom event
        val eventName = "screen_opened"
        val bundle = Bundle().apply {
            putString("screen_name", SplashFragment::class.java.simpleName)
        }
        firebaseAnalytics.logEvent(eventName, bundle)
    }

    private fun setListeners() {
        splashNext_BTN.setOnClickListener {
            checkLogin()
        }
    }

    private fun checkLogin() {
        if (isUserLoggedIn()) {
            setUserLoggedInProperty()
            navigateToHomeDestination()
        } else {
            setUserLoggedInProperty()
            navigateToLoginDestination()
        }
    }

    private fun setUserLoggedInProperty() {
        if (isUserLoggedIn()) {
            setDefaultProperty(getUserName())
            firebaseAnalytics.setUserProperty(USER_TYPE, EXISTING_USER)
        }
        else {
            setDefaultProperty(null)
            firebaseAnalytics.setUserProperty(USER_TYPE, NO_USER)
        }
    }

    private fun setDefaultProperty(userName: String?) {
        val bundle = Bundle().apply {
            firebaseAnalytics.setUserProperty("user_id", userName)
        }
        firebaseAnalytics.setDefaultEventParameters(bundle)
    }

    private fun navigateToLoginDestination() {
        // When using Global action, we need to pass the resource ID
        findNavController().navigate(R.id.action_global_loginFragment)
    }

    private fun navigateToHomeDestination() {
        // Navigate to a destination
        val action =
            SplashFragmentDirections.actionSplashFragmentToHomeFragment()
        findNavController().navigate(action)
    }

    private fun getUserName(): String {
        val sharedPreferences =
            activity?.getSharedPreferences(MainActivity.MY_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPreferences?.getString(MainActivity.NAME_KEY, "")!!
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences =
            activity?.getSharedPreferences(MainActivity.MY_PREFERENCES, Context.MODE_PRIVATE)
        val name = sharedPreferences?.getString(MainActivity.NAME_KEY, "")
        return name != ""
    }
}