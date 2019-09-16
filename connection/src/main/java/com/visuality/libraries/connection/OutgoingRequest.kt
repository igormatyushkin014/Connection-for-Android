package com.visuality.libraries.connection

internal class OutgoingRequest(
    val requestId: String,
    val recipientId: String?,
    val event: String,
    val data: Any?
) {

    companion object {
        const val TYPE = "raw.request"
    }
}
