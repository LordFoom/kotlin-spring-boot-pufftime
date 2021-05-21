package improbableotter.sideprojects.pufftime.pesticide

import improbableotter.sideprojects.pufftime.user.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.persistence.*

@Entity
@Table(name="pesticide")
class Pesticide (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String="",
    val brand: String?=null,
    val notes: String?=null,
    @ManyToOne
    @JoinColumn(name="user_id")
    val user: User,
    val purchaseDate: Date = Date(),
    val createDate: Date = Date(),
    val lastUpdate: Date = Date(),

    )

data class PesticideDto(
    var id: Long? = null,
    var name: String="",
    var brand: String="",
    var notes: String?=null,
    var purchaseDate: Date = Date(),
    var createDate: Date = Date(),
    var lastUpdate: Date = Date(),
)

interface PesticideRepository : JpaRepository<Pesticide, Long>{

    fun findAllByUserOrderByName(user: User)

}