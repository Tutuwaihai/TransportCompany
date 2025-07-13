package com.transportcompany.transport_app.exception

class InvalidJwtTokenException(message: String = "Недействительный JWT токен") : RuntimeException(message)