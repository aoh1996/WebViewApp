package com.example.webviewapp.ui

import NetworkConnectionMonitor
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.webviewapp.WebViewApp
import com.example.webviewapp.databinding.FragmentStartBinding
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: SharedPreferences
    private lateinit var networkConnectionMonitor: NetworkConnectionMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = requireActivity().getSharedPreferences(
            "com.example.webviewapp",
            AppCompatActivity.MODE_PRIVATE
        )
        networkConnectionMonitor =
            (requireActivity().application as WebViewApp).networkConnectionMonitor
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity!!.finish()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        val view = binding.root

        setLicenceAgreement()

        networkConnectionMonitor.observe(viewLifecycleOwner) { isConnected ->
            binding.agreeButton.setOnClickListener {
                prefs.edit().putBoolean("license_accepted", true).apply()
                if (isConnected) {
                    findNavController().popBackStack()
                    findNavController().navigate(com.example.webviewapp.R.id.webViewFragment)
                } else {
                    findNavController().popBackStack()
                    findNavController().navigate(com.example.webviewapp.R.id.noInternetFragment)
                }
            }
        }

        binding.declineButton.setOnClickListener {
            prefs.edit().putBoolean("license_accepted", false).apply()
            requireActivity().finish()
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setLicenceAgreement() {
        val inputStream: InputStream =
            resources.openRawResource(com.example.webviewapp.R.raw.license)

        val byteArrayOutputStream = ByteArrayOutputStream()

        var i: Int
        try {
            i = inputStream.read()
            while (i != -1) {
                byteArrayOutputStream.write(i)
                i = inputStream.read()
            }
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        binding.licenseTextview.text = byteArrayOutputStream.toString()
    }

}