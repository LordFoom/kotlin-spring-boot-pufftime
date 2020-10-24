package improbableotter.sideprojects.pufftime.plant

import improbableotter.sideprojects.pufftime.grow.Grow
import improbableotter.sideprojects.pufftime.user.User
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank

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
        @get:NotBlank
        val createDate: Date = Date(),
        val plantDate: Date? = Date(),
        val flowerDate: Date? = Date()
)

@Entity
@Table(name = "strain")
data class Strain(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        @OneToMany(mappedBy = "strain", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
        val plants: Set<Plant> = emptySet(),
        @get:NotBlank
        val createDate: Date = Date()
)