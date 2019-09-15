package com.visuality.libraries.connection

class RequestOptions(
    val recipientId: String?,
    val data: Any?,
    val callback: ((response: Response) -> Unit)?
)
