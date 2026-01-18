package uz.zero.course

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal


data class BaseMessage(val code: Int? = null, val message: String? = null){
    companion object{
        var OK = BaseMessage(0,"OK")
    }
}


data class CoursePostDto(
    @field:NotBlank(message = "{course.name.notBlank}")
    var name: String,
    var description: String?,
    @field:NotNull(message = "{course.price.notNull}")
    var price: BigDecimal,
    ){
    companion object{
        fun toEntity(coursePostDto: CoursePostDto) = Course(
            name = coursePostDto.name,
            description = coursePostDto.description,
            price = coursePostDto.price
        )
    }
}


data class CourseGetDto(
    val id: Long,
    var name: String,
    var description: String?,
    var price: BigDecimal
){
    companion object{
        fun toResponse(course: Course) = CourseGetDto(
            id = course.id!!,
            name = course.name,
            description = course.description,
            price = course.price
        )
    }
}



data class CourseUpdateDto(
    var name: String?,
    var description: String?,
    var price: BigDecimal?
)


data class UserGetDto(
    var id: Long,
    var firstName: String,
    var lastName: String,
    var username: String,
    var phoneNumber: String,
    var role: String
)