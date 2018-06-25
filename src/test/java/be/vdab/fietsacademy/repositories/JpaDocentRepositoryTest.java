package be.vdab.fietsacademy.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import be.vdab.fietsacademy.entities.Campus;
import be.vdab.fietsacademy.entities.Docent;
import be.vdab.fietsacademy.enums.Geslacht;
import be.vdab.fietsacademy.queryresults.AantalDocentenPerWedde;
import be.vdab.fietsacademy.queryresults.IdEnEmailAdres;
import be.vdab.fietsacademy.valueObjects.Adres;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(JpaDocentRepository.class)
@Sql("/insertDocent.Sql")
@Sql("/insertCampus.sql")
public class JpaDocentRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private JpaDocentRepository repository;

	@Autowired
	private EntityManager manager;

	private static final String DOCENTEN = "docenten";
	private Docent docent;
	private Campus campus;

	@Before
	public void before() {
		campus = new Campus("test", new Adres("test", "test", "test", "test"));
		docent = new Docent("test", "test", BigDecimal.TEN, "test@fietsacademy.be", Geslacht.MAN, campus);

		
}

	public long idVanTestDocent() {
		return super.jdbcTemplate.queryForObject("select id from docenten where voornaam = 'TestM'", Long.class);
	}

	public long idVanTestVrouw() {
		return super.jdbcTemplate.queryForObject("select id from docenten where voornaam = 'TestV'", Long.class);
	}

	@Test
	public void read() {
		long id = idVanTestDocent();
		Docent docent = repository.read(id).get();
		assertEquals("testM", docent.getFamilienaam());
	}

	@Test
	public void readOnbestaandeDocent() {
		assertFalse(repository.read(-1).isPresent());
	}

	@Test
	public void readV() {
		long id = idVanTestVrouw();
		Docent docent = repository.read(id).get();
		assertEquals("testV", docent.getFamilienaam());
	}

	@Test
	public void create() {
		manager.persist(campus);
		int aantalDocenten = super.countRowsInTable(DOCENTEN);
		repository.create(docent);
		manager.flush();
		assertEquals(aantalDocenten + 1, super.countRowsInTable(DOCENTEN));
		assertNotEquals(0, docent.getId());
		assertEquals(1, super.countRowsInTableWhere(DOCENTEN, "id=" + docent.getId()));
//		assertEquals(
//				campus.getId(), 
//				super.jdbcTemplate.queryForObject("select campusid from docenten where id=?", Long.class, docent.getId())
//				.longValue()); 
		assertTrue(campus.getDocenten().contains(docent)); 
	}

	@Test
	public void delete() {
		int aantalDocenten = super.countRowsInTable(DOCENTEN);
		repository.delete(idVanTestDocent());
		manager.flush();
		assertEquals(aantalDocenten - 1, super.countRowsInTable(DOCENTEN));
		assertEquals(0, super.countRowsInTableWhere(DOCENTEN, "id=" + docent.getId()));
	}

	@Test
	public void findAll() {
		int aantalDocenten = super.countRowsInTable(DOCENTEN);
		List<Docent> docenten = repository.findAll();
		assertEquals(aantalDocenten, docenten.size());
		BigDecimal vorigeWedde = BigDecimal.ZERO;
		for (Docent docent : docenten) {
			assertTrue(docent.getWedde().compareTo(vorigeWedde) >= 0);
			vorigeWedde = docent.getWedde();
		}
	}

	@Test
	public void findByWeddeBetween() {
		BigDecimal duizend = BigDecimal.valueOf(1_000);
		BigDecimal tweeduizend = BigDecimal.valueOf(2_000);
		List<Docent> docenten = repository.findByWeddeBetween(duizend, tweeduizend);
		long aantalDocenten = super.countRowsInTableWhere(DOCENTEN, "wedde between 1000 and 2000");
		assertEquals(aantalDocenten, docenten.size());
		docenten.forEach(docent -> {
			assertTrue(docent.getWedde().compareTo(duizend) >= 0);
			assertTrue(docent.getWedde().compareTo(tweeduizend) <= 0);
		});

	}

	@Test
	public void leesEmailAdres() {
		List<String> eAdressen = repository.findEmailAdressen();
		long aantalAdressen = super.jdbcTemplate.queryForObject("select count(distinct emailAdres) from docenten",
				Long.class);
		assertEquals(aantalAdressen, eAdressen.size());

	}

	@Test
	public void leesIdEnEmailAdres() {
		List<IdEnEmailAdres> IdenAdressen = repository.findIdsEnEmailAdressen();
		assertEquals(IdenAdressen.size(), super.countRowsInTable(DOCENTEN));

	}

	@Test
	public void leesMaxWedde() {
		BigDecimal grootste = repository.findGrootsteWedde();
		BigDecimal grootsteTest = super.jdbcTemplate.queryForObject("select max(wedde) from docenten",
				BigDecimal.class);
		assertEquals(0, grootste.compareTo(grootsteTest));

	}

	@Test
	public void findAantalDocentenPerWedde() {
		List<AantalDocentenPerWedde> aantalDocentenPerWedde = repository.findAantalDocentenPerWedde();
		long aantalUniekeWeddes = super.jdbcTemplate.queryForObject("select count(distinct wedde) from docenten",
				Long.class);
		assertEquals(aantalUniekeWeddes, aantalDocentenPerWedde.size());
		long aantalDocentenMetWedde1000 = super.countRowsInTableWhere(DOCENTEN, "wedde = 1000");
		aantalDocentenPerWedde.stream()
				.filter(aantalPerWedde -> aantalPerWedde.getWedde().compareTo(BigDecimal.valueOf(1_000)) == 0)
				.forEach(aantalPerWedde -> assertEquals(aantalDocentenMetWedde1000, aantalPerWedde.getAantal()));
	}

	@Test
	public void algemeneOpslag() {
		int aantalAangepast = repository.algemeneOpslag(BigDecimal.TEN);
		assertEquals(super.countRowsInTable(DOCENTEN), aantalAangepast);
		BigDecimal nieuweWedde = super.jdbcTemplate.queryForObject("select wedde from docenten where id=?",
				BigDecimal.class, idVanTestDocent());
		assertEquals(0, BigDecimal.valueOf(1_100).compareTo(nieuweWedde));
	}

	@Test
	public void bijnamenLezen() {
		Docent docent = repository.read(idVanTestDocent()).get();
		assertEquals(1, docent.getBijnamen().size());
		assertTrue(docent.getBijnamen().contains("test"));
	}

	@Test
	public void bijnaamToevoegen() {
		manager.persist(campus);
		repository.create(docent);
		docent.addBijnaam("test");
		manager.flush();
		assertEquals("test", super.jdbcTemplate.queryForObject("select bijnaam from docentenbijnamen where docentid=?",
				String.class, docent.getId()));
	}

}
