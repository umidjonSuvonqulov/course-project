package uz.zero.course

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uz.zero.course.CourseGetDto.Companion.toResponse
import uz.zero.course.CoursePostDto.Companion.toEntity
import java.math.BigDecimal

interface CourseService {
    fun add(coursePostDto: CoursePostDto)
    fun findAll(): List<CourseGetDto>
    fun findPageable(pageable: Pageable): Page<CourseGetDto>
    fun findById(id: Long): CourseGetDto
    fun update(id: Long, courseUpdateDto: CourseUpdateDto)
    fun delete(id: Long)
}

@Service
class CourseServiceImpl(private val courseRepository: CourseRepository,
                        private val userFeignClient: UserFeignClient) : CourseService {

    @Transactional
    override fun add(coursePostDto: CoursePostDto) {

        if (coursePostDto.price < BigDecimal.ZERO) {
            throw CoursePriceInvalidException()
        }

        val course = toEntity(coursePostDto)
        courseRepository.save(course)
    }

    override fun findAll(): List<CourseGetDto> =
        courseRepository.findAllNotDeleted().map(CourseGetDto::toResponse)


    override fun findPageable(pageable: Pageable): Page<CourseGetDto> =
        courseRepository.findAllNotDeleted(pageable).map(CourseGetDto::toResponse)

    override fun findById(id: Long): CourseGetDto {
        val course = (courseRepository.findByIdAndDeletedFalse(id)
            ?: throw CourseNotFoundException())

        return toResponse(course)
    }

    @Transactional
    override fun update(id: Long, courseUpdateDto: CourseUpdateDto) {
        val course = (courseRepository.findByIdAndDeletedFalse(id)
            ?: throw CourseNotFoundException())

        courseUpdateDto.run {
            name?.let { course.name = it }
            description?.let { course.description = it }
            price?.let {
                if (it < BigDecimal.ZERO) {
                    throw CoursePriceInvalidException()
                }
                course.price = it
            }
        }
        courseRepository.save(course)
    }

    override fun delete(id: Long) {
        (courseRepository.findByIdAndDeletedFalse(id)
            ?: throw CourseNotFoundException())

        courseRepository.trash(id)
    }

}