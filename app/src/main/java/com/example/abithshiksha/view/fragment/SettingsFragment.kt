package com.example.abithshiksha.view.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.FragmentMyCoursesBinding
import com.example.abithshiksha.databinding.FragmentSettingsBinding
import com.example.abithshiksha.helper.PrefManager
import com.example.abithshiksha.model.repo.Outcome
import com.example.abithshiksha.view.activity.AuthActivity
import com.example.abithshiksha.view.activity.ChangePasswordActivity
import com.example.abithshiksha.view_model.GetTopicViewModel
import com.example.abithshiksha.view_model.LogoutViewModel
import com.thekhaeng.pushdownanim.PushDownAnim
import com.user.caregiver.gone
import com.user.caregiver.isConnectedToInternet
import com.user.caregiver.loadingDialog
import com.user.caregiver.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val mLogoutViewModel: LogoutViewModel by viewModel()
    private lateinit var accessToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //get token
        accessToken = "Bearer "+ PrefManager.getKeyAuthToken()
        binding.progressBar.gone()

        PushDownAnim.setPushDownAnimTo(binding.logoutLay).setOnClickListener {
            showLogoutPopUp()
        }

        PushDownAnim.setPushDownAnimTo(binding.changePassLay).setOnClickListener {
            startActivity(Intent(requireActivity(), ChangePasswordActivity::class.java))
        }
    }

    private fun showLogoutPopUp(){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Logout")
        builder.setMessage("Do you want to exit the app ?")
        builder.setIcon(R.drawable.ic_baseline_warning_24)
        builder.setPositiveButton("Yes"){dialogInterface, which ->

            if (requireActivity().isConnectedToInternet()){
                logout(accessToken)
            }else{
                Toast.makeText(requireActivity(),"No internet connection.",Toast.LENGTH_LONG).show()
            }
        }
        builder.setNegativeButton("No"){dialogInterface, which ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun logout(
        token: String
    ){
        val loader = requireActivity().loadingDialog(false,R.raw.loader_bar)
        loader.show()
        mLogoutViewModel.logout(token).observe(viewLifecycleOwner) { outcome ->
            loader.dismiss()
            when (outcome) {
                is Outcome.Success -> {
                    if (outcome.data.status == 1) {
                        Toast.makeText(requireActivity(), outcome.data.message, Toast.LENGTH_SHORT)
                            .show()
                        PrefManager.clearPref()
                        startActivity(Intent(requireActivity(), AuthActivity::class.java))
                        requireActivity().finish()
                    } else {
                        Toast.makeText(requireActivity(), outcome.data.message, Toast.LENGTH_SHORT)
                            .show()
                        PrefManager.clearPref()
                        startActivity(Intent(requireActivity(), AuthActivity::class.java))
                        requireActivity().finish()
                    }
                }
                is Outcome.Failure<*> -> {
                    PrefManager.clearPref()
                    startActivity(Intent(requireActivity(), AuthActivity::class.java))
                    requireActivity().finish()

                    Toast.makeText(requireActivity(), R.string.network_issue, Toast.LENGTH_SHORT).show()

                    Log.i("statusMsg", outcome.e.message.toString())

                    outcome.e.printStackTrace()
                    Log.i("status", outcome.e.cause.toString())
                }
            }
        }
    }
}