package uz.zero.user

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


@FeignClient(name = "course", url = "\${services.hosts.course}")
interface CourseFeignClient {

    @GetMapping("/getById/{id}")
    fun getCourseById(@PathVariable id: Long): CourseGetDto
}

@FeignClient(name = "payment", url = "\${services.hosts.payment}")
interface PaymentFeignClient {

    @PostMapping("/add")
    fun addPayment(@RequestBody paymentPostDto: PaymentPostDto)
}