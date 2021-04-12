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
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Initialise Firebase analytics
        firebaseAnalytics = Firebase.analytics
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        logScreenEvent()
    }

    private fun logScreenEvent() {
        // custom event
        val eventName = "screen_opened"
        val bundle = Bundle().apply {
            putString("screen_name", LoginFragment::class.java.simpleName)
        }
        firebaseAnalytics.logEvent(eventName, bundle)
    }

    private fun setListeners() {
        login_BTN.setOnClickListener {
            if (isNameNotEmpty(name_ET.text.toString())) {
                val name = name_ET.text.toString()
                saveNameInSharedPref(name)
                navigateToHomeDestination()
            }
            else {
                name_ET.error = getString(R.string.name_must_enter_info)
            }
        }
    }

    private fun saveNameInSharedPref(name: String) {
        val sharedPreferences = activity?.getSharedPreferences(MainActivity.MY_PREFERENCES, Context.MODE_PRIVATE)
        val sharedPreferencesEditor = sharedPreferences?.edit()
        sharedPreferencesEditor?.putString(MainActivity.NAME_KEY, name)
        sharedPreferencesEditor?.apply()
    }

    private fun navigateToHomeDestination() {
        // Navigate to a destination
        val action =
            LoginFragmentDirections.actionLoginFragmentToHomeFragment()
        findNavController().navigate(action)
    }

    private fun isNameNotEmpty(name: String): Boolean {
        return name != ""
    }
}