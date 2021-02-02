package improbableotter.sideprojects.pufftime.note

import improbableotter.sideprojects.pufftime.grow.Grow
import improbableotter.sideprojects.pufftime.plant.Plant
import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.*

@Entity
@Table(name="note")
class Note (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @ManyToOne
    val plant: Plant?,
    @ManyToOne
    val grow: Grow?,
    var note: String?,
 )

interface NoteRepository: JpaRepository<Note, Long>{

    fun findAllByGrow(grow:Grow):List<Note>
    fun findAllByPlant(plant:Plant):List<Note>
    fun findTopByGrowIdOrderByIdDesc(growId:Long):Note?
    fun findTopByGrowOrderByIdDesc(grow:Grow):Note?
}