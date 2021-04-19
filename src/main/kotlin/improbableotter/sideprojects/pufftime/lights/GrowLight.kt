package improbableotter.sideprojects.pufftime.lights

import improbableotter.sideprojects.pufftime.grow.Grow
import improbableotter.sideprojects.pufftime.user.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.persistence.*

@Entity
@Table(name="grow_light")
class GrowLight (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @ManyToOne
    @JoinColumn(name = "grow_id")
    val grow: Grow,
    @ManyToOne
    @JoinColumn(name = "light_id")
    val light: Light,
    val createDate: Date = Date(),
    val lastUpdate: Date = Date(),
){
    companion object{
        fun fromDto(dto: GrowLightDto)=GrowLight(grow=dto.grow!!, light=dto.light!!)
    }
}

data class GrowLightDto(
    var id: Long = -1,
    var growId: Long = 0,
    var grow: Grow? = null,
    var light: Light? = null,
    var lightId:  Long = 0,
    var userId: Long = 0,
//        var strain: Strain? = null,
    var createDate: Date = Date(),
)

interface GrowLightRepository : JpaRepository<GrowLight, Long> {
    fun findByGrowOrderByCreateDate(grow: Grow):List<GrowLight>
    fun findByGrowIdOrderByCreateDate(growId: Long):List<GrowLight>
}