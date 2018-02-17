package prepoznavacAlfabeta;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * U klasi <code>Matrica</code> se nalaze metode koje rade sa matricama.
 *
 */
public class Matrica {
	
	public static final int CRNOBELO = 1;
	
	/**
	 * Slike su matrice piksela, predstavljaju se svojom sirinom, visinom i vrednostima piksela.
	 * Za slike u boji imaju svaka od tri kanala (RGB) i oni se predstavljaju vrednostima piksela u opsegu 0-255.
	 * RGB struktura ima 3D strukturu i predstavlja <b>ulaznu zapreminu</b>.
	 */
	public int[][][] pikseli;

	/**
	 * Konstruktor sa parametrima.
	 * 
	 * @param red sirina piksela
	 * @param kol visina piksela
	 * @param tip vrednost piksela
	 */
	public Matrica(int red, int kol, int tip) {
		
		if (red < 1) {
			red = 1;
		}

		if (kol < 1) {
			kol = 1;
		}

		tip = 1;
		

		this.pikseli = new int[red][kol][tip];
	}

	
	/**
	 * Konstruktor sa parametrima koji cita fajl sliku.
	 * 
	 * @param putanjaSlike
	 * @param tip
	 * @throws Exception ako ne uspe da procita fajl sa hard diska
	 * @see BufferedImage
	 */
	public Matrica(String putanjaSlike, int tip) {
		
		BufferedImage bufferedImage = null;

		try {
			bufferedImage = ImageIO.read(new File(putanjaSlike));
		} catch (Exception arg) {
			arg.printStackTrace();
		}

		this.pikseli = baferovanaSlikaUMatricu(bufferedImage, tip).pikseli;
	}

	
	/**
	 * Preuzimanje vrednosti komponente <code>pikseli.length</code>
	 * 
	 * @return
	 */
	public int getRed() {
		return this.pikseli.length;
	}

	
	/**
	 * Preuzimanje vrednosti komponente <code>pikseli[0].length</code>
	 * 
	 * @return
	 */
	public int getKol() {
		return this.pikseli[0].length;
	}
	
	
	/**
	 * Konverzija baferovane slike u matricu. Obrada unetih podataka na osnovu kojih se obucava mreza.
	 * <p>Poziva se u metodama {@link Matrica#Matrica(String, int)} i {@link Matrica#promeniVelicinu}</p>
	 * 
	 * @param baferovanaSlika
	 * @param tip
	 * @return
	 * @see BufferedImage
	 */
	public static Matrica baferovanaSlikaUMatricu(BufferedImage baferovanaSlika, int tip) {
		//html,css kod ENCODE
		int red = baferovanaSlika.getHeight();	//visina
		int kol = baferovanaSlika.getWidth();	//sirina
		tip = 1;

		Matrica matrica = new Matrica(red, kol, tip);
		int i;
		int j;
		int rgb;
		
		for (i = 0; i < red; ++i) {
			for (j = 0; j < kol; ++j) {
				rgb = baferovanaSlika.getRGB(j, i);
				matrica.pikseli[i][j] = new int[]{rgb >> 8 & 255};
			}
		}
		
		return matrica;
	}

	
	/**
	 * Konverzija matrice u baferovanu sliku. Obrada unetih podataka na osnovu kojih se obucava mreza.
	 * <p>Potrebno je da se dobiju vrednosti za crvenu, zelenu i plavu u rasponu od 0 do 255 iz slike
	 * sa metodom {@link java.awt.image.BufferedImage#getRGB(int x, int y)}, 
	 * a zatim se one pretvaraju u heksadecimalnu niz koji se može koristiti za odredjivanje boje u html/css kodu.</p>
	 * <p>Poziva se u metodama {@link Matrica#promeniVelicinu} i {@link Matrica#write}</p>
	 * 
	 * @param matrica
	 * @return
	 * @see BufferedImage
	 */
	public static BufferedImage matricaUBaferovanuSliku(Matrica matrica) {
		//DECODE
		BufferedImage baferovanaSlika = new BufferedImage(matrica.pikseli[0].length, matrica.pikseli.length, 6);
		int i;
		int j;
		int[] rgb;
		int boja;
		
		for (i = 0; i < matrica.pikseli.length; ++i) {
			for (j = 0; j < matrica.pikseli[0].length; ++j) {
				rgb = matrica.pikseli[i][j];
				boja = -16777216 | rgb[0] << 16 | rgb[0] << 8 | rgb[0];
				baferovanaSlika.setRGB(j, i, boja);
			}
		}

		return baferovanaSlika;
	}
	
	
	/**
	 * Kreiranje filtera Cernel.
	 * Poziva se u metodi {@link Predvidjanje#slikaUTekst}
	 * 
	 * @param redStart
	 * @param redKraj
	 * @param kolStart
	 * @param kolKraj
	 * @return
	 */
	public Matrica subMatrica(int redStart, int redKraj, int kolStart, int kolKraj) {
		
		if (redStart >= 0 && kolStart >= 0 && redKraj <= this.getRed() && kolKraj <= this.getKol() && 
				redKraj > redStart && kolKraj > kolStart) {
			int red = redKraj - redStart;
			int kol = kolKraj - kolStart;
			Matrica matrica = new Matrica(red, kol, Matrica.CRNOBELO);

			for (int i = 0; i < red; ++i) {
				for (int j = 0; j < kol; ++j) {
					matrica.pikseli[i][j] = (int[]) this.pikseli[redStart + i][kolStart + j].clone();
				}
			}

			return matrica;
		} 
		else {
			return null;
		}
	}
	
	
	/**
	 * Skladistenje slike.
	 * Poziva se u metodi {@link Treniranje#main}
	 * 
	 * @param putanjaSlike
	 * @throws Exception ako ne procita sliku
	 * @see BufferedImage
	 * @see ImageIO
	 * @see File
	 */
	public void write(String putanjaSlike) {
		BufferedImage bufferedImage = matricaUBaferovanuSliku(this);

		try {
			ImageIO.write(bufferedImage, putanjaSlike.substring(putanjaSlike.lastIndexOf(46) + 1), new File(putanjaSlike));
		} catch (Exception arg) {
			arg.printStackTrace();
		}

	}
	
}
