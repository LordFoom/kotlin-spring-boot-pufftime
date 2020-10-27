package improbableotter.sideprojects.pufftime.plant

import improbableotter.sideprojects.pufftime.grow.Grow
import improbableotter.sideprojects.pufftime.strain.Strain
import improbableotter.sideprojects.pufftime.user.User
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Entity
@Table(name = "plant")
data class Plant(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        @ManyToOne
        val user: User,
        @ManyToOne
        @JoinColumn(name="grow_id")
        val grow: Grow,
        @ManyToOne
        @JoinColumn(name="strain_id")
        val strain: Strain,
        @get:NotNull
        val createDate: Date = Date(),
        var plantDate: Date? = null,
        var flowerDate: Date? = null,
        val harvestDate: Date? = null,
        val cureDate: Date? = null
)

