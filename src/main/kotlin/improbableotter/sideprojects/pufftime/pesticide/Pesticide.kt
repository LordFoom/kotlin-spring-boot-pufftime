package improbableotter.sideprojects.pufftime.pesticide

import improbableotter.sideprojects.pufftime.pest.Pest
import improbableotter.sideprojects.pufftime.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.format.annotation.DateTimeFormat
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
    @ManyToMany
    @JoinTable(
        name = "pesticide_pests",
        joinColumns = arrayOf(JoinColumn(name="pesticide_id")),
        inverseJoinColumns = arrayOf(JoinColumn(name="pest_id")),
    )
    var pests:Set<Pest> = emptySet()

    ){
    companion object{
        fun fromDto(dto: PesticideDto) = Pesticide(
            name = dto.name,
            brand = dto.brand,
            createDate = dto.createDate,
            purchaseDate = dto.purchaseDate,
            user = dto.user!!,
            notes = dto.notes,
        )

    }


}

data class PesticideDto(
    var id: Long? = null,
    var name: String="",
    var brand: String="",
    var notes: String?=null,
    var user:User?=null,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    var purchaseDate: Date = Date(),
    var createDate: Date = Date(),
    var lastUpdate: Date = Date(),
)

interface PesticideRepository : JpaRepository<Pesticide, Long>{

    fun findAllByUserOrderByName(user: User):List<Pesticide>
    fun findByName(name: String): Pesticide?

}