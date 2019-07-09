import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import net.miginfocom.swing.MigLayout;

/**
 * @author Maxx Persin
 *
 */
public class TarkovHelper implements ActionListener {

	// Disregard all of row 1 and all of column 2 when parsing csv file
	public static LinkedList<LinkedList<Ammo>> chartData = new LinkedList<>();
	private JTable table;

	/**
	 * 
	 */
	public TarkovHelper() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		JFrame mainWindow = new JFrame();
		mainWindow.setTitle("Tarkov Helper");
		mainWindow.setVisible(true);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setLocation(screenSize.width - 450, screenSize.height - 500);
		mainWindow.setSize(1341, 541);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		mainWindow.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Ammo", null, panel, null);
		panel.setLayout(null);

		JLabel lblCaliber = new JLabel("Caliber");
		lblCaliber.setBounds(7, 12, 33, 14);
		panel.add(lblCaliber);

		String[] ammoTypes = { "12x70", "20x70", ".366", "4.6x30", "5.45x39", "5.56x45", "7.62x25tt", "7.62x39",
				"7.62x51", "7.62x54R", "9x18pm", "9x19", "9x21", "9x39" };

		JComboBox comboBox = new JComboBox(ammoTypes);
		comboBox.setBounds(52, 9, 87, 20);
		panel.add(comboBox);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(7, 34, 1303, 431);
		panel.add(scrollPane);

		Object columnNames[] = { "Caliber", "Name", "Damage", "Penetration Value", "Armour Damage %",
				"Fragmentation Chance", "Vs Class 1", "Vs Class 2", "Vs Class 3", "Vs Class 4", "Vs Class 5",
				"Vs Class 6", "Most Effective Against" };
		Object rowData[][] = new Object[14][13];
		table = new JTable(rowData, columnNames);

		scrollPane.setViewportView(table);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Maps", null, panel_1, null);

		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Credits", null, panel_2, null);

		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(150, 7, 90, 23);
		btnSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				for (int i = 0; i < 14; i++) {
					for(int j = 0; j < 13; j++) {
						rowData[i][j] = null;
					}
				}
				updateTable(table, rowData, comboBox.getSelectedItem());
				table.updateUI();
			}
		});
		panel.add(btnSearch);

		JButton btnCompare = new JButton("Compare");
		btnCompare.setBounds(1208, 8, 102, 23);
		btnCompare.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		panel.add(btnCompare);

	}

	private void updateTable(JTable table, Object[][] r, Object item) {
		int idx = 0;
		for (int i = 0; i < chartData.size(); i++) {
			for (int j = 0; j < chartData.get(i).size(); j++) {
				if (chartData.get(i).get(j).getRoundType().equals(item.toString())) {
					Ammo a = chartData.get(i).get(j);
					r[idx][0] = a.getRoundType();
					r[idx][1] = a.getName();
					r[idx][2] = a.getDamage();
					r[idx][3] = a.getPenetration();
					r[idx][4] = a.getArmourDamage();
					r[idx][5] = a.getFragmentationChance();
					r[idx][6] = a.getEffectiveness()[0];
					r[idx][7] = a.getEffectiveness()[1];
					r[idx][8] = a.getEffectiveness()[2];
					r[idx][9] = a.getEffectiveness()[3];
					r[idx][10] = a.getEffectiveness()[4];
					r[idx][11] = a.getEffectiveness()[5];
					r[idx][12] = a.getHighestArmourPen();
					idx++;
				}
			}
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			File csvFile = new File("tarkov_ammo_chart.csv");
			BufferedReader csvReader = new BufferedReader(new FileReader(csvFile));
			int idx1 = 0, idx2 = 0;
			String line;
			String prevCaliber = "";
			while ((line = csvReader.readLine()) != null) {
				String[] values = line.split(",");
				if (!values[0].equals("Ammo Type")) {
					if (!values[0].equals(prevCaliber)) {
						idx1++;
						idx2 = 0;
						chartData.add(new LinkedList<Ammo>());
					}
					if (prevCaliber.equals("")) {
						prevCaliber = values[0];
					}
					int[] temp = { Integer.parseInt(values[6]), Integer.parseInt(values[7]),
							Integer.parseInt(values[8]), Integer.parseInt(values[9]), Integer.parseInt(values[10]),
							Integer.parseInt(values[11]) };
					Ammo a = new Ammo(values[0], values[1], Integer.parseInt(values[2]), Integer.parseInt(values[3]),
							Integer.parseInt(values[4]),
							Integer.parseInt(values[5].substring(0, values[5].length() - 1)), temp);
					// chartData.add((ArrayList<String>) Arrays.asList(values));
					// System.out.println(Arrays.asList(values));
					// chartData.System.out.println(a.toString());
					chartData.getLast().add(a);
					// System.out.println(chartData.getLast().getLast());
					idx2++;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
			JOptionPane.showMessageDialog(null, "Error reading ammo chart file");
			System.exit(-1);
		}
//		for(int i = 0; i < chartData.size(); i++) {
//			for(int j = 0; j < chartData.get(i).size(); j++) {
//				System.out.println(chartData.get(i).get(j).toString());
//			}
//		}
		TarkovHelper t = new TarkovHelper();

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

	}
}