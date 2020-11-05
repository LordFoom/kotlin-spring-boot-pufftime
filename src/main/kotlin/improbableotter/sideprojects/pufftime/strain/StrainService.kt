package improbableotter.sideprojects.pufftime.strain

import improbableotter.sideprojects.pufftime.plant.StrainRepository
import improbableotter.sideprojects.pufftime.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class StrainService {
    @Autowired
    lateinit var strainRepository: StrainRepository
    @Autowired
    lateinit var userRepository: UserRepository
    fun create(dto: CreateStrainDto): Strain {
        val user = userRepository.findByUsername(dto.userName)
        user ?: throw IllegalStateException("No such user: " + dto.userName)
        dto.user = user
        return strainRepository.save(Strain.fromDto(dto))
    }
}
