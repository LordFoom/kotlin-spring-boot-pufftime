package improbableotter.sideprojects.pufftime.grow

import improbableotter.sideprojects.pufftime.plant.Plant
import improbableotter.sideprojects.pufftime.user.User
import javax.persistence.*

@Entity
@Table(name="grow")
data class Grow(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        @ManyToOne
        @JoinColumn(name = "user_id")
        val user: User,
        @OneToMany(mappedBy="grow", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
        val plants:Set<Plant> = emptySet()
) {

}