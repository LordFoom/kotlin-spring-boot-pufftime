package improbableotter.sideprojects.pufftime.grow

import improbableotter.sideprojects.pufftime.user.UserRepository
import org.springframework.stereotype.Service

@Service
class GrowService (private val growRepository: GrowRepository,
                    private val userRepository: UserRepository){
    fun create(dto: GrowDto): Grow {
        dto.user = dto.user ?: userRepository.findByUsername(dto.username!!)
        return growRepository.save(Grow.fromDto(dto))
    }
}