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
        fun fromDto(dto: StrainDto) = Strain(
                 dto.id,
                 dto.name,
                 dto.description,
                 createdBy = dto.user!!
                )
    }
}

data class StrainDto(
        var id: Long? = null,
        @get:NotBlank
        var name: String = "",
        var description: String = "",
        var userName: String = "",
        var user: User? = null,
        var createDate: Date? = null,
)

