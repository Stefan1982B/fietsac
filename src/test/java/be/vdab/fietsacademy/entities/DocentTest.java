package be.vdab.fietsacademy.entities;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import be.vdab.fietsacademy.enums.Geslacht;

public class DocentTest {
	private Docent docent;
	
	@Before
	public void before() {
		docent = new Docent("test", "test", ORIGINELEWEDDE, "test@fietsacademy.be", Geslacht.MAN); 
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

}
