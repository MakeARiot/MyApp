package com.example.myapplication.model.network

import android.content.Context
import android.net.ConnectivityManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class Network {

    suspend fun ping(ip: String, context: Context, timeout: Int = 5000): String {
        if (isOnline(context)) {
            return withContext(Dispatchers.IO) {
                try {
                    val process = ProcessBuilder(
                        "ping",
                        "-c",
                        "8",
                        "-w",
                        (timeout / 1000).toString(),
                        ip
                    ).start()
                    val input = BufferedReader(InputStreamReader(process.inputStream))
                    val output = StringBuilder()
                    input.lineSequence().forEach {
                        output.append(it).append("\n")
                    }
                    output.toString()
                } catch (e: Exception) {
                    "Failed to ping $ip: $e"
                }
            }
        } else {
            return "network error"
        }
    }

    private fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }
}