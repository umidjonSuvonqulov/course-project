package uz.zero.user

import jakarta.validation.Valid
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
@RequestMapping
class UserController(private val userService: UserService) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody userPostDto: UserPostDto) = userService.register(userPostDto)

    @GetMapping("/getAll")
    fun findAll(): List<UserGetDto> = userService.findAll()

    @GetMapping("/getPageable")
    fun findPageable(@RequestBody pageable: Pageable): Page<UserGetDto> = userService.findPageable(pageable)

    @GetMapping("/getById/{id}")
    fun findById(@PathVariable id: Long): UserGetDto = userService.findById(id)

    @PutMapping("/update/{id}")
    fun update(@PathVariable id: Long, @RequestBody userUpdateDto: UserUpdateDto) = userService.update(id, userUpdateDto)

    @PutMapping("/addAdmin/{id}")
    fun addAdmin(@PathVariable id: Long) = userService.addAdmin(id)

    @DeleteMapping("/remove/{id}")
    fun delete(@PathVariable id: Long) = userService.delete(id)

    @PostMapping("/buy")
    fun buyCourse(@RequestBody userCourseDto: UserCourseDto) = userService.buyCourse(userCourseDto)

    //user sotib olgan kurslari
    @GetMapping("/getAll/{id}")
    fun findCourses(@PathVariable id: Long) = userService.findCourses(id)

    //courseni sotib olgan userlar
    @GetMapping("/getAl/{id}")
    fun findUsers(@PathVariable id: Long) = userService.findUsers(id)

}