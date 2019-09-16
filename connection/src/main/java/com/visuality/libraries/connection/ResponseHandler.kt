package com.visuality.libraries.connection

class ResponseHandler(
    val requestId: String,
    val handler: (data: Any?) -> Unit
)
