package improbableotter.sideprojects.pufftime.grow

import improbableotter.sideprojects.pufftime.plant.Plant
import improbableotter.sideprojects.pufftime.user.User
import org.springframework.data.jpa.repository.JpaRepository
import java.text.SimpleDateFormat
import java.util.*
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
        val plants:Set<Plant> = emptySet(),
        val createDate: Date = Date()
) {

        @Transient
        val dateFormatter:SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

        fun getDisplayCreateDate():String{
                return dateFormatter.format(createDate)
        }
}

interface GrowRepository:JpaRepository<Grow, Long>{
        fun findAllByUser(user: User):Set<Grow>
        fun findAllByUserId(userId: Long):Set<Grow>
}
