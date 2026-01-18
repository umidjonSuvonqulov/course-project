package uz.zero.payment

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/payment")
class PaymentController(private val paymentService: PaymentService) {

    @PostMapping("/add")
    fun add(@RequestBody paymentPostDto: PaymentPostDto) = paymentService.add(paymentPostDto)

    @GetMapping("/getAll")
    fun findAll(): List<PaymentGetDto> = paymentService.findAll()

    @GetMapping("/getPageable")
    fun findPageable(@RequestBody pageable: Pageable): Page<PaymentGetDto> = paymentService.findPageable(pageable)

    @GetMapping("/getById/{id}")
    fun findById(@PathVariable id: Long): PaymentGetDto = paymentService.findById(id)

    @GetMapping("/getByUser/{id}")
    fun findByUserId(@PathVariable id: Long): List<PaymentGetDto> = paymentService.findByUserId(id)

    @GetMapping("/getByCourseId/{id}")
    fun findByCourseId(@PathVariable id: Long): List<PaymentGetDto> = paymentService.findByCourseId(id)

}