package improbableotter.sideprojects.pufftime.strain.clone

import improbableotter.sideprojects.pufftime.strain.Strain
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "clone")
class Clone(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = -1,
    val cloneDate: Date = Date(),
    val created: Date = Date(),
    val updated: Date = Date(),
    @ManyToOne
    @JoinColumn(name = "strain_id")
    val strain: Strain,
    var status: CloneStatus = CloneStatus.CUTTING
)

enum class CloneStatus{
    UNKNOWN, CUTTING, ROOTS, TRANSPLANTED, FAILED
}

interface CloneRepository: JpaRepository<Clone, Long>{
    fun findByStrainOrderByCloneDateDesc(strain: Strain):List<Clone>
}