package improbableotter.sideprojects.pufftime.grow

import improbableotter.sideprojects.pufftime.plant.Plant
import improbableotter.sideprojects.pufftime.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.format.annotation.DateTimeFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name="grow")
data class Grow(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        @get:NotBlank
        val name:String,
        val description: String?,
        val status: GrowStatus = GrowStatus.CREATED,
        @ManyToOne
        @JoinColumn(name = "user_id")
        val user: User,
        @OneToMany(mappedBy="grow", cascade = [CascadeType.ALL])
        val plants: MutableSet<Plant> = mutableSetOf(),
        val createDate: Date = Date(),
        val startDate: Date? = null,
        val flowerDate: Date? = null,
        val harvestDate: Date? = null
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
                        startDate = dto.getConvertedStartDate())
        }
}

enum class GrowStatus {
    NONE, CREATED, IN_PROGRESS, COMPLETED

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
)
{
        fun getConvertedStartDate():Date{
                return Date.from(startDate?.atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant()) ?:Date()
        }
}


interface GrowRepository:JpaRepository<Grow, Long>{
        fun findAllByUser(user: User):List<Grow>
        fun findAllByUserId(userId: Long):List<Grow>
}
