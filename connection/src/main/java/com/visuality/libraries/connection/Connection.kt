package com.visuality.libraries.connection

import android.os.Handler
import android.os.Looper
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.lang.Exception

class Connection(
    private val configuration: ConnectionConfiguration
) {

    private lateinit var socket: Socket

    private val responseHandlers = arrayListOf<ResponseHandler>()

    private val idGenerator = IdGenerator()

    init {
        this.socket = IO.socket(
            configuration.server
        )
        this.setupSocketIO()
    }

    private fun setupSocketIO() {
        this.socket
            .on(
                Socket.EVENT_CONNECT
            ) { args ->
                this.configuration.onConnected?.invoke()
            }
            .on(
                Socket.EVENT_CONNECT_ERROR
            ) { args ->
                this.configuration.onConnectionError?.invoke()
            }
            .on(
                EVENT
            ) { args ->
                val requestJsonObject = args.firstOrNull() ?: return@on

                if (requestJsonObject !is JSONObject) {
                    return@on
                }

                val type: String

                try {
                    type = requestJsonObject.getString("type")
                } catch (exception: Exception) {
                    return@on
                }

                when (type) {
                    IncomingRequest.TYPE -> {
                        val requestId: String
                        val event: String
                        var data: Any? = null

                        try {
                            requestId = requestJsonObject.getString("requestId")
                            event = requestJsonObject.getString("event")

                            if (requestJsonObject.has("data")) {
                                data = requestJsonObject.get("data")
                            }
                        } catch (exception: Exception) {
                            return@on
                        }

                        val request = IncomingRequest(
                            event,
                            data
                        )
                        val respond: (data: Any) -> Unit = { data ->
                            val response = Response(
                                requestId = requestId,
                                data = data
                            )
                            this.sendResponse(
                                response
                            )
                        }
                        this.runCallback {
                            this.configuration.onRequest?.invoke(
                                request,
                                respond
                            )
                        }
                    }
                    Response.TYPE -> {
                        val requestId: String
                        var data: Any? = null

                        try {
                            requestId = requestJsonObject.getString("requestId")

                            if (requestJsonObject.has("data")) {
                                data = requestJsonObject.get("data")
                            }
                        } catch (exception: Exception) {
                            return@on
                        }

                        val responseHandler = this.responseHandlers.find { it.requestId == requestId }
                            ?: return@on
                        this.responseHandlers.remove(
                            responseHandler
                        )
                        responseHandler.handler(
                            data
                        )
                    }
                }
            }
            .on(
                Socket.EVENT_DISCONNECT
            ) { args ->
                this.configuration.onDisconnected?.invoke()
            }
        this.socket.connect()
    }

    private fun sendRequest(
        request: OutgoingRequest
    ) {
        val event = EVENT
        val data = JSONObject().apply {
            put("type", OutgoingRequest.TYPE)
            put("recipientId", request.recipientId)
            put("requestId", request.requestId)
            put("event", request.event)
            put("data", request.data)
        }
        this.socket.emit(
            event,
            data
        )
    }

    private fun sendResponse(
        response: Response
    ) {
        val event = EVENT
        val data = JSONObject().apply {
            put("type", Response.TYPE)
            put("requestId", response.requestId)
            put("data", response.data)
        }
        this.socket.emit(
            event,
            data
        )
    }

    private fun runCallback(
        callback: () -> Unit
    ) {
        Handler(
            Looper.getMainLooper()
        ).post {
            callback()
        }
    }

    fun send(
        event: String,
        data: Any?,
        recipientId: String? = null,
        callback: ((data: Any?) -> Unit)? = null
    ) {
        val requestId = this.idGenerator.getNextId()
        val request = OutgoingRequest(
            requestId = requestId,
            recipientId = recipientId,
            event = event,
            data = data
        )

        callback?.let { callback ->
            val responseHandler = ResponseHandler(
                requestId = requestId,
                handler = callback
            )
            this.responseHandlers.add(
                responseHandler
            )
        }

        this.sendRequest(
            request
        )
    }

    companion object {
        const val EVENT = "connection.event"
    }
}
