package improbableotter.sideprojects.pufftime.pest

import improbableotter.sideprojects.pufftime.pesticide.Pesticide
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "pest")
class Pest(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String = "",
    val notes: String? = null,
    val createDate: Date = Date(),
    val lastUpdate: Date = Date(),

    @ManyToMany
    val pesticides: Set<Pesticide> = emptySet()

) {
    companion object {
        fun fromDto(dto: PestDto) = Pest(
            name = dto.name,
            notes = dto.notes,
            createDate = dto.createDate,
            lastUpdate = dto.lastUpdate
        )
    }
}

data class PestDto(
    var id: Long? = null,
    var name: String = "",
    var notes: String? = null,
    var createDate: Date = Date(),
    var lastUpdate: Date = Date(),
)

interface PestRepository : JpaRepository<Pest, Long>