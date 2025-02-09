package com.finlume.transport.rest.exceptions

import com.finlume.core.services.exceptions.AccountNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    // Trata exceções quando uma conta não é encontrada
    @ExceptionHandler(AccountNotFoundException::class)
    fun handleAccountNotFoundException(ex: AccountNotFoundException): ResponseEntity<Map<String, String>> {
        val responseBody = mapOf("error" to ex.message.orEmpty())
        return ResponseEntity(responseBody, HttpStatus.NOT_FOUND)
    }

    // Trata qualquer exceção não tratada previamente
    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<Map<String, String>> {
        val responseBody = mapOf("error" to "Internal Server Error: ${ex.message}")
        return ResponseEntity(responseBody, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}