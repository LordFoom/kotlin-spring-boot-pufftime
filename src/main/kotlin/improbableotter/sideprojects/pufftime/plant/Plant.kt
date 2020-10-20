package improbableotter.sideprojects.pufftime.plant

import java.util.*
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

data class Plant(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
//        @Column(nullable = false, unique = true)
        @get:NotBlank
        val createDate:Date = Date(),
        val plantDate: Date? = Date(),
        val flowerDate: Date? = Date()

)