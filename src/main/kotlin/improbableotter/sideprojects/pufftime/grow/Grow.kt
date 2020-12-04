package improbableotter.sideprojects.pufftime.grow

import improbableotter.sideprojects.pufftime.plant.Plant
import improbableotter.sideprojects.pufftime.user.User
import org.springframework.data.jpa.repository.JpaRepository
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name="grow")
data class Grow(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        @NotNull
        val name:String,
        val description: String?,
        val status: GrowStatus = GrowStatus.CREATED,
        @ManyToOne
        @JoinColumn(name = "user_id")
        val user: User,
        @OneToMany(mappedBy="grow", cascade = [CascadeType.ALL])
        val plants: MutableSet<Plant> = mutableSetOf(),
        val createDate: Date = Date(),
        val startDate: Date = Date(),
        val flowerDate: Date = Date(),
        val harvestDate: Date = Date()
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
                        startDate = dto.startDate?:Date())
        }
}

enum class GrowStatus {
    CREATED, IN_PROGRESS, COMPLETED

}

data class GrowDto(
        var id: Long? = -1,
        var user: User? = null,
        var username: String? = "",
        @NotNull
        var name: String,
        var description: String? = "",
        var userId: Long? = -1,
        var createDate: Date? = Date(),
        var startDate: Date? = Date(),
){
}

interface GrowRepository:JpaRepository<Grow, Long>{
        fun findAllByUser(user: User):List<Grow>
        fun findAllByUserId(userId: Long):List<Grow>
}
