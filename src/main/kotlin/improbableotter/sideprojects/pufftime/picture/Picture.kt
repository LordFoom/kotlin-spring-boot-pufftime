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
    @JoinTable(
        name = "grow_picture",
        joinColumns = arrayOf(JoinColumn(name="picture_id")),
        inverseJoinColumns = arrayOf(JoinColumn(name="grow_id")),
    )
    val grow: Grow?,

    @ManyToOne
    @JoinTable(
        name = "plant_picture",
        joinColumns = arrayOf(JoinColumn(name="picture_id")),
        inverseJoinColumns = arrayOf(JoinColumn(name="plant_id")),
    )
    val plant: Plant?,

    val filePath: String? = null,
    val smallFilePath: String? = null,
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
