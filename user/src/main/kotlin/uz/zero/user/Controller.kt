package uz.zero.user

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
@RequestMapping("/api/v1/user")
class UserController(private val userService: UserService) {

    @PostMapping("/add")
    fun register(@RequestBody userPostDto: UserPostDto) = userService.register(userPostDto)

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
}