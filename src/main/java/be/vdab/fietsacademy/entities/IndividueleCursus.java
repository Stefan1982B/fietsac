package be.vdab.fietsacademy.entities;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity 
@Table(name = "individuelecursussen")
public class IndividueleCursus extends Cursus {
	private static final long serialVersionUID = 1L;
	private int duurtijd;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getDuurtijd() {
		return duurtijd;
	}


	public IndividueleCursus(UUID id, String naam, int duurtijd) {
		super(id, naam);
		this.duurtijd = duurtijd;
	}

	protected IndividueleCursus() {
		
	}

}
