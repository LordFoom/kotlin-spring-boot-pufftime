package improbableotter.sideprojects.pufftime.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findByUsername(username: String): User?
    fun findByEmail(email: String): User

//    fun save(dto: UserDto): User {
//        val user = User(
//                username = dto.username!!,
//                password = dto.password!!,
//                email = dto.email,
//                role = "USER"
//        )
//        return save(user)
//    }
}