package improbableotter.sideprojects.pufftime.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findByUsername(username: String): User?;

    fun save(dto: UserRegistrationDto): User {
        val user = User(
                username = dto.username,
                password = dto.password,
                email = dto.email
        )
        return save(user)
    }
}