package improbableotter.sideprojects.pufftime.strain

import improbableotter.sideprojects.pufftime.plant.Plant
import improbableotter.sideprojects.pufftime.user.User
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name = "strain")
data class Strain(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        @get:NotBlank
        val name: String = "",
        @ManyToOne
        @JoinColumn(name="user_id")
        val createdBy: User,
        @OneToMany(mappedBy = "strain", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
        val plants: MutableList<Plant> = mutableListOf(),
        @get:NotNull
        val createDate: Date = Date()
)
