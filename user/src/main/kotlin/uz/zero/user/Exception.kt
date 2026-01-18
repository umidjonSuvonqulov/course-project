package uz.zero.user

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.util.Locale


@ControllerAdvice
class ExceptionHandler(
    private val errorMessageSource: MessageSource,
) {
    @ExceptionHandler(Throwable::class)
    fun handleOtherExceptions(exception: Throwable): ResponseEntity<Any> {
        when (exception) {
            is UserServiceException-> {

                return ResponseEntity
                    .badRequest()
                    .body(exception.getErrorMessage(errorMessageSource))
            }

            is MethodArgumentNotValidException -> {
                val errors = exception.bindingResult.allErrors.map { error ->
                    val fieldName = if (error is FieldError) error.field else error.objectName
                    val message = errorMessageSource.getMessage(error, LocaleContextHolder.getLocale())
                    BaseMessage(400, "$fieldName: $message")
                }
                return ResponseEntity.badRequest().body(errors)
            }

            else -> {
                exception.printStackTrace()
                return ResponseEntity
                    .badRequest().body(
                        BaseMessage(
                            500,
                            "Iltimos support bilan bog'laning"
                        )
                    )
            }
        }
    }

}



sealed class UserServiceException(message: String? = null) : RuntimeException(message) {
    abstract fun errorType(): ErrorCode
    protected open fun getErrorMessageArguments(): Array<Any>? = null
    fun getErrorMessage(errorMessageSource: MessageSource): BaseMessage {
        return BaseMessage(
            errorType().code,
            errorMessageSource.getMessage(
                errorType().toString(),
                getErrorMessageArguments(),
                Locale(LocaleContextHolder.getLocale().language)
            )
        )
    }
}




class UsernameAlreadyExistException() : UserServiceException() {
    override fun errorType() = ErrorCode.USERNAME_ALREADY_EXIST
}

class PhoneNumberAlreadyExistException() : UserServiceException() {
    override fun errorType() = ErrorCode.PHONE_NUMBER_ALREADY_EXIST
}

class UserNotFoundException() : UserServiceException() {
    override fun errorType() = ErrorCode.USER_NOT_FOUND
}

class UserAlreadyBoughtCourseException() : UserServiceException() {
    override fun errorType() = ErrorCode.USER_ALREADY_BOUGHT
}

class UserBlockedException() : UserServiceException() {
    override fun errorType() = ErrorCode.USER_BLOCKED
}



