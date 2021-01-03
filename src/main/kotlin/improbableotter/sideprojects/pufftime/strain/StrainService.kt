package improbableotter.sideprojects.pufftime.strain

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
    fun create(dto: StrainDto): Strain {
        val user = userRepository.findByUsername(dto.username)
        user ?: throw IllegalStateException("No such user: " + dto.username)
        dto.user = user

        val strain = strainRepository.findByName(dto.name)

        strain?.let{
            throw IllegalStateException("Strain already exists: "+dto.name)
        }

        return strainRepository.save(Strain.fromDto(dto))
    }

    fun updateStrain(dto: StrainDto):Strain {
        strainRepository.findByIdOrNull(dto.id!!)
                ?:throw IllegalStateException("No such strain: "+dto.name)
        return strainRepository.save(Strain.fromDto(dto))
    }
}
