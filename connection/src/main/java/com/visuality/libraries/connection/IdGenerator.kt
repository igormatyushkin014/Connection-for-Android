package com.visuality.libraries.connection

internal class IdGenerator {

    private var lastId = 0

    fun getNextId(): String {
        val nextId = this.lastId + 1
        this.lastId = nextId
        return String.format(
            "%s",
            nextId
        )
    }
}
