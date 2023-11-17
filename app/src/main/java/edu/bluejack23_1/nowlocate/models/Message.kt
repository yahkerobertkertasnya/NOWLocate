package edu.bluejack23_1.nowlocate.models

import java.util.Date

data class Message(
    val sender: User = User(),
    val message: String = "",
    val sentAt: Date = Date()

)
