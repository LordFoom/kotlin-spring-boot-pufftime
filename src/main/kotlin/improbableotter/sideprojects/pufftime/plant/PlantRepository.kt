package improbableotter.sideprojects.pufftime.plant

import org.springframework.data.jpa.repository.JpaRepository

interface PlantRepository: JpaRepository<Plant, Long> {
}