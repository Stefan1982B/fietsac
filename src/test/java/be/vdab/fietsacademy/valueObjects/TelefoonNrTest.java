package be.vdab.fietsacademy.valueObjects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class TelefoonNrTest {

	private TelefoonNr nummer1, nogEensNummer1, nummer2;

	@Before
	public void before() {
		nummer1 = new TelefoonNr("1", false, "");
		nogEensNummer1 = new TelefoonNr("1", false, "");
		nummer2 = new TelefoonNr("2", false, "");
	}

	@Test
	public void telefoonNrsZijnGelijkAlsHunNumersGelijkZijn() {
		assertEquals(nummer1, nogEensNummer1);
	}

	@Test
	public void telefooNrsZijnVerschillendAlsHunNummersVerschillen() {
		assertNotEquals(nummer1, nummer2);
	}

	@Test
	public void eenTelefoonNrVerschiltVanNull() {
		assertNotEquals(nummer1, null);
	}

	@Test
	public void eenTelefoonNrVerschiltVanEenAnderTypeObject() {
		assertNotEquals(nummer1, "");
	}

	@Test
	public void gelijkeTelefoonNrsGevenDezelfdeHashCode() {
		assertEquals(nummer1.hashCode(), nogEensNummer1.hashCode());
	}
}
