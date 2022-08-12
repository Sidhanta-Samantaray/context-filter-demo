package com.context.filter.demo.controller.filter

import com.context.filter.demo.model.RequestBodyString
import com.context.filter.demo.model.RequestContextHelper
import com.context.filter.demo.model.RequestContextHolder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import org.springframework.stereotype.Component
import org.springframework.util.Base64Utils
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.ServerWebExchangeDecorator
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.UUID

@Component
class ContextFilter : WebFilter {
    private val LOGGER: Logger = LoggerFactory.getLogger(javaClass)
    private val requestContextHolder: RequestContextHolder = RequestContextHolder()
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return chain.filter(ContextWebExchangeDecorator(exchange))
            .contextWrite {
                it.put(RequestContextHelper.REQUEST_CONTEXT, requestContextHolder)
            }
    }
  /*  suspend fun coRoutineFilter(exchange: ServerWebExchange, chain: WebFilterChain) {
        // LOGGER.info("coRoutineFilter ${exchange.request.body.awaitSingle()}")
        withContext()
        chain.filter(ContextWebExchangeDecorator(exchange)).awaitSingle()
    }*/
}
class ContextWebExchangeDecorator(delegate: ServerWebExchange) : ServerWebExchangeDecorator(delegate) {
    private val contextRequestDecorator = ContextRequestDecorator(delegate.request)

    @Override
    override fun getRequest(): ServerHttpRequest {
        return this.contextRequestDecorator
    }
}
class ContextRequestDecorator(delegate: ServerHttpRequest) : ServerHttpRequestDecorator(delegate) {
    private val requestBody: RequestBodyString = RequestBodyString()
    @Override
    override fun getBody(): Flux<DataBuffer> {
        return delegate.body.doOnNext {
            requestBody.data = Base64Utils.encodeToString(StandardCharsets.UTF_8.decode(it.asByteBuffer().asReadOnlyBuffer()).toString().encodeToByteArray())
        }.contextWrite {
            it.get<RequestContextHolder>(RequestContextHelper.REQUEST_CONTEXT).apply {
                data.putAll(
                    mapOf(
                        "reqId" to delegate.id,
                        "processedTime" to Instant.now().toEpochMilli(),
                        "randomKey" to UUID.randomUUID()
                    )
                )
                this.requestBodyString = this@ContextRequestDecorator.requestBody
            }
            it
        }
    }
}
