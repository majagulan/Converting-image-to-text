package prepoznavacAlfabeta;

import prepoznavacAlfabeta.Matrica;

/**
 * U klasi <code>DetektorGranicaObjekta</code> se nalaze metode koje detektuju 
 * granice na objektu na svih strana.
 * Cilj klase je implementacija metoda 
 * kako da razlikujemo piksel od ivice suseda.
 *
 */
public class DetektorGranicaObjekta {
	
	private static int darkValue = 150;
	private static int buffer = 1;
	
	/**
	 * Pronalazi levu i desnu granicu objekta (koji je tamniji od pozadine).
	 * <p>Poziva se u metodi {@link Predvidjanje#slikaUTekst}</p>
	 * 
	 * @param mat
	 * @param startnaPoz start pozicija
	 * @return
	 */
	public static int[] levoDesnoGranica(Matrica mat, int startnaPoz) {
		
		int duzina = mat.getKol();
		int visina = mat.getRed();
		int[] x = new int[2];
		
		boolean nadjenaCrnaTacka = false;
		int pozicija;
		int i;
		
		
		for (pozicija = startnaPoz + 1; pozicija < duzina && !nadjenaCrnaTacka; pozicija++) {
			for (i = 0; i < visina; i++) {
				if (mat.pikseli[i][pozicija][0] < darkValue) {
					nadjenaCrnaTacka = true;
					break;
				}
			}
		}
		
		if (!nadjenaCrnaTacka) 
			return x;
		
		x[0] = pozicija-1-buffer; // x kordinata crne tacke
		
		
		for (; pozicija < duzina && nadjenaCrnaTacka; pozicija++) {
			nadjenaCrnaTacka = false;
			for (i = 0; i < visina; i++) {	
				if (mat.pikseli[i][pozicija][0] < darkValue) {
					nadjenaCrnaTacka = true;
					break;
				}
			}
		}
		
		x[1] = pozicija-1+buffer;
		
		return x;
	}

	/**
	 * Pronalazi gornju i donju granicu objekta (koji je tamniji od pozadine).
	 * <p>Poziva se u metodi {@link Predvidjanje#slikaUTekst}<p>
	 * 
	 * @param mat
	 * @param startnaPoz
	 * @return
	 */
	public static int[] goreDoleGranica(Matrica mat, int startnaPoz) {
		
		int duzina = mat.getKol();
		int visina = mat.getRed();
		int[] y = new int[2];
		
		boolean nadjenaCrnaTacka = false;
		int pozicija, i;
		for (pozicija = startnaPoz + 1; pozicija < visina && !nadjenaCrnaTacka; pozicija++) {
			for (i = 0; i < duzina; i++) {
				if (mat.pikseli[pozicija][i][0] < darkValue) {
					nadjenaCrnaTacka = true;
					break;
				}
			}
		}
		
		if (!nadjenaCrnaTacka) 
			return y;
		
		y[0] = pozicija-1-buffer;
		
		for (; pozicija < visina && nadjenaCrnaTacka; pozicija++) {
			nadjenaCrnaTacka = false;
			for (i = 0; i < duzina; i++) {
				if (mat.pikseli[pozicija][i][0] < darkValue) {
					nadjenaCrnaTacka = true;
					break;
				}
			}
		}
		
		y[1] = pozicija-1+buffer;
		
		return y;
	}
	
	
	/**
	 * Pronalazi granice objekta.
	 * Poziva se u metodi {@link Predvidjanje#slikaUTekst}
	 * 
	 * @param mat
	 * @param startnaPoz
	 * @param stopPoz
	 * @return
	 */
	public static int[] goreGranicaDoleGranica(Matrica mat, int startnaPoz, int stopPoz) {
		
		int sirina = mat.getKol();
		int visina = mat.getRed();
		int[] y = new int[2];
		
		if (stopPoz > visina)
			return y;
		
		boolean nadjenaCrnaTacka = false;
		int pozicija, i;
		for (pozicija = startnaPoz + 1; pozicija < stopPoz && !nadjenaCrnaTacka; pozicija++) {
			for (i = 0; i < sirina; i++) {
				if (mat.pikseli[pozicija][i][0] < darkValue) {
					nadjenaCrnaTacka = true;
					break;
				}
			}
		}
		
		if (!nadjenaCrnaTacka)
			return y;
		
		y[0] = pozicija-1-buffer;
		
		//ovde se razlikuje u odnosu na upDownRange (prvi for)
		nadjenaCrnaTacka = false;
		for (pozicija = stopPoz; pozicija > y[0] && !nadjenaCrnaTacka; pozicija--) {
			for (i = 0; i < sirina; i++) {
				if (mat.pikseli[pozicija][i][0] < darkValue) {
					nadjenaCrnaTacka = true;
					break;
				}
			}
		}
		
		//ovde se razlikuje u odnosu na upDownRange
		y[1] = pozicija+1+buffer;
		
		return y;
	}
}
