package improbableotter.sideprojects.pufftime.plant

import improbableotter.sideprojects.pufftime.grow.Grow
import improbableotter.sideprojects.pufftime.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface PlantRepository: JpaRepository<Plant, Long> {
    fun findByStrainId(strainId: Long):List<Plant>
    fun findByStrain(strain: Strain):List<Plant>
    fun findByGrowId(growId: Long):List<Plant>
    fun findByGrow(grow: Grow):List<Plant>
    fun findByUserId(userId: Long):List<Plant>
    fun findByUser(user: User):List<Plant>
}

interface StrainRepository: JpaRepository<Strain, Long>{
    fun findByName(name: String):Strain?
}