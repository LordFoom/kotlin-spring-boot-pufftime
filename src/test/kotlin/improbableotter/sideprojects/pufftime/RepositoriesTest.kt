package improbableotter.sideprojects.pufftime

import improbableotter.sideprojects.pufftime.grow.Grow
import improbableotter.sideprojects.pufftime.grow.GrowRepository
import improbableotter.sideprojects.pufftime.plant.Plant
import improbableotter.sideprojects.pufftime.plant.Strain
import improbableotter.sideprojects.pufftime.user.User
import improbableotter.sideprojects.pufftime.user.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

private val user_name = "foom"

@DataJpaTest
class RepositoriesTest @Autowired constructor(
        val entityManager: TestEntityManager,
        val userRepository: UserRepository,
        val growRepository: GrowRepository
){
    @Test
    @Order(1)//want this to run first so we can use this user again
    fun `Should successefully register a user`(){
        val foom = User(username= user_name, password="testpassword", email="lordfoom@gmail.com", role="TEST")
        entityManager.persist(foom)
        entityManager.flush()
        val user = userRepository.findByUsername(user_name)
        assertThat(user).isEqualTo(foom)
    }

    @Test
    @Order(2)
    fun `Should create a grow and associate a plant with and then retrieve the grow and plant`(){
        val foom = User(username= user_name, password="testpassword", email="lordfoom@gmail.com", role="TEST")
        entityManager.persist(foom)
        entityManager.flush()
        val user = userRepository.findByUsername(user_name)!!
        val grow = Grow(user = user)
        entityManager.persist(grow)
        val strain = Strain(name = "Pollygoggle")
        entityManager.persist(strain)
        val plant = Plant(user = user, grow = grow, strain = strain)
        entityManager.persist(plant)
        entityManager.flush()

        val growList = growRepository.findAllByUser(user)
        assertThat(growList.size==1)
        var grow_loaded = growList.get(0)

        assertThat(grow_loaded.plants.size==1)
    }
}
