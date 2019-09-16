package com.visuality.libraries.connection

class ConnectionConfiguration(
    val server: String,
    val onConnected: (() -> Unit)? = null,
    val onConnectionError: (() -> Unit)? = null,
    val onRequest: ((request: IncomingRequest, respond: (data: Any) -> Unit) -> Unit)? = null,
    val onDisconnected: (() -> Unit)? = null
)
