package com.bootcampjava.starwars.repository;

import com.bootcampjava.starwars.model.Jedi;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@ExtendWith({DBUnitExtension.class, SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
public class JediRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JediRepository jediRepository;

    public ConnectionHolder getConnectionHolder() {
        return () -> dataSource.getConnection();
    }

    @Test
    @DataSet("jedi.yml")
    @DisplayName("Should return All Jedis with success")
    public void testFindAll() {

        // execucao
        List<Jedi> listJedis = jediRepository.findAll();

        // assert
        Assertions.assertTrue(listJedis.size() > 0, "Jedi list is not empty");
        Assertions.assertEquals(4, listJedis.size(), "We should have 4 jedis in our database");

    }

    @Test
    @DataSet(cleanBefore = true)
    @DisplayName("Should return an empty list of jedis")
    public void testFindAllReturnEmpty() {

        // execucao
        List<Jedi> listJedis = jediRepository.findAll();

        // assert
        Assertions.assertFalse(listJedis.size() > 0, "There are no jedis on the list");

    }

    @Test
    @DataSet("jedi.yml")
    @DisplayName("Should return a Jedi by id with success")
    public void testFindByIdWithSuccess() {

        // cenario
        Integer id  = 1;
        Jedi expectedJedi = new Jedi(id, "HanSolo", 50, 1);

        // execucao
        Optional<Jedi> jediDB = jediRepository.findById(id);

        // assert
        Assertions.assertTrue(jediDB.isPresent(), "Jedi was not found");
        Assertions.assertEquals(jediDB.get().getVersion(), expectedJedi.getVersion(), "Jedi version must be the same");
        Assertions.assertEquals(jediDB.get().getName(), expectedJedi.getName(), "Jedi name must be the same");
        Assertions.assertEquals(jediDB.get().getStrength(), expectedJedi.getStrength(), "Jedi strength must be the same");
        Assertions.assertEquals(jediDB.get().getId(), expectedJedi.getId(), "Jedi id must be the same");

    }

    @Test
    @DataSet("jedi.yml")
    @DisplayName("Should return empty when Jedi not found")
    public void testFindByIdNotFound() {

        // execucao
        Optional<Jedi> jediNotFound = jediRepository.findById(0);

        // assert
        Assertions.assertFalse(jediNotFound.isPresent(), "Jedi not was found");

    }

    @Test
    @DataSet("jedi.yml")
    @DisplayName("Should return a Jedi saved with success")
    public void testSaveWithSuccess() {

        // cenario
        Jedi jedi = new Jedi(50, "Jedi Name", 40, 1);

        // execucao
        Jedi jediSaved = jediRepository.save(jedi);

        // assert
        Assertions.assertTrue(jediSaved != null && jediSaved.getId() != null, "Jedi was not saved");
        Assertions.assertNotNull(jediSaved, "Jedi should not be null");
        Assertions.assertEquals(jediSaved.getVersion(), jediSaved.getVersion(), "Jedi version must be the same");
        Assertions.assertEquals(jediSaved.getName(), jediSaved.getName(), "Jedi name must be the same");
        Assertions.assertEquals(jediSaved.getStrength(), jediSaved.getStrength(), "Jedi strength must be the same");
        Assertions.assertEquals(jediSaved.getId(), jediSaved.getId(), "Jedi id must be the same");
    }

    @Test
    @DataSet("jedi.yml")
    @DisplayName("Should return a Jedi updated with success")
    public void testUpdateWithSuccess() {

        // cenario

        Integer id = 2;
        Optional<Jedi> jediBeforeUpdate = jediRepository.findById(id);

        Jedi jediUpdating = null;
        if (jediBeforeUpdate.isPresent()) {
            jediUpdating = new Jedi(jediBeforeUpdate.get().getId(),
                                    jediBeforeUpdate.get().getName(),
                                    jediBeforeUpdate.get().getStrength(),
                                    jediBeforeUpdate.get().getVersion());
            jediUpdating.setName("Jedi Name");
        }

        // execucao
        boolean jediUpdated = jediRepository.update(jediUpdating);

        // assert

        Assertions.assertTrue(jediUpdated, "Could not update jedi");

        // Assertions.assertNotEquals(jediBeforeUpdate.get().getName(), jediUpdating.getName(), "Jedis name is same");

        String nameBeforeUpdate = jediBeforeUpdate.map(Jedi::getName).orElse("");
        String nameAfterUpdate = jediRepository.findById(id).map(Jedi::getName).orElse("");
        Assertions.assertNotEquals(nameBeforeUpdate, nameAfterUpdate, "Jedis name is same");

    }

    @Test
    @DataSet("jedi.yml")
    @DisplayName("Should not update a Jedi")
    public void testUpdateFail() {

        // cenario
        Jedi jedi = new Jedi(20, "Jedi Name", 231, 1);

        // execucao
        boolean updateFail = jediRepository.update(jedi);

        // assert
        Assertions.assertFalse(updateFail, "Jedi updated");

    }

    @Test
    @DataSet("jedi.yml")
    @DisplayName("Should delete a Jedi with success into repository")
    public void testDeleteSuccess() {

        // execucao
        Integer id = 1;
        boolean jediDeleted = jediRepository.delete(id);

        // assert
        Assertions.assertTrue(jediDeleted, "Jedi not deleted");

        Optional<Jedi> jedi = jediRepository.findById(id);
        Assertions.assertFalse(jedi.isPresent(), "Jedi exists in the database");
    }

    @Test
    @DataSet("jedi.yml")
    @DisplayName("Shouldn't delete a Jedi when it has an error")
    public void testDeleteFail() {

        // execucao
        Integer id = 99;
        boolean jediNotDeleted = jediRepository.delete(id);

        // assert
        Assertions.assertFalse(jediNotDeleted, "Jedi deleted");

        Optional<Jedi> jedi = jediRepository.findById(id);
        Assertions.assertTrue(jedi.isEmpty(), "Jedi not exists in the database");
    }

}
