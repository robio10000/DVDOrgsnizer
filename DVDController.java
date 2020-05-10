package DVDOrganizer;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class DVDController {

	public static void main(String[] args) {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look and feel.
		}
		new DVDController();
	}

	private DVDView window;
	private XMLEncoder enc;
	private XMLDecoder dec;
	private Vector<DVD> dvds;
	private File path;
	
	private BufferedInputStream in;

	@SuppressWarnings("unchecked")
	public DVDController() {
		try {
			this.window = new DVDView(this);
			this.path = new File(System.getenv("APPDATA") + "\\DVD_Organizer\\dvds.xml");
//			this.path = new File("F:\\dvds.xml");
			if (path.exists()) {
				if (path.length() == 0) {
					this.dvds = new Vector<DVD>();
					return;
				}
				in = new BufferedInputStream(new FileInputStream(path));
				this.dec = new XMLDecoder(in);
				this.dvds = (Vector<DVD>) dec.readObject();
				this.window.addAllMoviesToTable(dvds);
				in.close();
			} else {
				new File(System.getenv("APPDATA") + "\\DVD_Organizer").mkdir();
				this.dvds = new Vector<DVD>();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createMovie(DVD m, String title, String releaseDate, String author) {
		m.setTitle(title);
		m.setDate(releaseDate);
		m.setAuthor(author);
		addMovieToVector(m);
	}

	public void addMovieToVector(DVD m) {
		dvds.add(m);
		window.addMovieToTable(dvds);
	}

	public void saveArray(Vector<DVD> ar) {
		try {
			this.enc = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(path)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		enc.writeObject(ar);
		enc.close();
	}

	public Vector<DVD> getDvds() {
		return dvds;
	}

	public void setDvds(Vector<DVD> dvds) {
		this.dvds = dvds;
	}

}

