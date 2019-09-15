package com.visuality.libraries.connection

class ConnectionConfiguration(
    val server: String,
    val onConnected: (() -> Unit)?,
    val onConnectionError: (() -> Unit)?,
    val onRequest: ((request: Request, respond: (data: Any) -> Unit) -> Unit)?,
    val onDisconnected: (() -> Unit)?
)
