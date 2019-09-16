package com.visuality.libraries.connectiondemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.visuality.libraries.connection.Connection
import com.visuality.libraries.connection.ConnectionConfiguration
import com.visuality.libraries.connection.IncomingRequest
import org.json.JSONObject

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

        this.connection.send(
            event = "test",
            data = JSONObject().apply {
                put("greeting", "Hello!")
            },
            recipientId = null,
            callback = { data ->
            }
        )
    }

    private fun onConnectionError() {
        this.updateStatusTextView(
            status = "Connection error"
        )
    }

    private fun onRequest(
        request: IncomingRequest,
        respond: (data: Any) -> Unit
    ) {
        respond(
            JSONObject().apply {
                put("description", "Hello server!")
            }
        )
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
