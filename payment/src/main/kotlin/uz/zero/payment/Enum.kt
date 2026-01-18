package uz.zero.payment


enum class ErrorCode(val code: Int) {
    TYPE_NOT_FOUND(400),
    PAYMENT_NOT_FOUND(401)
}

enum class Type{
    CLICK,
    PAYME,
    UZUM
}
