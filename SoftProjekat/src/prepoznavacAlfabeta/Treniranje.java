package prepoznavacAlfabeta;

import java.io.File;
import java.util.ArrayList;

import prepoznavacAlfabeta.Matrica;
import slikeObrada.TezineStandardnihSlika;
import slikeObrada.PikseliTrener;

/**
 * U klasi <code>Treniranje</code> se treniraju podaci.
 * Ova klasa se prva poziva.
 *
 */
public class Treniranje {
	
	/**
	 * Treniranje podataka se pokrece iz <code>main</code> metode.
	 * Pravi se matrica[45][45], zatim se treniraju podaci u metodi {@link PikseliTrener#treniranje}.
	 * 
	 * @throws Exception 
	 */
	public void mainTreniranje() throws Exception {
			
		int brojFoldera = 79;		//lista slika
		String[] putanjeFajlova = new String[brojFoldera];
		
		for (int i = 1; i <= brojFoldera; i++)
			putanjeFajlova[i-1] = "data/treningPodaci/" + i;
			
		ArrayList<String> listaPutanjaFajlova = new ArrayList<String>();		
		ArrayList<Integer> listaId = new ArrayList<Integer>();			
		
		int id = 1;
		for(String stringPutanje : putanjeFajlova) {
					
			File[] fajlovi = new File(stringPutanje).listFiles();		
			
			for (File fajl : fajlovi) {
				listaPutanjaFajlova.add(fajl.getAbsolutePath());	
				listaId.add(id);		
			}
			
			id++;
		}
		
		String[] putanje = new String[listaPutanjaFajlova.size()];
		listaPutanjaFajlova.toArray(putanje);
		
		Integer[] id_ijevi = new Integer[listaId.size()];
		listaId.toArray(id_ijevi);		//stavljanje rednih brojeva fajlova u listu

		
		
		PikseliTrener pikseliTrener = new PikseliTrener(new int[]{45, 45});
		
		pikseliTrener.treniraj(putanje, id_ijevi); 
		TezineStandardnihSlika tezineStandardnihSlika = pikseliTrener.getTezineStandardnihSlika();
		tezineStandardnihSlika.sacuvajNauceno("data/nauceno/naucenoAlfabet.txt");
		
		///TESTIRANJE!
		for (int i = 0; i < brojFoldera; i++) {		
			Matrica mat = tezineStandardnihSlika.getTezineStandardnihSlika(i);	
			mat.write("data/nauceno/naucenoSlike/naucenoSlika " + i + ".png");
		}
		
		System.out.println("\nOperacija uspesna!!!");
	}
}
