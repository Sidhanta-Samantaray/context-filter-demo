package com.context.filter.demo.model

import kotlinx.coroutines.reactor.ReactorContext
import kotlin.coroutines.coroutineContext

class RequestContextHelper {
    companion object {
        const val REQUEST_CONTEXT = "requestContext"
        suspend fun fetch(): RequestContextHolder {
            return coroutineContext[ReactorContext]?.context?.readOnly()
                ?.get<RequestContextHolder>(REQUEST_CONTEXT)
                ?: throw RuntimeException("Error Processing Context")
        }
    }
}
