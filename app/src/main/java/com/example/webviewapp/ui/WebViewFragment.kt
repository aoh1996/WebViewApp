package com.example.webviewapp.ui

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.webviewapp.WebViewApp
import com.example.webviewapp.databinding.FragmentWebViewBinding
import com.example.webviewapp.util.HistoryManager

private const val BASE_URL = "https://google.com"

class WebViewFragment : Fragment() {

    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: SharedPreferences
    private lateinit var webView: WebView
    private lateinit var historyManager: HistoryManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        historyManager = (requireActivity().application as WebViewApp).getHistoryManager()
        historyManager.load()


        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                val previousUrl = historyManager.getPreviuosUrl()
                if (previousUrl.isBlank()) {
                    requireActivity().finish()
                } else {
                    webView.loadUrl(previousUrl)
                }
            }
        })

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

        webView = binding.webView

        if (historyManager.currentUrl.isBlank()) {
            webView.loadUrl(BASE_URL)

        } else {
            Log.d("webfrag", "new WebView")
            webView.loadUrl(historyManager.currentUrl)
        }

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = MyWebViewClient(historyManager)

        val view = binding.root
        return view
    }

    override fun onStop() {
        historyManager.saveAll()
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        historyManager.saveAll()
        super.onSaveInstanceState(outState)
    }

    private class MyWebViewClient(val historyManager: HistoryManager) : WebViewClient() {


        @SuppressLint("CommitPrefEdits")
        override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
            historyManager.addToHistory(url)
            super.doUpdateVisitedHistory(view, url, isReload)
        }
    }
}