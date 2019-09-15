package com.visuality.libraries.connection

class Request(
    val requestId: String,
    val recipientId: String?,
    val event: String,
    val data: Any?
) {

    val type = TYPE

    companion object {
        const val TYPE = "raw.request"
    }
}
