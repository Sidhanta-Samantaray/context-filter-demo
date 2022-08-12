package com.context.filter.demo.controller

import com.context.filter.demo.model.RequestContextHelper
import com.context.filter.demo.model.UserData
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.delay
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/ping/user")
class PingUserController(@Autowired val objectMapper: ObjectMapper) {
    private val LOGGER: Logger = LoggerFactory.getLogger(javaClass)
    @PostMapping
    suspend fun createUser(@RequestBody userData: UserData): ResponseEntity<UserData> {
        LOGGER.info("Request Payload $userData")
        delay((100L..1200L).random())
        val requestContextHolder = RequestContextHelper.fetch()
        LOGGER.info("Request Context ${objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestContextHolder)}")
        delay((100L..1200L).random())
        return ResponseEntity.ok(userData)
    }
}
