package uz.zero.user


enum class ErrorCode(val code: Int) {
    USERNAME_ALREADY_EXIST(200),
    PHONE_NUMBER_ALREADY_EXIST(201),
    USER_NOT_FOUND(202),
    USER_ALREADY_BOUGHT(203),
    USER_BLOCKED(204)

}

enum class Role {
    ADMIN,
    USER
}

enum class Status{
    ACTIVE,
    BLOCKED
}