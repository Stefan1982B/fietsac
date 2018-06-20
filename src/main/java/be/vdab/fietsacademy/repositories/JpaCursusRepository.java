package be.vdab.fietsacademy.repositories;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import be.vdab.fietsacademy.entities.Cursus;

@Repository
public class JpaCursusRepository implements CursusRepository {
	private final EntityManager manager;

	JpaCursusRepository(EntityManager manager) {
		this.manager = manager;
	}

	@Override
	public Optional<Cursus> read(String id) {
		return Optional.ofNullable(manager.find(Cursus.class, id));
	}

	@Override
	public void create(Cursus cursus) {
		manager.persist(cursus);
	}
}
