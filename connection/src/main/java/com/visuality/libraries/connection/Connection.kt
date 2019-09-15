package com.visuality.libraries.connection

import io.socket.client.IO
import io.socket.client.Socket

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
                Socket.EVENT_MESSAGE
            ) { args ->
                print(args)
                /*this.configuration.onRequest?.invoke(
                    null,
                    null
                )*/
            }
            .on(
                Socket.EVENT_DISCONNECT
            ) { args ->
                this.configuration.onDisconnected?.invoke()
            }
        this.socket.connect()
    }

    companion object {
        const val EVENT = "connection.event"
    }
}
