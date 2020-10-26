package improbableotter.sideprojects.pufftime.plant

import improbableotter.sideprojects.pufftime.grow.Grow
import improbableotter.sideprojects.pufftime.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface PlantRepository: JpaRepository<Plant, Long> {
    fun findByStrainId(strainId: Long):Set<Plant>
    fun findByStrain(strain: Strain):Set<Plant>
    fun findByGrowId(growId: Long):Set<Plant>
    fun findByGrow(grow: Grow):Set<Plant>
    fun findByUserId(userId: Long):Set<Plant>
    fun findByUser(user: User):Set<Plant>
}

interface StrainRepository: JpaRepository<Strain, Long>{
    fun findByName(name: String):Strain?
}