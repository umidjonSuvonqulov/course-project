package uz.zero.user

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.math.BigDecimal


data class BaseMessage(val code: Int? = null, val message: String? = null){
    companion object{
        var OK = BaseMessage(0,"OK")
    }
}


data class UserPostDto(
    @field:NotBlank(message = "{user.firstname.notBlank}")
    var firstName: String,

    @field:NotBlank(message = "{user.lastname.notBlank}")
    var lastName: String,

    @field:NotBlank(message = "{user.username.notBlank}")
    @field:Size(min = 4, max = 16, message = "{user.username.size}")
    @field:Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "{user.username.pattern}")
    var username: String,

    @field:NotBlank(message = "{user.phoneNumber.notBlank}")
    @field:Pattern(regexp = "^(\\+998)?[0-9]{9}$", message = "{user.phoneNumber.pattern}")
    var phoneNumber: String,

    @field:NotBlank(message = "{user.password.notBlank}")
    @field:Size(min = 4, message = "{user.password.size}")
    var password: String
){
    companion object{
        fun toEntity(userPostDto: UserPostDto) = User(
            firstName = userPostDto.firstName,
            lastName = userPostDto.lastName,
            username = userPostDto.username,
            phoneNumber = userPostDto.phoneNumber,
            password = userPostDto.password
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
){
    companion object{
        fun toResponse(user: User) = UserGetDto(
            id = user.id!!,
            firstName = user.firstName,
            lastName = user.lastName,
            username = user.username,
            phoneNumber = user.phoneNumber,
            role = user.role.name
        )
    }
}



data class UserUpdateDto(
    var firstName: String?,
    var lastName: String?,
    var username: String?,
    var phoneNumber: String?,
    var password: String?
)


data class CourseGetDto(
    val id: Long,
    var name: String,
    var description: String?,
    var price: BigDecimal
)



data class PaymentPostDto(
    var userId: Long,
    var courseId: Long,
    var amount: BigDecimal,
    val type: String
)

data class UserCourseDto(
    val userId: Long,
    val courseId: Long,
    val type: String
){
    companion object{
        fun toEntity(user: User, courseId: Long) = UserCourse(
            user = user,
            courseId = courseId
        )
    }
}

data class UserCourseGetDto(
    val userId: Long,
    val courseId: Long,
){
    companion object{
        fun toResponse(userCourse: UserCourse) = UserCourseGetDto(
            userCourse.user.id!!,
            userCourse.courseId
        )
    }
}

