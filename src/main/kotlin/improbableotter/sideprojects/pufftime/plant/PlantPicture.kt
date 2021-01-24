package improbableotter.sideprojects.pufftime.plant

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "plant_picture")
class PlantPicture(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne
    val plant: Plant,
    val filePath: String? = null,
    val notes: String? = null,
    @NotNull
    val createDate: Date = Date(),
    val picDate: Date = Date()
)

interface PlantPictureRepository : JpaRepository<PlantPicture, Long> {
    fun findTopByPlantIdOrderByCreateDateDesc(plantId: Long):PlantPicture?

}
