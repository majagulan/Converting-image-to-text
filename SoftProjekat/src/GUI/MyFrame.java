package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import prepoznavacAlfabeta.Predvidjanje;
import prepoznavacAlfabeta.Treniranje;


/**
 * U klasi <code>MyFrame</code> se nalazi glavni prozor aplikacije 
 * iz kog je moguce obuciti program i testirati ga.
 * <p>Klasa nasledjuje {@link JFrame}.</p>
 *
 */
@SuppressWarnings("serial")
public class MyFrame extends JFrame {

	private static MyFrame instance = null;
	
	private JFrame optionPaneFrame;
	
	private JPanel obuciPanel;
	private JPanel slikaPanel;
	private JPanel konvPanel;
	private JPanel tekstPanel;
	
	private JLabel obucavanje;
	private JLabel infoObuci;
	private JLabel izaberiSliku;
	private JLabel konvertovanje;
	private JLabel labelaSlike;
	private JLabel konvertovanTekst;
	private JLabel infoTekst;
	
	private JButton obuci;
	private JButton dodajSliku;
	private JButton konvertuj;
	
	private BufferedImage image;

	public String slikaPutanja = "";
	
	public Predvidjanje pr;

	/**
	 * Preuzimanje vrednosti komponente <code>slikaPutanja</code>
	 * 
	 * @return
	 */
	public String getSlikaPutanja() {
		return slikaPutanja;
	}

	/**
	 * Postavljanje nove vrednosti komponente <code>slikaPutanja</code>
	 * 
	 * @param slikaPutanja
	 */
	public void setSlikaPutanja(String slikaPutanja) {
		this.slikaPutanja = slikaPutanja;
	}

	/**
	 * Konstruktor singleton
	 * 
	 * @return
	 */
	public static MyFrame getInstance() {
		if (instance == null) {
			instance = new MyFrame();
			instance.initialise();
		}
		return instance;
	}

	/**
	 * Realizacija svih GUI elemenata glavnog prozora aplikacije.
	 * <p>Poziva se iz metode {@link MyFrame#getInstance}</p>
	 * 
	 * @throws Exception
	 * @throws IOException
	 */
	private void initialise() {
		
		Toolkit kit = Toolkit.getDefaultToolkit();
		setSize(450, 450);
		setTitle("Konvertovanje slike u tekst");
		setResizable(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		Image img = kit.getImage("data/logo.png");
		setIconImage(img);
		
		JPanel gornji = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		gornji.setPreferredSize(new Dimension(400, 50));
		add(gornji, BorderLayout.NORTH);

		obuciPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		obucavanje = new JLabel("Treniranje podataka:");	
		obuci = new JButton("Obuci");
		infoObuci = new JLabel();	
		obuciPanel.add(obucavanje);
		obuciPanel.add(obuci);
		obuciPanel.add(infoObuci);
		
		slikaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		izaberiSliku = new JLabel("Izaberite sliku:");
		dodajSliku = new JButton("+");
		labelaSlike = new JLabel();
		slikaPanel.add(izaberiSliku);
		slikaPanel.add(dodajSliku);
		slikaPanel.add(labelaSlike);
	
		
		konvPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		konvertovanje = new JLabel("Konvertovanje slike u tekst: ");
		konvertuj = new JButton("Konvertuj");	
		konvPanel.add(konvertovanje);
		konvPanel.add(konvertuj);
		
		tekstPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		infoTekst = new JLabel("Konvertovan tekst: ");
		konvertovanTekst = new JLabel();
		tekstPanel.add(infoTekst);
		tekstPanel.add(konvertovanTekst);

		
		Box boxCentar = Box.createVerticalBox();
		boxCentar.add(obuciPanel);
		boxCentar.add(slikaPanel);
		boxCentar.add(konvPanel);
		boxCentar.add(tekstPanel);
		add(boxCentar, BorderLayout.CENTER);
		
		
		dodajSliku.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// deo za dodavanje slike
				JFileChooser chooser = new JFileChooser(new File("..\\SoftProjekat\\data\\testPrimeri"));
				chooser.addChoosableFileFilter(
						new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
				int returnVal = chooser.showOpenDialog(MyFrame.getInstance());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File slika = chooser.getSelectedFile();
					setSlikaPutanja(slika.getAbsolutePath());
					try {
						image = ImageIO.read(slika);
						Image img = (Image) image;
						@SuppressWarnings("unused")
						ImageIcon icon = new ImageIcon(img);
						img = img.getScaledInstance(200, 100, Image.SCALE_SMOOTH);
						ImageIcon temp = new ImageIcon(img);
						labelaSlike.setIcon(temp);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					slikaPanel.revalidate();
					slikaPanel.repaint();
				}
			}
		});
		
		
		obuci.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				Treniranje tr = new Treniranje();
				try {
					tr.mainTreniranje();
					infoObuci.setText("Operacija uspesna!");  
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				obuciPanel.revalidate();
				obuciPanel.repaint();			
			}
		});
		
		
		konvertuj.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (slikaPutanja == "") {
					JOptionPane.showMessageDialog(optionPaneFrame, "Niste odabrali sliku za konvertovanje!", "Upozorenje", 2);
				}
				else {
					Predvidjanje pr = new Predvidjanje();
					try {
						pr.mainPredvidjanje();
						konvertovanTekst.setText(pr.getKonvTekst());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					tekstPanel.revalidate();
					tekstPanel.repaint();
				}
				
			}
		});
		
	}
	

}
