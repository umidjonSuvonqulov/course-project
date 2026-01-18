package uz.zero.payment

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uz.zero.payment.PaymentGetDto.Companion.toResponse
import uz.zero.payment.PaymentPostDto.Companion.toEntity
import kotlin.collections.map

interface PaymentService {
    fun add(paymentPostDto: PaymentPostDto)
    fun findAll(): List<PaymentGetDto>
    fun findPageable(pageable: Pageable): Page<PaymentGetDto>
    fun findById(id: Long): PaymentGetDto
    fun findByUserId(userId: Long): List<PaymentGetDto>
    fun findByCourseId(courseId: Long): List<PaymentGetDto>
}

@Service
class PaymentServiceImpl(private val paymentRepository: PaymentRepository,
                         private val userFeignClient: UserFeignClient,
                         private val courseFeignClient: CourseFeignClient) : PaymentService {

    @Transactional
    override fun add(paymentPostDto: PaymentPostDto) {
        val payment = toEntity(paymentPostDto)
        paymentRepository.save(payment)
    }


    override fun findAll(): List<PaymentGetDto> =
        paymentRepository.findAllNotDeleted().map(PaymentGetDto::toResponse)


    override fun findPageable(pageable: Pageable): Page<PaymentGetDto> =
        paymentRepository.findAllNotDeleted(pageable).map(PaymentGetDto::toResponse)


    override fun findById(id: Long): PaymentGetDto {
        val payment = (paymentRepository.findByIdAndDeletedFalse(id)
            ?: throw PaymentNotFoundException())

        return toResponse(payment)
    }

    override fun findByUserId(userId: Long): List<PaymentGetDto> {
        userFeignClient.getUserById(userId)
        return paymentRepository.findByUserIdAndDeletedFalse(userId).map(PaymentGetDto::toResponse)
    }

    override fun findByCourseId(courseId: Long): List<PaymentGetDto> {
        courseFeignClient.getCourseById(courseId)
        return paymentRepository.findByCourseIdAndDeletedFalse(courseId).map(PaymentGetDto::toResponse)
    }


}