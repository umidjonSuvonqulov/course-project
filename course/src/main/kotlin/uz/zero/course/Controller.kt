package uz.zero.course

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/course")
class CourseController(private val courseService: CourseService) {

    @PostMapping("/add")
    fun add(@RequestBody coursePostDto: CoursePostDto) = courseService.add(coursePostDto)

    @GetMapping("/getAll")
    fun findAll(): List<CourseGetDto> = courseService.findAll()

    @GetMapping("/getPageable")
    fun findPageable(pageable: Pageable): Page<CourseGetDto> = courseService.findPageable(pageable)

    @GetMapping("/getById/{id}")
    fun findById(@PathVariable id: Long): CourseGetDto = courseService.findById(id)

    @PutMapping("/update/{id}")
    fun update(@PathVariable id: Long, @RequestBody courseUpdateDto: CourseUpdateDto) = courseService.update(id, courseUpdateDto)

    @DeleteMapping("/remove/{id}")
    fun delete(@PathVariable id: Long) = courseService.delete(id)
}