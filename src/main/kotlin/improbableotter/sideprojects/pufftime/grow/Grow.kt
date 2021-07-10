package improbableotter.sideprojects.pufftime.grow

import improbableotter.sideprojects.pufftime.note.Note
import improbableotter.sideprojects.pufftime.picture.Picture
import improbableotter.sideprojects.pufftime.plant.Plant
import improbableotter.sideprojects.pufftime.user.User
import improbableotter.sideprojects.pufftime.water.WateringHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.format.annotation.DateTimeFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "grow")
class Grow(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @get:NotBlank
    val name: String,
    val description: String?,
    var status: GrowStatus = GrowStatus.CREATED,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,
    @OneToMany(mappedBy = "grow", cascade = [CascadeType.ALL])
    val wateringHistory: MutableSet<WateringHistory> = mutableSetOf(),
    @OneToMany(mappedBy = "grow", cascade = [CascadeType.ALL])
    val plants: MutableSet<Plant> = mutableSetOf(),
    @OneToMany(mappedBy = "grow", cascade = [CascadeType.ALL])
    @OrderBy("picDate DESC")
    val pictures: MutableList<Picture> = mutableListOf<Picture>(),
    @OneToMany(mappedBy = "grow", cascade = [CascadeType.ALL])
    val notes: MutableSet<Note> = mutableSetOf(),
    val createDate: Date = Date(),
    var lastUpdate: Date = Date(),
    var startDate: Date? = null,
    var flowerDate: Date? = null,
    var harvestDate: Date? = null,
    var type: GrowType = GrowType.INDOOR,//a default that suits me, don'cha know
) {


    fun getDisplayCreateDate(): String {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        return dateFormatter.format(createDate)
    }

    /**
     * Return list of pictures that aren't plant specific
     */
    fun getAllGrowPics():List<Picture>{
       return pictures.filter { it.plant == null }
    }

    fun getMostRecentPicture():Picture{
        return getAllGrowPics().first()//ordered by picDate desc
    }

    companion object {
        fun fromDto(dto: GrowDto): Grow = Grow(
            dto.id,
            name = dto.name,
            user = dto.user!!,
            description = dto.description,
            createDate = dto.createDate ?: Date(),
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
        return GrowDto(
            id,
            name = name,
            description = description,
            startDate = startDate,
            harvestDate = harvestDate,
            flowerDate = flowerDate
        )
    }

    fun getDisplayStartDate(): String {
        return displayDate(startDate)
    }

    fun getDisplayFloweringDate(): String {
        return displayDate(startDate)
    }

    fun getDisplayHarvestDate(): String {
        return displayDate(harvestDate)
    }

    /**
     * Will do yyyy-MM-dd for date, or "unknown" if null
     */
    fun displayDate(dateToDisplay: Date?): String{
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        return dateToDisplay?.let{ dateFormatter.format(it)}?: "unknown"
    }

}

enum class GrowStatus {
    NONE, CREATED, FLOWERING, HARVESTED, COMPLETED
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
) {
    fun getConvertedStartDate(): Date {
        return Date.from(startDate?.atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant()) ?: Date()
    }

    fun getConvertedFlowerDate(): Date? {
        return flowerDate?.let {
            Date.from(it.atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant())
        }

    }

    fun getConvertedHarvestDate(): Date? {
        return harvestDate?.let {
            Date.from(it.atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant())
        }
    }
}


interface GrowRepository : JpaRepository<Grow, Long> {
    fun findAllByUser(user: User): List<Grow>
    fun findAllByUserId(userId: Long): List<Grow>
}
