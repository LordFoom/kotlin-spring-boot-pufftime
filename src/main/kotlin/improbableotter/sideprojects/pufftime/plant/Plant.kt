package improbableotter.sideprojects.pufftime.plant

import improbableotter.sideprojects.pufftime.grow.Grow
import improbableotter.sideprojects.pufftime.note.Note
import improbableotter.sideprojects.pufftime.picture.Picture
import improbableotter.sideprojects.pufftime.strain.Strain
import improbableotter.sideprojects.pufftime.user.User
import org.springframework.data.jpa.repository.JpaRepository
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull
import kotlin.collections.ArrayList

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
        @OneToMany(mappedBy="plant", cascade = [CascadeType.ALL])
        @OrderBy("picDate DESC")
        var pictures: List<Picture>? = ArrayList(),
        @OneToMany(mappedBy = "plant", cascade = [CascadeType.ALL] )
        var notes: List<Note>? = ArrayList(),
        @get:NotNull
        val createDate: Date = Date(),
        var startDate: Date? = null,
        var flowerDate: Date? = null,
        var harvestDate: Date? = null,
        var cureDate: Date? = null
){
        @Transient val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

        override fun toString(): String {
                return "${id}(${displayStartDate()})${strain.name}"
        }

        fun getDisplayName():String{
                return toString()
        }

        fun displayStartDate():String{
                return startDate?.let { simpleDateFormat.format(it)  } ?: ""
        }


        companion object{
                fun fromDto(dto: PlantDto, strain: Strain)=Plant(
                        user = dto.user!!,
                        grow = dto.grow!!,
                        strain = strain,
                        createDate = dto.createDate,
                        startDate = dto.plantDate,
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




