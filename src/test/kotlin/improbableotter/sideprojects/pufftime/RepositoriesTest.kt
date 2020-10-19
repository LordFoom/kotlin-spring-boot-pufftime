package improbableotter.sideprojects.pufftime

import improbableotter.sideprojects.pufftime.user.User
import improbableotter.sideprojects.pufftime.user.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class RepositoriesTest @Autowired constructor(
        val entityManager: TestEntityManager,
        val userRepository: UserRepository
){
    @Test
    fun `Should successefully register a user`(){
        val foom = User(username="foom", password="testpassword", email="lordfoom@gmail.com", role="TEST")
        entityManager.persist(foom)
        entityManager.flush()
        val user = userRepository.findByUsername("foom")
        assertThat(user).isEqualTo(foom)

    }
}
