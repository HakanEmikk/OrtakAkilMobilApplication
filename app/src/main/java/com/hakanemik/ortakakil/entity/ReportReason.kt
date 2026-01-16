package com.hakanemik.ortakakil.entity

enum class ReportReason(val reason: String, val description: String) {
    SPAM("Spam", "Gereksiz veya tekrarlayan içerik"),
    INAPPROPRIATE("Uygunsuz İçerik", "Rahatsız edici veya uygunsuz materyal"),
    HATE_SPEECH("Nefret Söylemi", "Ayrımcılık veya nefret barındıran söylem"),
    VIOLENCE("Şiddet", "Şiddet içeren veya teşvik eden içerik"),
    MISLEADING("Yanıltıcı Bilgi", "Yanlış veya yanıltıcı bilgi"),
    OTHER("Diğer", "Diğer sebepler")
}
