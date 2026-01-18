package uz.zero.user

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uz.zero.user.UserPostDto.Companion.toEntity
import uz.zero.user.UserCourseDto.Companion.toEntity

interface UserService {
    fun register(userPostDto: UserPostDto)
    fun findAll(): List<UserGetDto>
    fun findPageable(pageable: Pageable): Page<UserGetDto>
    fun findById(id: Long): UserGetDto
    fun buyCourse(userCourseDto: UserCourseDto)
    fun update(id: Long, userUpdateDto: UserUpdateDto)
    fun addAdmin(id: Long)
    fun delete(id: Long)
    fun findCourses(id: Long): List<UserCourseGetDto>
    fun findUsers(id: Long): List<UserCourseGetDto>
}

@Service
class UserServiceImpl(private val userRepository: UserRepository,
                      private val userCourseRepository: UserCourseRepository,
                      private val courseFeignClient: CourseFeignClient,
                      private val paymentFeignClient: PaymentFeignClient) : UserService {

    @Transactional
    override fun register(userPostDto: UserPostDto) {
        userPostDto.run {
            userRepository.findByUsernameAndDeletedFalse(username)?.let {
                throw UsernameAlreadyExistException()
            }

            userRepository.findByPhoneNumberAndDeletedFalse(phoneNumber)?.let {
                throw PhoneNumberAlreadyExistException()
            }

            val user = toEntity(userPostDto)
            userRepository.save(user)
        }
    }

    override fun findAll(): List<UserGetDto> {
        return userRepository.findAllNotDeleted().map(UserGetDto::toResponse)
    }

    override fun findPageable(pageable: Pageable): Page<UserGetDto> {
        return userRepository.findAllNotDeleted(pageable).map(UserGetDto::toResponse)
    }

    override fun findById(id: Long): UserGetDto {
        val user = userRepository.findByIdAndDeletedFalse(id)
            ?: throw UserNotFoundException()
        return UserGetDto.toResponse(user)
    }

    @Transactional
    override fun update(id: Long, userUpdateDto: UserUpdateDto) {
        val user = userRepository.findByIdAndDeletedFalse(id)
            ?: throw UserNotFoundException()

        userUpdateDto.run {
            firstName?.let { user.firstName = it }
            lastName?.let { user.lastName = it }
            username?.let { newUsername ->
                userRepository.findByIdNotAndUsernameAndDeletedFalse(id, newUsername)
                    ?: throw UsernameAlreadyExistException()
                user.username = newUsername
            }
            phoneNumber?.let { newPhoneNumber ->
                userRepository.findByIdNotAndPhoneNumberAndDeletedFalse(id, newPhoneNumber)
                    ?: throw PhoneNumberAlreadyExistException()
                user.phoneNumber = newPhoneNumber
            }
            password?.let { user.password = it }
        }

        userRepository.save(user)


    }


    @Transactional
    override fun buyCourse(userCourseDto: UserCourseDto) {
        val user = userRepository.findByIdAndDeletedFalse(userCourseDto.userId)
            ?: throw UserNotFoundException()

        if (user.status == Status.BLOCKED){
            throw UserBlockedException()
        }

        val courseGetDto = courseFeignClient.getCourseById(userCourseDto.courseId)

        userCourseRepository.findByUserIdAndCourseIdAndDeletedFalse(userCourseDto.userId,userCourseDto.courseId)?.let {
            throw UserAlreadyBoughtCourseException()
        }


        paymentFeignClient.addPayment(PaymentPostDto(
            courseId = courseGetDto.id,
            userId = user.id!!,
            amount = courseGetDto.price,
            type = userCourseDto.type))

        val userCourse = toEntity(user, userCourseDto.courseId)
        userCourseRepository.save(userCourse)
    }

    @Transactional
    override fun addAdmin(id: Long) {
        val user = (userRepository.findByIdAndDeletedFalse(id)
            ?: throw UserNotFoundException())

        user.role = Role.ADMIN
        userRepository.save(user)
    }

    @Transactional
    override fun delete(id: Long) {
        userRepository.findByIdAndDeletedFalse(id)
            ?: throw UserNotFoundException()

        userRepository.trash(id)
    }

    override fun findCourses(id: Long): List<UserCourseGetDto> =
        userCourseRepository.findByUserIdAndDeletedFalse(id).map(UserCourseGetDto::toResponse)

    override fun findUsers(id: Long): List<UserCourseGetDto> =
        userCourseRepository.findByCourseIdAndDeletedFalse(id).map(UserCourseGetDto::toResponse)


}