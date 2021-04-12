package com.example.firebase.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.firebase.MainActivity
import com.example.firebase.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_account.*
import java.lang.Exception

class AccountFragment : Fragment() {

    lateinit var firebaseAnalytics: FirebaseAnalytics
    private val TAG = AccountFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logScreenEvent()
        checkLogin()
        setListeners()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Initialise Firebase analytics
        firebaseAnalytics = Firebase.analytics
    }

    private fun logScreenEvent() {
        // custom event
        val eventName = "screen_opened"
        val bundle = Bundle().apply {
            putString("screen_name", AccountFragment::class.java.simpleName)
        }
        firebaseAnalytics.logEvent(eventName, bundle)
    }

    /**
     * Forcefully creating exception for logging Crashlytics Non-fatals exception
     */
    private fun checkLogin() {
        try {
            val name = getSharedPref() ?: throw NullPointerException("No user logged in")
            "Welcome $name".also { name_TV.text = it }
        }
        catch (e: Exception) {
            Toast.makeText(activity, getString(R.string.name_must_enter_info), Toast.LENGTH_LONG).show()
            FirebaseCrashlytics.getInstance().recordException(e)
            Log.i(TAG, "Exception: $e")
            navigateToLoginDestination()
        }

        /*if (isUserLoggedIn()) {
            val name = getSharedPref()
            name_TV.text = "Welcome " + name.toString()
        }
        else {
            Toast.makeText(activity, getString(R.string.name_must_enter_info), Toast.LENGTH_LONG).show()
            FirebaseCrashlytics.getInstance().recordException(e)
            navigateToLoginDestination()
        }*/
    }

    private fun getSharedPref(): String? {
        val sharedPreferences = activity?.getSharedPreferences(MainActivity.MY_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPreferences?.getString(MainActivity.NAME_KEY, null)
    }

    private fun setListeners() {
        logout_BTN.setOnClickListener {
            removeSharedPrefData()
            navigateToLoginDestination()
        }
    }

    private fun navigateToLoginDestination() {
        findNavController().navigate(R.id.action_global_loginFragment)
    }

    private fun removeSharedPrefData() {
        val sharedPreferences = activity?.getSharedPreferences(MainActivity.MY_PREFERENCES, Context.MODE_PRIVATE)
        val sharedPreferencesEditor = sharedPreferences?.edit()
        sharedPreferencesEditor?.remove(MainActivity.NAME_KEY)
        sharedPreferencesEditor?.apply()
    }
}