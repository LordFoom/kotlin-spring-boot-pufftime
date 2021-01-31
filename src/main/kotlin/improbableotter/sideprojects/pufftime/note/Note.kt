package improbableotter.sideprojects.pufftime.note

import improbableotter.sideprojects.pufftime.grow.Grow
import improbableotter.sideprojects.pufftime.plant.Plant
import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name="note")
class Note (
    @Id
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
}