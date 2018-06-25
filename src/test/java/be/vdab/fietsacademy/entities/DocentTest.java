package be.vdab.fietsacademy.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import be.vdab.fietsacademy.enums.Geslacht;
import be.vdab.fietsacademy.valueObjects.Adres;

public class DocentTest {
	private Docent docent;
	private Campus campus1;
	private Docent docent2;
	private Docent nogEensDocent;
	private Campus campus2;

	@Before
	public void before() {
		campus1 = new Campus("test", new Adres("test", "test", "test", "test"));
		campus2 = new Campus("test2", new Adres("test2", "test2", "test2", "test2"));
		docent = new Docent("test", "test", ORIGINELEWEDDE, "test@fietsacademy.be", Geslacht.MAN, campus1);
		docent2 = new Docent("test2", "test2", ORIGINELEWEDDE, "test2@fietsacademy.be", Geslacht.MAN, campus1);
		nogEensDocent = new Docent("test", "test", ORIGINELEWEDDE, "test@fietsacademy.be", Geslacht.MAN, campus1);
	}

	private final static BigDecimal ORIGINELEWEDDE = BigDecimal.valueOf(100);

	@Test
	public void opslag() {
		docent.opslag(BigDecimal.TEN);
		assertEquals(0, BigDecimal.valueOf(110).compareTo(docent.getWedde()));
	}

	@Test(expected = NullPointerException.class)
	public void opslagMetNull() {
		docent.opslag(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void opslagMetNegatiefGetal() {
		docent.opslag(BigDecimal.valueOf(-1));
		assertEquals(0, ORIGINELEWEDDE.compareTo(docent.getWedde()));
	}

	@Test
	public void bijnaamToevoegen() {
		docent.addBijnaam("testje");
		assertEquals(1, docent.getBijnamen().size());
		assertTrue(docent.getBijnamen().contains("testje"));
	}

	@Test
	public void bijnaamVerwijderen() {
		docent.addBijnaam("test1");
		assertTrue(docent.removeBijnaam("test1"));
		assertTrue(docent.getBijnamen().isEmpty());
	}

	@Test
	public void NieuweDocentHeeftGeenBijnaam() {
		assertTrue(docent.getBijnamen().isEmpty());
	}

	@Test
	public void tweeKeerDezelfdeBijnaamToevoegenKanNiet() {
		docent.addBijnaam("test");
		assertFalse(docent.addBijnaam("test"));
		assertEquals(1, docent.getBijnamen().size());
	}

	@Test(expected = NullPointerException.class)
	public void nullAlsBijnaamToevoegenKanNiet() {
		docent.addBijnaam(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void eenLegeBijnaamToevoegenKanNiet() {
		docent.addBijnaam("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void eenBijnaamMetEnkelSpatiesToevoegenKanNiet() {
		docent.addBijnaam(" ");
	}

	@Test
	public void meerdereDocentenKunnenTotDezelfdeCampusBehoren() {
		assertTrue(campus1.getDocenten().contains(docent));
		assertTrue(campus1.getDocenten().contains(docent2));
	}

	@Test
	public void docentenZijnGelijkAlsHunEmailAdressenGelijkZijn() {
		assertEquals(docent, nogEensDocent);
	}

	@Test
	public void docentenZijnVerschillendAlsHunEmailAdressenVerschillen() {
		assertNotEquals(docent, docent2);
	}

	@Test
	public void eenDocentVerschiltVanNull() {
		assertNotEquals(docent, null);
	}

	@Test
	public void eenDocentVerschiltVanEenAnderTypeObject() {
		assertNotEquals(docent, "");
	}

	@Test
	public void gelijkeDocentenGevenDezelfdeHashCode() {
		assertEquals(docent.hashCode(), nogEensDocent.hashCode());
	}

	@Test
	public void docent1KomtVoorInCampus1() {
		assertEquals(docent.getCampus(), campus1);
		assertEquals(2, campus1.getDocenten().size());
		assertTrue(campus1.getDocenten().contains(docent));
	}

	@Test
	public void docent1VerhuistNaarCampus2() {
		docent.setCampus(campus2);
		assertEquals(docent.getCampus(), campus2);
		assertEquals(1, campus1.getDocenten().size());
		assertEquals(1, campus2.getDocenten().size());
		assertTrue(campus2.getDocenten().contains(docent));
	}

}
