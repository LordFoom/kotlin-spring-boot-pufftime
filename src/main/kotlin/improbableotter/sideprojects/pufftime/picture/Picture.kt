package improbableotter.sideprojects.pufftime.picture

import improbableotter.sideprojects.pufftime.grow.Grow
import improbableotter.sideprojects.pufftime.plant.Plant
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "picture")
class Picture(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne
    val plant: Plant? = null,
    @ManyToOne
    val grow: Grow?,
    val filePath: String? = null,
    val notes: String? = null,
    @NotNull
    val createDate: Date = Date(),
    val picDate: Date = Date()
)

interface PictureRepository : JpaRepository<Picture, Long> {
    /**
     * Most recent plant pic
     */
    fun findTopByPlantIdOrderByCreateDateDesc(plantId: Long): Picture?

    /**
     * Most recent grow pic
     */
    fun findTopByGrowIdOrderByCreateDateDesc(plantId: Long): Picture?
    fun findTopByGrowOrderByCreateDateDesc(plant: Plant): Picture?

}
