package slikeObrada;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;

import prepoznavacAlfabeta.Matrica;

/**
 * U klasi <code>PikseliTrener</code> se nalaze metode koje rade sa slikama.
 *Tokom treniranja CNN automatski uci vrednost svojih filtera 
 */
public class PikseliTrener {
	
	public static final int[] velicinaSlika60 = new int[]{60, 60};
	private int[] velicinaSlike;
	private TezineStandardnihSlika tezineStandardnihSlika; //naucene
	
	/**
	 * Konstruktor bez parametara.
	 * Poziva se u metodi {@link prepoznavacAlfabeta.Predvidjanje#slikaUTekst}
	 */
	public PikseliTrener() {
		
		this.velicinaSlike = velicinaSlika60;
		this.tezineStandardnihSlika = new TezineStandardnihSlika();
	}
	
	
	/**
	 * Konstruktor sa parametrima
	 * Poziva se u metodi {@link prepoznavacAlfabeta.Treniranje#main}
	 * 
	 * @param velSlike
	 */
	public PikseliTrener(int[] velSlike) {
		
		this.velicinaSlike = velSlike;
		this.tezineStandardnihSlika = new TezineStandardnihSlika();
	}
	
	
	/**
	 * Treniranje mreze!
	 * Prosledili smo u metodu putanje svih fajlova (slika), zajedno sa njihovim id-ijevima.
	 * Ovde se nalazi aktivaciona funkcija i obucavanje mreze.
	 * <p>Poziva se u metodi {@link prepoznavacAlfabeta.Treniranje#main}</p>
	 * 
	 * @param putanjeFajlova
	 * @param id_ijevi
	 * @return
	 * @throws Exception
	 */
	public void treniraj(String[] putanjeFajlova, Integer[] id_ijevi) throws Exception {
		
		if (putanjeFajlova.length != id_ijevi.length) {	//provera podataka
			System.out.println("Nekompatibilni podaci.");
			return;
		}
		
		int[] razlike = varijacijeNadji(id_ijevi);
		int tipovi = razlike[razlike.length-1]; 	//tip je svaka razlika
		int tezinaStandardnihSlikaRed = velicinaSlike[0];
		int tezinaStandardnihSlikaKol = velicinaSlike[1];
		
		//kreiram novu matricu tezineStandardnihSlika i posto sam nasla razlike to cuvam u ovoj matrici (tipovi prosledjujemo u konstruktor) 
		TezineStandardnihSlika tezineStandardnihSlika = new TezineStandardnihSlika(tipovi, velicinaSlike);
		
		//setuj id-ijeve u ovu novu matricu sa razlikama
		for (int i = 0; i < tipovi; i++) {
			tezineStandardnihSlika.setId(i, razlike[i]);
		}
		
		
		Matrica mat;
		int tipBroj = 0;
		int indeks = 0;
		
		for (String putanje : putanjeFajlova) {
			mat = new Matrica(putanje, Matrica.CRNOBELO);
			mat = promeniVelicinu(mat, velicinaSlike[1], velicinaSlike[0]);
			mat = uSredini(mat);
			
			double suma = 0;
			int vrednost = 0;
			
			for (int i = 0; i < tipovi; i++) {
				if (tezineStandardnihSlika.getId(i) == id_ijevi[indeks]) {
					tipBroj = i;
					break;
				}
			}
			
			for (int red = 0; red < tezinaStandardnihSlikaRed; red++) {
				for (int kol = 0; kol < tezinaStandardnihSlikaKol; kol++) {
					//aktivaciona funkcija
					suma = (tezineStandardnihSlika.getTezineStandardnihSlika(tipBroj, red, kol) *
							tezineStandardnihSlika.getTezina(tipBroj)) + mat.pikseli[red][kol][0];
					//output axon
					vrednost = (int) suma / (tezineStandardnihSlika.getTezina(tipBroj) + 1);
					
					tezineStandardnihSlika.setTezineStandardnihSlika(tipBroj, red, kol, (short) vrednost);
				}
			}
			
			tezineStandardnihSlika.inkrementirajTezinu(tipBroj);
			System.out.println(indeks + ": slika broj: " + tezineStandardnihSlika.getTezina(tipBroj));	//pokazuje progres
			
			indeks++;
		}
		
		this.tezineStandardnihSlika = tezineStandardnihSlika;
		
		return;
	}
	
