package improbableotter.sideprojects.pufftime.user

import lombok.NoArgsConstructor
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import kotlin.collections.HashSet

@Entity
@Table(name = "users")
data class User(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
//        @Column(nullable = false, unique = true)
        @get:NotBlank
        val username:String,
//        @Column(nullable = false)
        @get:NotBlank
        val password:String,
//        @Column(unique = true)
        @get:Email
        val email:String?,
//        @Column
        @get:NotBlank
        val role:String,
//        @Column
        val created: Date = Date()) {

    fun toDto():UserDto{
        return UserDto(
                id = this.id,
                username = this.username,
                email = this.email

        )
    }
//    override fun toString(): String {
//        return "User id = $id, name = '$username', email = '$email', role = '$role', created = '$created' "
//    }
}

/**
 * Dto to hold info when a new user is registered`
 */
@NoArgsConstructor
data class UserDto(
        var id: Long? = null,
        var username: String?=null,
        var password: String?=null,
        var confirmPassword: String?=null,
        var email: String?=null
)

@Service
class UserDetailsServiceImpl(val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username) ?: throw UsernameNotFoundException(username)
        val grantedAuthorities = HashSet<GrantedAuthority>()
        grantedAuthorities.add(SimpleGrantedAuthority(user.role))
        return User(user.username, user.password, grantedAuthorities)
    }
}