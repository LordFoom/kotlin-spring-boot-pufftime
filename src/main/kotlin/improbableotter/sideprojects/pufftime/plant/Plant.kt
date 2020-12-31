package improbableotter.sideprojects.pufftime.plant

import improbableotter.sideprojects.pufftime.grow.Grow
import improbableotter.sideprojects.pufftime.strain.Strain
import improbableotter.sideprojects.pufftime.user.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "plant")
class Plant(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        @ManyToOne
        val user: User,
        @ManyToOne
        @JoinColumn(name="grow_id")
        val grow: Grow,
        @ManyToOne
        @JoinColumn(name="strain_id")
        val strain: Strain,
        @get:NotNull
        val createDate: Date = Date(),
        var plantDate: Date? = null,
        var flowerDate: Date? = null,
        var harvestDate: Date? = null,
        var cureDate: Date? = null
){
        companion object{
                fun fromDto(dto: PlantDto, strain: Strain)=Plant(
                        user = dto.user!!,
                        grow = dto.grow!!,
                        strain = strain,
                        createDate = dto.createDate,
                        plantDate = dto.plantDate,
                        flowerDate = dto.flowerDate,
                        harvestDate = dto.harvestDate,
                        cureDate = dto.cureDate
                )
        }
}

data class PlantDto(
        var id: Long? = null,
        var username: String? = null,
        var userId: Long? = null,
        var user: User? = null,
        var growId: Long? = 0,
        var grow: Grow? = null,
        var strainId: Long? = null,
//        var strain: Strain? = null,
        var createDate: Date = Date(),
        var plantDate: Date? = null,
        var flowerDate: Date? = null,
        var harvestDate: Date? = null,
        var cureDate: Date? = null,
        var numPlants: Int? = 1
)


interface PlantRepository: JpaRepository<Plant, Long> {
        fun findByStrainId(strainId: Long):List<Plant>
        fun findByStrain(strain: Strain):List<Plant>
        fun findByGrowId(growId: Long):List<Plant>
        fun findByGrow(grow: Grow):List<Plant>
        fun findByUserId(userId: Long):List<Plant>
        fun findByUser(user: User):List<Plant>
//        @Query("SELECT p FROM Plant p where growId = ?1")
        fun findByGrowIdOrderByStrainDesc(growId: Long): List<Plant>
}



