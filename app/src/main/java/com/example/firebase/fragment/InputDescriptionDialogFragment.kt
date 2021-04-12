package com.example.firebase.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.firebase.MainActivity
import com.example.firebase.R

class InputDescriptionDialogFragment : DialogFragment() {

    // Get argument using Safe args
    val args: InputDescriptionDialogFragmentArgs by navArgs()
    lateinit var navController: NavController
    private val TAG = InputDescriptionDialogFragment::class.java.simpleName
    lateinit var descriptionEditText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        navController = findNavController()
        // Get data using Safe args
        val title = args.dialogFragmentTitle

        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_description, null)
        descriptionEditText = dialogLayout.findViewById(R.id.description_ET)

        return builder
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            .setView(dialogLayout)
            .setTitle(title)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.submit_btn_text)) { dialog, which ->
                //Log.i(TAG, "Description is ${descriptionEditText.text}")
                getDescriptionFromDialog()
            }
            .create()
    }

    private fun getDescriptionFromDialog() {
        if (!isDescriptionEmpty()) {
            Log.i(TAG, "Description is ${descriptionEditText.text}")
            navController.previousBackStackEntry?.savedStateHandle?.set(
                MainActivity.DESCRIPTION_KEY,
                descriptionEditText.text.toString()
            )

        } else {
            navController.previousBackStackEntry?.savedStateHandle?.set(
                MainActivity.DESCRIPTION_KEY,
                getString(R.string.description_not_entered_info)
            )
            Toast.makeText(
                context,
                getString(R.string.description_not_entered_info),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun isDescriptionEmpty(): Boolean {
        return descriptionEditText.text.toString() == ""
    }
}