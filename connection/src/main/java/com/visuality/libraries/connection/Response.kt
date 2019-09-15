package com.visuality.libraries.connection

class Response(
    val requestId: String,
    val data: Any?
) {

    val type = TYPE

    companion object {
        const val TYPE = "raw.response"
    }
}
