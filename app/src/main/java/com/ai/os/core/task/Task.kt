package com.ai.os.core.task

/**
 * Core unit of work in the AI system
 */
data class Task(
    val id: String,
    val type: String,
    val payload: String,
    val priority: Int = 0,
    val status: TaskStatus = TaskStatus.PENDING
)

enum class TaskStatus {
    PENDING,
    RUNNING,
    COMPLETED,
    FAILED
}