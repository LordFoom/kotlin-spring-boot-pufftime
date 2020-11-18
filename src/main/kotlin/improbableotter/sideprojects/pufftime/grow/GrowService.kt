package improbableotter.sideprojects.pufftime.grow

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GrowService {
    @Autowired
    lateinit var growRepository: GrowRepository
    fun create(dto: GrowDto): Grow {
        return growRepository.save(Grow.fromDto(dto))
    }
}