	/**
	 * Pronalazi sve moguce varijacije istih slova, cifara i specijalnih znakova.
	 * Prolazi redom kroz foldere sa slovima i trazi slicnosti medju slikama.
	 * <p>Poziva se u metodi {@link PikseliTrener#treniraj}</p>
	 * 
	 * @param id_ijevi
	 * @return
	 */
	private int[] varijacijeNadji(Integer[] id_ijevi) {
		
		int duzina = id_ijevi.length;
		int[] clan = new int[duzina+1];
		int varijacija = 0;
		boolean flagPoklapanje = false;
		
		for (int i = 0; i < duzina; i++) {
			clan[i] = id_ijevi[i];
			flagPoklapanje = false;
			
			for (int j = 0; j < varijacija; j++) { //prolazim kroz prvi folder npr.A
				if (clan[j] == clan[i]) {   //trazim slicnosti medju slikama
					flagPoklapanje = true;
					break;
				}
			}	//kraj drugog for-a
			
			if (!flagPoklapanje) { //ukoliko je slika ista
				clan[varijacija] = clan[i]; //varijacija se povecava, imamo veci broj varijanti slova A
				varijacija++;
			} //nema poklapanja kreni na sledeci folder i novo slovo
		}	//kraj prvog for-a	
		
		clan[duzina] = varijacija; //ubaci novu varijantu slova u postojeci niz 
		
		return clan;
	}
	
	
	/**
	 * Kreiraju se filteri i radi se average pooling. Zamagljuje slike racunanjem 
	 * prosecnog piksela u odnosu na vrednosti susednih. 
	 * <p>Poziva se u metodi {@link PikseliTrener#treniraj}</p>
	 * 
	 * @param mat
	 * @return
	 */
	private Matrica uSredini(Matrica mat) {
		Matrica mat2 = new Matrica(mat.getRed(), mat.getKol(), Matrica.CRNOBELO);
		
		double srednjiPiksel = 0;
		double sumaPikselaPoRedu = 0;
		double sumaPikselaPoKoloniURedu = 0;
		
		int redovi = mat.getRed();
		int kolona = mat.getKol();
		
		for (int x = 0; x < redovi; x++) {
			
			sumaPikselaPoRedu = 0;
			for (int y = 0; y < kolona; y++) {
				sumaPikselaPoRedu = sumaPikselaPoRedu + mat.pikseli[x][y][0];
			}	//izracuna za ceo red sumu piksela
			
			sumaPikselaPoRedu = sumaPikselaPoRedu / kolona;	
			// prosecno piksela po 1 polju (gledamo sumu redova)
			sumaPikselaPoKoloniURedu = sumaPikselaPoKoloniURedu + sumaPikselaPoRedu;
		}
		// AVERAGE POOLING
		srednjiPiksel = sumaPikselaPoKoloniURedu / redovi;	
		//"2"2"4"5"=>12/4=3 srednjiPiksel=3										
		//"4"6"4"5"
		
		int savrseniSrednjiPiksel = 255 / 2; //127.5	
		
		
		int srednjiVrednost = 0;
		int vrednostPiksela = 0;
		
		for (int x = 0; x < redovi; x++) {
			for (int y = 0; y < kolona; y++) {
				vrednostPiksela = (int) mat.pikseli[x][y][0];
				
				srednjiVrednost = (int) (vrednostPiksela * savrseniSrednjiPiksel / srednjiPiksel);
				
				if (srednjiVrednost > 255) 
					srednjiVrednost = 255;
				
				mat2.pikseli[x][y][0] = srednjiVrednost; //matrica posle average pooling-a
			}
		}
		return mat2;
	}

