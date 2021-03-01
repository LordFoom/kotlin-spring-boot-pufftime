package improbableotter.sideprojects.pufftime.nute

import improbableotter.sideprojects.pufftime.user.User
import improbableotter.sideprojects.pufftime.water.WateringHistory
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import kotlin.collections.ArrayList

@Entity
@Table(name = "feed")
class Feed(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "watering_history_id")
    var wateringHistory: WateringHistory,
    var notes: String? = null,
    @ManyToOne
    @JoinColumn(name = "nute_id")
    var nuteType: Nute?,
    var amount: Double = 0.0,
    val createDate: Date = Date(),
    var lastUpdate: Date = Date(),
)

enum class NuteStatus {
    AVAILABLE, UNAVAILABLE
}

@Entity
@Table(name = "nute")
class Nute(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String,
    var description: String?,
    var manufacturer: String?,
    @OneToMany(mappedBy = "nuteType", cascade = [CascadeType.ALL])
    var feeds: List<Feed>? = ArrayList(),
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User,
    var createDate: Date = Date(),
    var status: NuteStatus = NuteStatus.AVAILABLE,

    ) {

    companion object {

        fun fromDto(dto: NuteDto) = Nute(
            name = dto.name,
            description = dto.description,
            manufacturer = dto.manufacturer,
            user = dto.user!!,
            createDate = dto.createDate,
            status = dto.status,
        )

    }
}

data class NuteDto(
    var id: Long? = -1,
    @get:NotBlank(message = "Nute name required")
    var name: String = "",
    var description: String? = null,
    var manufacturer: String? = null,
    @get:Min(1)
    var userId: Long? = null,
    var user: User? = null,
    var createDate: Date = Date(),
    var status: NuteStatus = NuteStatus.AVAILABLE,
)

interface NuteRepository : JpaRepository<Nute, Long> {

    fun findByUser(user: User): List<Nute>
    fun findByUserId(userId: Long): List<Nute>

}


