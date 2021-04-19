package improbableotter.sideprojects.pufftime.lights

import improbableotter.sideprojects.pufftime.common.Status
import improbableotter.sideprojects.pufftime.user.User
import improbableotter.sideprojects.pufftime.user.UserDto
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.annotation.Generated
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "light")
class Light(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String? = "",
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,
    val brand: String? = null,
    val power: String? = null,
    val temperatureColor:String? = null,
    val notes: String? = null,
    val purchaseDate: Date? = null,
    val createDate: Date = Date(),
    val lastUpdate: Date = Date(),
    val statusId:Int = Status.ACTIVE.ordinal
 ){

    companion object {
        fun fromDto(dto: LightDto) = Light(
            id = dto.id,
            name = dto.name!!,
            brand = dto.brand,
            power = dto.power,
            temperatureColor = dto.temperatureColor,
            notes = dto.notes,
            purchaseDate = dto.purchaseDate,
            lastUpdate = Date(),
            user = dto.user!!,
        )
    }}

interface LightRepository : JpaRepository<Light, Long> {
    fun findAllByUserOrderByCreateDateDesc(user: User):List<Light>
    fun findAllByStatusIdOrderByIdDesc(statusId: Int):List<Light>
}

data class LightDto(
    var id: Long? = null,
    @get:NotBlank
    var name: String? = "",
    var username: String = "",
    var user: User? = null,
    var brand: String? = null,
    var power: String? = null,
    var temperatureColor:String? = null,
    var notes: String? = null,
    var purchaseDate: Date? = null,
    var createDate: Date? = null,
    var lastUpdate: Date?= null,
)