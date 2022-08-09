package com.example.webviewapp.util

import android.app.Application
import java.io.File
import java.util.*


class HistoryManager(private val application: Application) {

    private val historyList = Stack<String>()
    var currentUrl = ""

    fun addToHistory(s: String?) {
        s?.let {
            if (it != currentUrl) {
                if (currentUrl.isNotBlank()) {
                    historyList.push(currentUrl)
                }
                currentUrl = it
            }

        }
    }

    fun saveAll() {
        val stackLastUrl = try {
            historyList.peek()
        } catch (e: Exception) {
            ""
        }
        if (currentUrl.isNotBlank() && stackLastUrl != currentUrl) {
            historyList.push(currentUrl)
        }

        val stackpath = File(application.externalCacheDir, "webstack")
        stackpath.writeText("")
        historyList.forEach { s ->
            stackpath.appendText("\n$s")
        }
    }

    fun getPreviuosUrl(): String {

        return try {
            val previousUrl = historyList.pop()
            currentUrl = ""
            previousUrl
        } catch (e: Exception) {
            ""
        }

    }

    fun load() {
        val stackpath = File(application.externalCacheDir, "webstack")


        if (stackpath.exists()) {
            stackpath.forEachLine { line ->
                historyList.push(line)
            }
        }

        if (!historyList.isEmpty()) {
            currentUrl = historyList.pop()
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: HistoryManager? = null

        fun getInstance(application: Application): HistoryManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: HistoryManager(application).also {
                    INSTANCE = it
                }
            }
        }
    }
}