package com.context.filter.demo.model

import java.time.Instant

data class RequestContextHolder(
    val data: MutableMap<String, Any> = mutableMapOf(),
    val createdAt: Long = Instant.now().toEpochMilli(),
    var requestBodyString: RequestBodyString = RequestBodyString()
)