	/**
	 * Ucitavanje naucenog. U metodi koja se poziva {@link PikseliTrener#readFile}
	 * ucitava se mreza iz fajla <code>NaucenoAlfabet.txt</code> koja je obucena u prethodnim funkcijama.
	 * <p>Poziva se u metodi {@link prepoznavacAlfabeta.Predvidjanje#slikaUTekst}</p>
	 * 
	 * @param putanjaFajla
	 * @return
	 */
	public void ucitaj(String putanjaFajla) {
		String mainString = readFile(putanjaFajla);
		
		if(!mainString.substring(0, 25).equals("<TezineStandardnihSlika/>"))  //fajl je ostecen		
			return;
		
		int startIndeks = 0;
		int stopIndeks = 0;
		
		//svi tipovi slika
		startIndeks = mainString.indexOf("<tipovi>", 25);
		stopIndeks = mainString.indexOf("</tipovi>", 25);
		int tipovi = Integer.parseInt(mainString.substring(startIndeks + 8, stopIndeks));
		
		//sve velicine slika
		startIndeks = mainString.indexOf("<velicina>", stopIndeks);
		stopIndeks = mainString.indexOf(",", stopIndeks);
		int sirina = Integer.parseInt(mainString.substring(startIndeks + 10, stopIndeks));
		
		startIndeks = stopIndeks+1;
		stopIndeks = mainString.indexOf("</velicina>", stopIndeks);	
		int visina = Integer.parseInt(mainString.substring(startIndeks, stopIndeks));

		velicinaSlike = new int[]{visina, sirina};
		
		//pozivamo metodu TezineStandardnihSlika sa novim podacima
		
		TezineStandardnihSlika tezineStandardnihSlika = new TezineStandardnihSlika(tipovi, velicinaSlike);
		
		stopIndeks = mainString.indexOf("<podaci>", stopIndeks);
		for (int i = 0; i < tipovi; i++) {
			//id slike
			startIndeks = mainString.indexOf("<id>", stopIndeks);
			stopIndeks = mainString.indexOf("</id>", stopIndeks);
			int id = Integer.parseInt(mainString.substring(startIndeks + 4, stopIndeks));
			tezineStandardnihSlika.setId(i, id);
			
			//broj slike ili tezina
			startIndeks = mainString.indexOf("<tezina>", stopIndeks);
			stopIndeks = mainString.indexOf("</tezina>", stopIndeks);
			int tezina = Integer.parseInt(mainString.substring(startIndeks + 8, stopIndeks));
			tezineStandardnihSlika.setTezina(i, tezina);
			
			startIndeks = mainString.indexOf("<stdSlika>", stopIndeks);
			stopIndeks = startIndeks + 10;
			//stopIndex = mainString.indexOf("</stdImage>", stopIndex);
			//System.out.println(mainString.substring(startIndex+11, stopIndex-1));
			
			short piksel;
			for (int red = 0, kol; red < sirina; red++) {
				for (kol = 0; kol < visina; kol++) {
					//standardni podaci slike
					startIndeks = stopIndeks + 1;
					stopIndeks = mainString.indexOf(',', startIndeks);
					//System.out.println(mainString.substring(startIndex, stopIndex));
					piksel = Short.parseShort(mainString.substring(startIndeks, stopIndeks));
					tezineStandardnihSlika.setTezineStandardnihSlika(i, red, kol, piksel);
					//na zamagljene slike jos primenjuje baferovanje
				}
				
				stopIndeks++;
			}
		}

		this.tezineStandardnihSlika = tezineStandardnihSlika;
		
		return;
	}
	
