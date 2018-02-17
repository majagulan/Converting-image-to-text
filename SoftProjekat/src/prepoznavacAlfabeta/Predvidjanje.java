package prepoznavacAlfabeta;

import java.io.*;

import GUI.MyFrame;
import prepoznavacAlfabeta.Matrica;
import slikeObrada.PikseliTrener;


/**
 * U klasi <code>Predvidjanje</code> se izvrsava prepoznavanje teksta sa slike.
 * Ova klasa se poziva nakon sto pozovemo klasu {@link Treniranje}
 *
 */
public class Predvidjanje {
	
	public String konvTekst = "";

	/**
	 * Preuzimanje vrednosti komponente <code>konvTekst</code>
	 * 
	 * @return
	 */
	public String getKonvTekst() {
		return konvTekst;
	}

	/**
	 * Postavljanje nove vrednosti komponente <code>konvTekst</code>
	 * 
	 * @param str
	 */
	public void setKonvTekst(String konvTekst) {
		this.konvTekst = konvTekst;
	}


	/**
	 * Testiranje podataka se pokrece iz <code>main</code> metode
	 * 
	 * @throws Exception
	 * @throws IOException ako ne uspe da uskladisti podatke na hard disk
	 * @see FileWriter
	 */
	public void mainPredvidjanje() throws Exception {
		
		String testSlika = MyFrame.getInstance().getSlikaPutanja();
		String nauceno = "data/nauceno/NaucenoAlfabet.txt";
		String izlaz = testSlika + "-tekst.txt";
			
		String str = slikaUTekst(testSlika, nauceno);
		setKonvTekst(str);
		
		System.out.println("\n\n ***Tekst sa izabrane slike: " + str + "\n\n");
		
		//skladiste se rezultati na hard disk
		try {
			FileWriter writer = new FileWriter(new File(izlaz));
			writer.write(str);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Operacija uspesna!!!");
	}
	
	
	/**
	 * Koristi se za samo konvertovanje izabrane slike u tekst.
	 * U metodi se implementira nacin na koji se pomera filter (submatrica) na test slici.
	 * Postavlja se filter na matricu (ulaznu sliku) velicine od a[0]-3 do a[1]+3 -> koji je red,
	 * a kolona od 0 do koliko ima kolona testSlika.
	 * <p>Poziva se u metodi {@link Predvidjanje#main}</p>
	 * 
	 * @param putanja
	 * @param iskustvo
	 * @return
	 * @throws Exception 
	 */
	public static String slikaUTekst(String putanja, String iskustvo) throws Exception {
		
		String string = "";
		
		System.out.println("Ucitava se nauceno...");
		PikseliTrener pikseliTrener = new PikseliTrener();
		pikseliTrener.ucitaj(iskustvo);

		Matrica matSlika = new Matrica(putanja, Matrica.CRNOBELO);
		
		Matrica matTemp;
		int[] a = new int[2];
		
		int vertikalniProstor = 3;
		int i = 1;
		while (true) {
			
			a = DetektorGranicaObjekta.goreDoleGranica(matSlika, a[1]);
			if (a[1] == 0)
				break;
			//subMatrica(int redStart, int redKraj, int kolStart, int kolKraj) 
			matTemp = matSlika.subMatrica(a[0]-vertikalniProstor, a[1]+vertikalniProstor, 0, matSlika.getKol());
			
			
			Matrica matTemp2;
			int x[] = new int[2];
			int y[] = new int[2];
			
			while (true) {
				x = DetektorGranicaObjekta.levoDesnoGranica(matTemp, x[1]);
				if (x[1] == 0)
					break;
				
				matTemp2 = matTemp.subMatrica(0, matTemp.getRed(), x[0], x[1]);
				
				
				y = DetektorGranicaObjekta.goreGranicaDoleGranica(matTemp2, 0, matTemp2.getRed()-1);
				if (y[1] == 0)
					break;
				
				matTemp2 = matTemp2.subMatrica(y[0]-vertikalniProstor, y[1]+vertikalniProstor, 0, matTemp2.getKol());
				
				int f = pikseliTrener.predvidi(matTemp2);	//predvidi koje slovo je na slici (redom predvidja dogod ima detektovanih slova)
				
				string = string + getVrednostOd(f);
				System.out.println("Radi se na alfabetu >> " + i++);	
			}
		}
		
		return string;
	}
	
	/**
	 * Preuzimaju se vrednosti svih karaktera sa kojima se radi u programu
	 * (mala i velika slova, cifre i specijalni znakovi).
	 * <p>Poziva se u metodi {@link Predvidjanje#slikaUTekst}</p>
	 * 
	 * @param f
	 * @return
	 */
 	public static char getVrednostOd(int f) {
 		
		char ch;
		
		switch(f) {
		
		// mala slova
		
		case 1:
			ch='a';
			break;
		case 2:
			ch='b';
			break;
		case 3:
			ch='c';
			break;
		case 4:
			ch='d';
			break;
		case 5:
			ch='e';
			break;
		case 6:
			ch='f';
			break;
		case 7:
			ch='g';
			break;
		case 8:
			ch='h';
			break;
		case 9:
			ch='i';
			break;
		case 10:
			ch='j';
			break;
		case 11:
			ch='k';
			break;
		case 12:
			ch='l';
			break;
		case 13:
			ch='m';
			break;
		case 14:
			ch='n';
			break;
		case 15:
			ch='o';
			break;
		case 16:
			ch='p';
			break;
		case 17:
			ch='q';
			break;
		case 18:
			ch='r';
			break;
		case 19:
			ch='s';
			break;
		case 20:
			ch='t';
			break;
		case 21:
			ch='u';
			break;
		case 22:
			ch='v';
			break;
		case 23:
			ch='w';
			break;
		case 24:
			ch='x';
			break;
		case 25:
			ch='y';
			break;
		case 26:
			ch='z';
			break;
			
		// velika slova
			
		case 27:
			ch='A';
			break;
		case 28:
			ch='B';
			break;
		case 29:
			ch='C';
			break;
		case 30:
			ch='D';
			break;
		case 31:
			ch='E';
			break;
		case 32:
			ch='F';
			break;
		case 33:
			ch='G';
			break;
		case 34:
			ch='H';
			break;
		case 35:
			ch='I';
			break;
		case 36:
			ch='J';
			break;
		case 37:
			ch='K';
			break;
		case 38:
			ch='L';
			break;
		case 39:
			ch='M';
			break;
		case 40:
			ch='N';
			break;
		case 41:
			ch='O';
			break;
		case 42:
			ch='P';
			break;
		case 43:
			ch='Q';
			break;
		case 44:
			ch='R';
			break;
		case 45:
			ch='S';
			break;
		case 46:
			ch='T';
			break;
		case 47:
			ch='U';
			break;
		case 48:
			ch='V';
			break;
		case 49:
			ch='W';
			break;
		case 50:
			ch='X';
			break;
		case 51:
			ch='Y';
			break;
		case 52:
			ch='Z';
			break;
			
		// cifre
			
		case 53:
			ch='1';
			break;
		case 54:
			ch='2';
			break;
		case 55:
			ch='3';
			break;
		case 56:
			ch='4';
			break;
		case 57:
			ch='5';
			break;
		case 58:
			ch='6';
			break;
		case 59:
			ch='7';
			break;
		case 60:
			ch='8';
			break;
		case 61:
			ch='9';
			break;
		case 62:
			ch='0';
			break;
			
		// specijalni znakovi
			
		case 63:
			ch=',';
			break;
		case 64:
			ch=';';
			break;
		case 65:
			ch=':';
			break;
		case 66:
			ch='?';
			break;
		case 67:
			ch='!';
			break;
		case 68:
			ch='.';
			break;
		case 69:
			ch='@';
			break;
		case 70:
			ch='#';
			break;
		case 71:
			ch='$';
			break;
		case 72:
			ch='%';
			break;
		case 73:
			ch='&';
			break;
		case 74:
			ch='(';
			break;
		case 75:
			ch=')';
			break;
		case 76:
			ch='{';
			break;
		case 77:
			ch='}';
			break;
		case 78:
			ch='[';
			break;
		case 79:
			ch=']';
			break;
		
		
			
		default:
			ch='~';
		}
		
		return ch;
	}

}
