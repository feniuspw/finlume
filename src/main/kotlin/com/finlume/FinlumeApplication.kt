package com.finlume

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FinlumeApplication

fun main(args: Array<String>) {
	runApplication<FinlumeApplication>(*args)
}
