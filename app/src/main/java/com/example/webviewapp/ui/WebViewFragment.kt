package com.example.webviewapp.ui

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.webviewapp.databinding.FragmentWebViewBinding

class WebViewFragment : Fragment() {

    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: SharedPreferences
    private lateinit var webView: WebView
    private var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = requireActivity().getSharedPreferences(
            "com.example.webviewapp",
            AppCompatActivity.MODE_PRIVATE
        )
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)

        url = prefs.getString("url", "https://google.com")!!

        webView = binding.webView
        if (savedInstanceState != null)
            webView.restoreState(savedInstanceState);
        else
            webView.loadUrl(url)

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = MyWebViewClient(prefs)

        webView.setOnKeyListener(View.OnKeyListener { _, keyCode, keyEvent ->

            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (webView.canGoBack()) {
                        webView.goBack()
                    } else {
                        requireActivity().finish()
                    }
                }
            }
            return@OnKeyListener true
        })
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {

        webView.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    private class MyWebViewClient(val prefs: SharedPreferences) : WebViewClient() {


        @SuppressLint("CommitPrefEdits")
        override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
            prefs.edit().putString("url", url).apply()
            super.doUpdateVisitedHistory(view, url, isReload)
        }
    }
}