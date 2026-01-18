package uz.zero.payment

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


@FeignClient(name = "user", url = "\${services.hosts.user}")
interface UserFeignClient {

    @GetMapping("/getById/{id}")
    fun getUserById(@PathVariable id: Long) : UserGetDto
}

@FeignClient(name = "course", url = "\${services.hosts.course}")
interface CourseFeignClient {

    @GetMapping("/getById/{id}")
    fun getCourseById(@PathVariable id: Long) : CourseGetDto
}