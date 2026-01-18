package uz.zero.payment

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal


data class BaseMessage(val code: Int? = null, val message: String? = null){
    companion object{
        var OK = BaseMessage(0,"OK")
    }
}


data class PaymentPostDto(
    @field:NotNull(message = "{user.id.notBlank}")
    var userId: Long,
    @field:NotNull(message = "{course.id.notBlank}")
    var courseId: Long,
    @field:NotNull(message = "{amount.notBlank}")
    var amount: BigDecimal,
    @field:NotBlank(message = "{type.notBlank}")
    val type: String
    ){
    companion object{
        fun toEntity(paymentPostDto: PaymentPostDto) = Payment(
            userId = paymentPostDto.userId,
            courseId = paymentPostDto.courseId,
            amount = paymentPostDto.amount,
            type = Type.entries.firstOrNull {
                it.name.equals(paymentPostDto.type, ignoreCase = true)
            } ?: throw TypeNotFoundException()
        )
    }
}


data class PaymentGetDto(
    val id: Long,
    var userId: Long,
    var courseId: Long,
    var amount: BigDecimal
){
    companion object{
        fun toResponse(payment: Payment) = PaymentGetDto(
            id = payment.id!!,
            userId = payment.userId,
            courseId = payment.courseId,
            amount = payment.amount
        )
    }
}


data class UserGetDto(
    var id: Long,
    var firstName: String,
    var lastName: String,
    var username: String,
    var phoneNumber: String,
    var role: String
)


data class CourseGetDto(
    val id: Long,
    var name: String,
    var description: String?,
    var price: BigDecimal
)