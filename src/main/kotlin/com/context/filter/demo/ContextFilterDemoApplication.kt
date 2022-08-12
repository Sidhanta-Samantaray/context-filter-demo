package com.context.filter.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ContextFilterDemoApplication

fun main(args: Array<String>) {
    runApplication<ContextFilterDemoApplication>(*args)
}
