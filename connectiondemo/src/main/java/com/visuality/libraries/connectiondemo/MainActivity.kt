package com.visuality.libraries.connectiondemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.visuality.libraries.connection.Connection
import com.visuality.libraries.connection.ConnectionConfiguration
import com.visuality.libraries.connection.Request

class MainActivity : AppCompatActivity() {

    private lateinit var connection: Connection

    private val statusTextView by lazy {
        this.findViewById<TextView>(R.id.status_text_view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)
        this.setupConnection()
    }

    private fun setupConnection() {
        this.connection = Connection(
            configuration = ConnectionConfiguration(
                server = "{INSERT YOUR SERVER URL HERE}",
                onConnected = {
                    this.onConnected()
                },
                onConnectionError = {
                    this.onConnectionError()
                },
                onRequest = { request, respond ->
                    this.onRequest(
                        request,
                        respond
                    )
                },
                onDisconnected = {
                    this.onDisconnected()
                }
            )
        )
    }

    private fun onConnected() {
        this.updateStatusTextView(
            status = "Connected"
        )
    }

    private fun onConnectionError() {
        this.updateStatusTextView(
            status = "Connection error"
        )
    }

    private fun onRequest(
        request: Request,
        respond: (data: Any) -> Unit
    ) {
        Toast.makeText(
            this,
            "Incoming request",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onDisconnected() {
        this.updateStatusTextView(
            status = "Disconnected"
        )
    }

    private fun updateStatusTextView(
        status: String
    ) {
        this.statusTextView.text = status
    }
}
