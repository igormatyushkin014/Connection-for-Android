package com.visuality.libraries.connection

class IncomingRequest(
    val event: String,
    val data: Any?
) {

    companion object {
        const val TYPE = "raw.request"
    }
}
