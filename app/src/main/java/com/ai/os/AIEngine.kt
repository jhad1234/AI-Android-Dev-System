package com.ai.os

class AIEngine {

    private val memory = mutableListOf<String>()

    fun process(input: String): String {

        memory.add(input)

        return when {
            input.contains("hello", true) ->
                "Hello! AI Core is active."

            input.contains("status", true) ->
                "System running. Memory size: ${memory.size}"

            input.contains("clear", true) -> {
                memory.clear()
                "Memory cleared."
            }

            else ->
                "Command received: $input"
        }
    }
}
