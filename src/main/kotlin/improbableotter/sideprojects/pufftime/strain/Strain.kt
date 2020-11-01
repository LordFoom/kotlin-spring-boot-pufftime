package improbableotter.sideprojects.pufftime.strain

import improbableotter.sideprojects.pufftime.plant.Plant
import improbableotter.sideprojects.pufftime.plant.StrainRepository
import improbableotter.sideprojects.pufftime.user.User
import improbableotter.sideprojects.pufftime.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.lang.IllegalStateException
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
        val description: String = "",
        @ManyToOne
        @JoinColumn(name = "user_id")
        val createdBy: User,
        @OneToMany(mappedBy = "strain", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
        val plants: MutableList<Plant> = mutableListOf(),
        @get:NotNull
        val createDate: Date = Date()
) {

    companion object {
        fun fromDto(dto: CreateStrainDto) = Strain(
                id = dto.id,
                name = dto.name,
                description = dto.description,
                createdBy = dto.user!!
                )
    }
}

data class CreateStrainDto(
        var id: Long? = null,
        @get:NotBlank
        var name: String = "",
        var description: String = "",
        var userId: Long = 0,
        var user: User? = null
)
