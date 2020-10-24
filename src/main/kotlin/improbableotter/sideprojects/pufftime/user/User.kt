package improbableotter.sideprojects.pufftime.user

import improbableotter.sideprojects.pufftime.grow.Grow
import improbableotter.sideprojects.pufftime.plant.Plant
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
import javax.validation.constraints.NotEmpty
import kotlin.collections.HashSet

@Entity
@Table(name = "user")
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
        @OneToMany(mappedBy="user", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
        val plants:Set<Plant> = emptySet(),
        @OneToMany(mappedBy="user", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
        val grows:Set<Grow> = emptySet(),
//        @Column
        @get:NotBlank
        val role:String,
//        @Column
        val created: Date = Date()) {

    fun toDto():UserDto= UserDto(
                id = this.id,
                username = this.username,
                email = this.email
        )


    companion object{
        fun fromDto(dto:UserDto)= User(
                id=dto.id,
                username = dto.username!!,
                password = dto.password!!,
                email = dto.email!!,
                role = dto.role!!
        )
    }
//    override fun toString(): String {
//        return "User id = $id, name = '$username', email = '$email', role = '$role', created = '$created' "
//    }
}

/**
 * Dto to hold info when a new user is registered`
 */
//@NoArgsConstructor
data class UserDto(
        var id: Long? = null,
        @get:NotEmpty
        var username: String?=null,
        @get:NotEmpty
        var password: String?=null,
        @get:NotEmpty
        var confirmPassword: String?=null,
        @get:Email
        var email: String?=null,
        var role: String?="USER",
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