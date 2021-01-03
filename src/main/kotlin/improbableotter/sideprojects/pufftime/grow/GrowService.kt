package improbableotter.sideprojects.pufftime.grow

import improbableotter.sideprojects.pufftime.user.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class GrowService (private val growRepository: GrowRepository,
                    private val userRepository: UserRepository){
    fun create(dto: GrowDto): Grow {
        dto.user = dto.user ?: userRepository.findByUsername(dto.username!!)
        return growRepository.save(Grow.fromDto(dto))
    }

    fun update(dto: GrowDto):Grow{
        growRepository.findByIdOrNull(dto.id!!)?:throw IllegalArgumentException("No grow with id ${dto.id} exists")
        return growRepository.save(Grow.fromDto(dto))
    }
}