	/**
	 * Citanje prosledjenog fajla.
	 * Poziva se u metodi {@link PikseliTrener#ucitaj}
	 * 
	 * @param putanjaFajla
	 * @return
	 * @see BufferedReader
	 * @throws Exception ako ne uspe da procita fajl sa hard diska
	 */
	private String readFile(String putanjaFajla) {
		
		String tempString = new String(); // za privremeno skladistenje podataka
		String informacija = new String(); // sadrzi ceo fajl

		try {
			BufferedReader mainBR = new BufferedReader(new FileReader(putanjaFajla)); 

			tempString = mainBR.readLine();
			while (tempString != null) { // citanje korak po korak
				informacija = informacija + tempString + "\n";
				tempString = mainBR.readLine();
			}

			mainBR.close(); // zatvaranje fajla

		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return informacija;
	}
	

	/**
	 * Predikcija
	 * Poziva se u metodi {@link prepoznavacAlfabeta.Predvidjanje#slikaUTekst}
	 * 
	 * @param matTemp2
	 * @return
	 */
	public int predvidi(Matrica matTemp2) {
		
		int id = 0;
		float slicnost = 0;
		
		matTemp2 = promeniVelicinu(matTemp2, velicinaSlike[1], velicinaSlike[0]);
		
		int tipovi = tezineStandardnihSlika.getTipovi();
		for (int i = 0; i < tipovi; i++) {
			float trenutnaSlicnost = poredjenjeMatPiksel1(tezineStandardnihSlika.getTezineStandardnihSlika(i), matTemp2) + 
					poredjenjeMatPiksel2(tezineStandardnihSlika.getTezineStandardnihSlika(i), matTemp2);
			
			//System.out.println(trenutnaSlicnost);	///test
			
			if (trenutnaSlicnost > slicnost) {
				slicnost = trenutnaSlicnost;
				id = tezineStandardnihSlika.getId(i);
			}
		}
		
		if (slicnost < 20){	// ako slika nije prepoznata
			return -1;
		}
		
		return id;
	}
	
	/**
	 * Poredi matricu unete slike sa naucenom matricom koliko su slicne.
	 * Poziva se u metodi {@link PikseliTrener#predvidi}
	 * 
	 * @param mat1
	 * @param mat2
	 * @return
	 */
	private float poredjenjeMatPiksel1(Matrica mat1, Matrica mat2) {
		//piksel1 testPrimer, mat2 naucena
		if (mat1.getRed() != mat2.getRed() || mat1.getKol() != mat2.getKol()) {
			System.out.println("Nekompatibilan podatak!");
			return -1;
		}
		
		float slicnost = 0;
		
		int redovi = mat1.getRed();
		int kolone = mat1.getKol();
		
		float piksel1 = 0;
		float piksel2 = 0;
		float pikselSlicnost = 0;
		float sumaSlicnostiPoRedu = 0;
		float sumaSlicnostiPoKoloniURedu = 0;
		
		for (int red = 0; red < redovi; red++) {
			sumaSlicnostiPoRedu = 0;
			
			for (int kol = 0; kol < kolone; kol++) {
				piksel1 = (float) mat1.pikseli[red][kol][0];
				piksel2 = (float) mat2.pikseli[red][kol][0];
				
				if (piksel1 == piksel2)
					pikselSlicnost = 100;
				else if (piksel1 > piksel2)		//ako je true, povecaj vrednost onog sto je slicno shodno tome kolika je slicnost
					pikselSlicnost = ( (255 + piksel2) / (255 + piksel1) ) * 100;
				else
					pikselSlicnost = (piksel1 / piksel2) * 100;				
				
				sumaSlicnostiPoRedu = sumaSlicnostiPoRedu + pikselSlicnost;
			}
			
			sumaSlicnostiPoRedu = sumaSlicnostiPoRedu / kolone;
			sumaSlicnostiPoKoloniURedu = sumaSlicnostiPoKoloniURedu + sumaSlicnostiPoRedu;
		}
		
		slicnost = sumaSlicnostiPoKoloniURedu / redovi;
		
		
		if (slicnost > 50) 
			slicnost = slicnost - 50;
		else
			slicnost = 50 - slicnost;

		
		return slicnost;
	}
	
	/**
	 * Poredi matricu unete slike sa naucenom matricom koliko su slicne.
	 * Poziva se u metodi {@link PikseliTrener#predvidi}
	 * 
	 * @param mat1
	 * @param mat2
	 * @return
	 */
	private float poredjenjeMatPiksel2(Matrica mat1, Matrica mat2) {
		
		if (mat1.getRed() != mat2.getRed() || mat1.getKol() != mat2.getKol()) {
			System.out.println("Nekompatibilan podatak!");
			return -1;
		}
		
		float slicnost = 0;
		
		int redovi = mat1.getRed();
		int kolone = mat1.getKol();
		
		float piksel1 = 0;
		float piksel2 = 0;
		float pikselSlicnost = 0;
		float sumaSlicnostiPoRedu = 0;
		float sumaSlicnostiPoKoloniURedu = 0;
		
		for (int red = 0; red < redovi; red++) {
			sumaSlicnostiPoRedu = 0;
			
			for(int kol = 0; kol < kolone; kol++) {
				piksel1 = (float) mat1.pikseli[red][kol][0];
				piksel2 = (float) mat2.pikseli[red][kol][0];
				
				if(piksel1 == piksel2)
					pikselSlicnost = 100;
				else if(piksel1 < piksel2)	//koliko su slicni ako je na testSlika manji od onog sa obucene
					pikselSlicnost = (255 - piksel2 + piksel1) / 255 * 100;
				else
					pikselSlicnost = (255 - piksel1 + piksel2) / 255 * 100;
				
				sumaSlicnostiPoRedu = sumaSlicnostiPoRedu + pikselSlicnost;
			}
			
			sumaSlicnostiPoRedu = sumaSlicnostiPoRedu / kolone;
			sumaSlicnostiPoKoloniURedu = sumaSlicnostiPoKoloniURedu + sumaSlicnostiPoRedu;
		}
		
		slicnost = sumaSlicnostiPoKoloniURedu / redovi;
		
		
		if (slicnost > 50) 
			slicnost = slicnost - 50;
		else
			slicnost = 50 - slicnost;
		
		
		return slicnost;
	}
	
	
	/**
	 * Menja velicinu matrice tako sto se prvo matrica pretvori u baferovanu sliku, zatim se promeni velicina na odgovarajucu,
	 * pa se vrsi obrnut proces pretvaranja skalirane baferovane slike u matricu.
	 * Poziva se u metodama {@link PikseliTrener#treniraj} i {@link PikseliTrener#predvidi}
	 * 
	 * @param matrica
	 * @param skaliranaSirina
	 * @param skaliranaVisina
	 * @return moze da ne bude isti tip kao ulaz <code>Matrica</code>
	 * @see BufferedImage
	 * @see Graphics2D
	 */
	public Matrica promeniVelicinu(Matrica matrica, int skaliranaSirina, int skaliranaVisina) {
		
		BufferedImage originalnaBaferovanaSlika = Matrica.matricaUBaferovanuSliku(matrica);
		BufferedImage skaliranaBaferovanaSlika = new BufferedImage(skaliranaSirina, skaliranaVisina, originalnaBaferovanaSlika.getType());
	
		Graphics2D g = skaliranaBaferovanaSlika.createGraphics();
		g.drawImage(originalnaBaferovanaSlika, 0, 0, skaliranaSirina, skaliranaVisina, null); 
		g.dispose();
		
		return Matrica.baferovanaSlikaUMatricu(skaliranaBaferovanaSlika, Matrica.CRNOBELO);
	}

	
	/**
	 * Preuzimanje vrednosti komponente <code>standardnaSlika</code>.
	 * Poziva se u metodi {@link prepoznavacAlfabeta.Treniranje#main}
	 * 
	 * @return standardnaSlika
	 */	
	public TezineStandardnihSlika getTezineStandardnihSlika() {
		return tezineStandardnihSlika;
	}

	
}
