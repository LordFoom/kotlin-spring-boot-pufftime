package improbableotter.sideprojects.pufftime.grow

import improbableotter.sideprojects.pufftime.note.Note
import improbableotter.sideprojects.pufftime.picture.Picture
import improbableotter.sideprojects.pufftime.plant.Plant
import improbableotter.sideprojects.pufftime.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.format.annotation.DateTimeFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name="grow")
class Grow(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        @get:NotBlank
        val name:String,
        val description: String?,
        var status: GrowStatus = GrowStatus.CREATED,
        @ManyToOne
        @JoinColumn(name = "user_id")
        val user: User,
        @OneToMany(mappedBy="grow", cascade = [CascadeType.ALL])
        val plants: MutableSet<Plant> = mutableSetOf(),
        @OneToMany(mappedBy="grow", cascade = [CascadeType.ALL])
        val pictures: MutableSet<Picture> = mutableSetOf(),
        @OneToMany(mappedBy="grow", cascade = [CascadeType.ALL])
        val notes: MutableSet<Note> = mutableSetOf(),
        val createDate: Date = Date(),
        var lastUpdate: Date = Date(),
        var startDate: Date? = null,
        var flowerDate: Date? = null,
        var harvestDate: Date? = null,
        var type:  GrowType = GrowType.INDOOR//a default that suits me, don'cha know
) {

        @Transient
        val dateFormatter:SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

        fun getDisplayCreateDate():String{
                return dateFormatter.format(createDate)
        }

        companion object {
                fun fromDto(dto:GrowDto):Grow = Grow(dto.id,
                        name = dto.name,
                        user = dto.user!!,
                        description = dto.description,
                        createDate = dto.createDate?:Date(),
                        startDate = dto.getConvertedStartDate(),
                        harvestDate = dto.getConvertedHarvestDate(),
                        flowerDate = dto.getConvertedFlowerDate(),
                )

        }

        fun toDto(): GrowDto {
                val startDate = Instant.ofEpochMilli(startDate?.time!!)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                val flowerDate = flowerDate?.let {
                        Instant.ofEpochMilli(it.time)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                }
                val harvestDate = harvestDate?.let {
                        Instant.ofEpochMilli(it.time)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                }
                return GrowDto(id, name = name, description = description, startDate = startDate, harvestDate = harvestDate, flowerDate = flowerDate)
        }
}

enum class GrowStatus {
    NONE, CREATED, FLOWERING, HARVESTING, COMPLETED
}

enum class GrowType {
        NONE, INDOOR, OUTDOOR
}

data class GrowDto(
        var id: Long? = -1,
        var user: User? = null,
        var username: String? = "",
        @get:NotBlank
        var name: String,
        var description: String? = "",
        var userId: Long? = -1,
        var createDate: Date? = Date(),
        @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        var startDate: LocalDate? = null,
        @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        var flowerDate: LocalDate? = null,
        @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        var harvestDate: LocalDate? = null,
)
{
        fun getConvertedStartDate():Date{
                return Date.from(startDate?.atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant()) ?:Date()
        }

        fun getConvertedFlowerDate():Date?{
                return flowerDate?.let{
                        Date.from(it.atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant())
                }

        }
        fun getConvertedHarvestDate():Date?{
                return harvestDate?.let{
                        Date.from(it.atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant())
                }
        }
}


interface GrowRepository:JpaRepository<Grow, Long>{
        fun findAllByUser(user: User):List<Grow>
        fun findAllByUserId(userId: Long):List<Grow>
}
