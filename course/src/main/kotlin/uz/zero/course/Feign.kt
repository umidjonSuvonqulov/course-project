package uz.zero.course

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable


@FeignClient(name = "user", url = "\${services.hosts.user}")
interface UserFeignClient {

    @GetMapping("/getByCourseId/{id}")
    fun getUserById(@PathVariable id: Long) : UserGetDto
}
