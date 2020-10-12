package improbableotter.sideprojects.pufftime.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import javax.persistence.*

@Entity
@Table(name = "users")
class User(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1,
        @Column(nullable = false, unique = true)
        var username: String = "",
        @Column(nullable = false)
        var password: String = "",
        @Column(unique = true)
        var email: String = "",
        @Column
        var role: String = "USER") {
    override fun toString(): String {
        return "User id = $id, name = '$username', email = '$email', role = '$role'"
    }
}

@Service
class UserDetailsServiceImpl(val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username) ?: throw UsernameNotFoundException(username)
        val grantedAuthorities = HashSet<GrantedAuthority>()
        grantedAuthorities.add(SimpleGrantedAuthority(user.role))
        return User(user.username, user.password, grantedAuthorities)
    }
}