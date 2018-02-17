package slikeObrada;

import java.io.FileWriter;
import java.io.IOException;

import prepoznavacAlfabeta.Matrica;

/**
 * U klasi <code>TezinaStandardnihSlika</code> nalaze se sve metode sa rad sa naucenim podacima. 
 *
 */
public class TezineStandardnihSlika {
	
	private int tipovi; //tipovi naucenih slika
	private int[] velicina;
	private int[] id_ijevi;
	private int[] tezine;
	private Matrica[] tezineStandardnihSlika;
	
	/**
	 * Konstruktor sa parametrima. Kreiramo naucenu matricu kao crno-belu.
	 * 
	 * @param tip
	 * @param vel
	 */
	public TezineStandardnihSlika(int tip, int[] vel) {
		this.tipovi = tip;
		this.velicina = vel;
		this.id_ijevi = new int[tip];
		this.tezine = new int[tip];
		this.tezineStandardnihSlika = new Matrica[tip];
		for (int i = 0; i < tip; i++)
			tezineStandardnihSlika[i] = new Matrica(vel[0], vel[1], Matrica.CRNOBELO);
	}
	
	/**
	 * Konstruktor bez parametara 
	 */
	public TezineStandardnihSlika() {
		
		this.tipovi = 0;
		this.velicina = new int[]{0, 0};
		this.id_ijevi = new int[tipovi];
		this.tezine = new int[tipovi];
		this.tezineStandardnihSlika = new Matrica[tipovi];
		for (int i = 0; i < tipovi; i++)
			tezineStandardnihSlika[i] = new Matrica(velicina[0], velicina[1], Matrica.CRNOBELO);
	}
	
	/**
	 * Generisanje fajla <code>NaucenoAlfabet.txt</code>
	 * Poziva se u metodama {@link TezineStandardnihSlika#main} i {@link TezineStandardnihSlika#sacuvajNauceno}
	 * 
	 * @return
	 */
	public String obradaPocetak() {
		
		int redovi = this.velicina[0];
		int kolone = this.velicina[1];
		String sadrzaj = "<TezineStandardnihSlika/>\n<tipovi>" + tipovi + "</tipovi>\n<velicina>" +
				redovi + "," + kolone + "</velicina>\n<podaci>";
		
		String tipSlike = "";
		String linija = "";
		for (int i = 0; i < tipovi; i++) {	//za svaki znak
			tipSlike = "\n<id>" + id_ijevi[i] + "</id>\n" +
						"<tezina>" + tezine[i] + "</tezina>\n" +
						"<stdSlika>\n";
			
			for (int red = 0; red < redovi; red++) {
				linija = "";
				for (int kol = 0; kol < kolone; kol++)
					linija = linija + (short)tezineStandardnihSlika[i].pikseli[red][kol][0] + ",";
				
				tipSlike = tipSlike + linija + "\n";
			}
			
			sadrzaj = sadrzaj + tipSlike + "</stdSlika>\n";
		}	
		
		sadrzaj = sadrzaj + "</podaci>";
		
		return sadrzaj;
	}
	
	/**
	 * Cuvanje naucenog.
	 * Poziva se u metodi {@link TezineStandardnihSlika#main}
	 * 
	 * @param filePath
	 * @throws IOException ako ne uspe da uskladisti podatke na hard disk
	 * @see FileWriter
	 */
	public void sacuvajNauceno(String filePath) {
		
		System.out.println("\nSnima se nauceno...");	//notifikacija
		
		try {
			FileWriter fileWriter = new FileWriter(filePath);
			fileWriter.write(obradaPocetak());
			fileWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Preuzimanje vrednosti komponente <code>tipovi</code>
	 * Poziva se u metodi {@link PikseliTrener#predvidi}
	 * 
	 * @return tipovi
	 * 
	 */
	public int getTipovi(){
		return tipovi;
	}

	/**
	 * Preuzimanje vrednosti komponente <code>id_ijevi</code>
	 * Poziva se u metodama {@link PikseliTrener#predvidi(Matrica)} i {@link PikseliTrener#treniraj}
	 * 
	 * @param indeks
	 * @return
	 */
	public int getId(int indeks) {
		
		if (indeks < tipovi)
			return id_ijevi[indeks];
		else
			return -1;
	}
	
	/**
	 * Postavljanje nove vrednosti komponente <code>id_ijevi</code>
	 * Poziva se u metodama {@link PikseliTrener#ucitaj} i {@link PikseliTrener#treniraj}
	 * 
	 * @param indeks
	 * @param id
	 */
	public void setId(int indeks, int id){
		
		if (indeks < tipovi)
			id_ijevi[indeks] = id;
	}
	
	/**
	 * Preuzimanje vrednosti komponente <code>tezine[]</code>
	 * Poziva se u metodi {@link PikseliTrener#treniraj}
	 *
	 * @param indeks
	 * @return
	 */
	public int getTezina(int indeks) {
		
		if (indeks < tipovi)
			return tezine[indeks];
		else
			return -1;
	}
	
	/**
	 * Postavljanje nove vrednosti komponente <code>tezine</code>.
	 * Poziva se u metodi {@link PikseliTrener#ucitaj} 
	 * 
	 * @param indeks
	 * @param tezina
	 */
	public void setTezina(int indeks, int tezina){
		
		if (indeks < tipovi)
			tezine[indeks] = tezina;
	}
	
	/**
	 * Inkrementiranje tezine.
	 * Poziva se u metodi {@link PikseliTrener#train}
	 * 
	 * @param indeks
	 */
	public void inkrementirajTezinu(int indeks) {
		
		if (indeks < tipovi)
			tezine[indeks]++;
	}

	/**
	 * Postavljanje nove vrednosti komponente <code>standardneSlike[].pikseli[][][]</code>. 
	 * Poziva se u metodama {@link PikseliTrener#ucitaj} i {@link PikseliTrener#treniraj}
	 * 
	 * @param indeks
	 * @param red
	 * @param kol
	 * @param piksel
	 * @return
	 */
	public void setTezineStandardnihSlika(int indeks, int red, int kol, short piksel) {
		
		if (indeks < tipovi)
			tezineStandardnihSlika[indeks].pikseli[red][kol][0] = piksel;
	}

	
	/**
	 * Preuzimanje vrednosti komponente <code>standardneSlike</code>
	 * Poziva se u metodi {@link PikseliTrener#treniraj}
	 * 
	 * @param indeks
	 * @param red
	 * @param kol
	 * @return
	 */
	public short getTezineStandardnihSlika(int indeks, int red, int kol) {
		
		if (indeks < tipovi && red < velicina[0] && kol < velicina[1])
			return (short) tezineStandardnihSlika[indeks].pikseli[red][kol][0];
		else
			return 0;
	}
	
	/**
	 * Preuzimanje vrednosti komponente <code>standardneSlike</code>
	 * Poziva se u metodi {@link Treniranje#main} i {@link PikseliTrener#predvidi}
	 * 
	 * @param indeks
	 * @return
	 */
	public Matrica getTezineStandardnihSlika(int indeks) {
		
		if(indeks < tipovi)
			return tezineStandardnihSlika[indeks];
		else
			return null;
	}

}
