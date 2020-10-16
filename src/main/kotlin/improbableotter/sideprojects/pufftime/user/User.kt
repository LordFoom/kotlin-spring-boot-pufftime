package improbableotter.sideprojects.pufftime.user

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.*
import kotlin.collections.HashSet

@Entity
@Table(name = "users")
class User(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = -1,
        @Column(nullable = false, unique = true)
        var username: String = "",
        @Column(nullable = false)
        var password: String = "",
        @Column(unique = true)
        var email: String = "",
        @Column
        var role: String = "USER",
        @Column
        var created: Date = Date()) {
    override fun toString(): String {
        return "User id = $id, name = '$username', email = '$email', role = '$role', created = '$created' "
    }
}

/**
 * Dto to hold info when a new user is registered`
 */
data class UserRegistrationDto(
        val username: String,
        val password: String,
        val confirmPassword: String,
        val email: String
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