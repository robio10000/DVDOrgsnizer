package DVDOrganizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class DVDView extends JFrame {

	private static final long serialVersionUID = 9075977328656068763L;
	private JTable dataTable;
	private DefaultTableModel dataModel;
	private DVDController con;
	private JTextField searchField;

	public DVDView(DVDController con) {
		setTitle("DVD Oganizer 1.0");
		this.con = con;
		createGui();
	}

	public void createGui() {
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {

			}

			@Override
			public void windowIconified(WindowEvent e) {

			}

			@Override
			public void windowDeiconified(WindowEvent e) {

			}

			@Override
			public void windowDeactivated(WindowEvent e) {

			}

			@Override
			public void windowClosing(WindowEvent e) {
				con.saveArray(con.getDvds());
				JOptionPane.showMessageDialog(getContentPane(), "Save successfully!");
				System.exit(0);
			}

			@Override
			public void windowClosed(WindowEvent e) {

			}

			@Override
			public void windowActivated(WindowEvent e) {

			}
		});

		dataModel = new DefaultTableModel();
		dataTable = new JTable(dataModel);
		dataTable.setAutoCreateRowSorter(true);

		dataModel.addColumn("Title");
		dataModel.addColumn("Author");
		dataModel.addColumn("Release date / Note");

		getContentPane().setBackground(Color.DARK_GRAY);

		getContentPane().add(dataTable, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane(dataTable);
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		getContentPane().add(panel, BorderLayout.SOUTH);

		JButton btnAddElement = new JButton("Add object");
		btnAddElement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String title = null, author = null, date = null;
				while (title == null) {
					title = JOptionPane.showInputDialog(getContentPane(), "Title of movie", "Title",
							JOptionPane.PLAIN_MESSAGE);
				}
				while (author == null) {
					author = JOptionPane.showInputDialog(getContentPane(), "Author of movie", "Author",
							JOptionPane.PLAIN_MESSAGE);
				}
				while (date == null) {
					date = JOptionPane.showInputDialog(getContentPane(), "Release date of movie / or note", "Release / Note",
							JOptionPane.PLAIN_MESSAGE);
				}
				con.createMovie(new DVD(), title, date, author);
			}
		});
		panel.add(btnAddElement);

		JButton btnRemElement = new JButton("Remove object");
		btnRemElement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeElement();
			}
		});
		panel.add(btnRemElement);

		JLabel lblDvdSuche = new JLabel("Search:");
		lblDvdSuche.setForeground(Color.WHITE);
		panel.add(lblDvdSuche);
		searchField = new JTextField();
		searchField.setToolTipText("search title, author and date");
		searchField.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				insertUpdate(e);
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				String tmp = searchField.getText().toLowerCase();

				Vector<DVD> vec = con.getDvds();
				refreshTable(search(vec, tmp));
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				//not used
			}
		});

		panel.add(searchField);
		searchField.setColumns(10);
		this.setVisible(true);
	}

	private Vector<DVD> search(Vector<DVD> dvds, String text){
		Vector<DVD> tmpVec = new Vector<>();
		System.out.println("Search with: " + text);
		for (DVD dvd : dvds) {
			String aut = dvd.getAuthor().toLowerCase();
			String tit = dvd.getTitle().toLowerCase();
			String dat = dvd.getDate().toLowerCase();
			if(aut.contains(text) || tit.contains(text) | dat.contains(text)) {
				tmpVec.add(dvd);
				System.out.println(aut + "|" + tit +  "|" + dat);
			}
		}
		System.out.println("____End search____");
		return tmpVec;
	}

	private void refreshTable(Vector<DVD> dvds) {
		clearAll();
		addAllMoviesToTable(dvds);
	}
	
	private void removeElement() {
		con.getDvds().remove(dataTable.getSelectedRow());
		dataModel.removeRow(dataTable.getSelectedRow());
	}
	private void clearAll() {
		dataModel.setRowCount(0);
		dataTable.removeAll();
			
	}

	public void addMovieToTable(Vector<DVD> dvds) {
		Object[] ar = { dvds.get(dvds.size() - 1).getTitle(), dvds.get(dvds.size() - 1).getAuthor(),
				dvds.get(dvds.size() - 1).getDate(), };
		dataModel.addRow(ar);
	}

	public void addAllMoviesToTable(Vector<DVD> dvds) {
		for (int i = 0; i < dvds.size(); i++) {
			Object[] ar = { dvds.get(i).getTitle(), dvds.get(i).getAuthor(), dvds.get(i).getDate() };
			dataModel.addRow(ar);
		}
	}
}

