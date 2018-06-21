package be.vdab.fietsacademy.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import be.vdab.fietsacademy.enums.Geslacht;
import be.vdab.fietsacademy.valueObjects.Adres;

public class DocentTest {
	private Docent docent;
	private Campus campus1;

	@Before
	public void before() {
		campus1 = new Campus("test", new Adres("test", "test","test","test"));
		docent = new Docent("test", "test", ORIGINELEWEDDE, "test@fietsacademy.be", Geslacht.MAN, campus1);
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

}
