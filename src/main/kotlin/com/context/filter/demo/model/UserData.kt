package com.context.filter.demo.model

import java.util.UUID

data class UserData(val id: Long, val name: String, val isActive: Boolean = true) {
    val systemId: String = UUID.randomUUID().toString()
}
