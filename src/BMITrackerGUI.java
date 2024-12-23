import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Class Program.BMITrackerGUI adalah antarmuka pengguna grafis (GUI) untuk aplikasi BMI Tracker.
 * Aplikasi ini mendukung pengelolaan data BMI (Body Mass Index), seperti menambah,
 * memperbarui, mengekspor data ke file DOCX, dan menghapus data.
 */
public class BMITrackerGUI extends JFrame {
    private JTextField tfHeight, tfWeight; // Input field untuk tinggi dan berat badan
    private JTable table; // Tabel untuk menampilkan data BMI
    private DefaultTableModel tableModel; // Model untuk data pada tabel
    private BMIRecordsManager manager; // Manajer untuk mengelola data BMI

    public JTextField getTfHeight() {
        return tfHeight;
    }

    public JTextField getTfWeight() {
        return tfWeight;
    }

    public BMIRecordsManager getManager() {
        return manager;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    /**
     * Konstruktor untuk inisialisasi antarmuka pengguna.
     * Menyiapkan tata letak, tombol, tabel, dan event handler.
     */
    public BMITrackerGUI() {
        setTitle("BMI Tracker");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        manager = new BMIRecordsManager(); // Inisialisasi manajer data BMI
        setLayout(new BorderLayout(10, 10)); // Tata letak utama

        // Panel Input
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "Input Data", 0, 0, new Font("Arial", Font.BOLD, 14), Color.BLUE));
        inputPanel.setBackground(new Color(200, 220, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblHeight = new JLabel("Tinggi Badan (cm):");
        JLabel lblWeight = new JLabel("Berat Badan (kg):");
        tfHeight = new JTextField(10); // Input untuk tinggi badan
        tfWeight = new JTextField(10); // Input untuk berat badan

        JButton btnAdd = new JButton("Tambah Data");
        styleButton(btnAdd, new Color(50, 205, 50));

        JButton btnUpdate = new JButton("Update Progres");
        styleButton(btnUpdate, new Color(30, 144, 255));

        JButton btnExport = new JButton("Export ke DOCX");
        styleButton(btnExport, new Color(255, 165, 0));

        JButton btnDelete = new JButton("Hapus Data");
        styleButton(btnDelete, new Color(255, 69, 0));

        // Menambahkan komponen ke panel input
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(lblHeight, gbc);

        gbc.gridx = 1;
        inputPanel.add(tfHeight, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(lblWeight, gbc);

        gbc.gridx = 1;
        inputPanel.add(tfWeight, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(btnAdd, gbc);

        gbc.gridy = 3;
        inputPanel.add(btnUpdate, gbc);

        gbc.gridy = 4;
        inputPanel.add(btnExport, gbc);

        gbc.gridy = 5;
        inputPanel.add(btnDelete, gbc);

        add(inputPanel, BorderLayout.WEST);

        // Membuat tabel untuk menampilkan data BMI
        tableModel = new DefaultTableModel(new String[]{"Tanggal", "Jam", "Tinggi", "Berat", "BMI", "Rekomendasi"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "Data BMI", 0, 0, new Font("Arial", Font.BOLD, 14), Color.BLUE));
        add(scrollPane, BorderLayout.CENTER);

        // Set center alignment for all columns except "Rekomendasi"
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (!table.getColumnName(i).equals("Rekomendasi")) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        // Load data dari manager ke tabel
        for (BMIRecord record : manager.getAllRecords()) {
            tableModel.addRow(new Object[]{record.getDate(), record.getTime(), (int) record.getHeight(), (int) record.getWeight(), String.format("%.2f", record.getBmi()), record.getRecommendation()});
        }

        // Event Handler: Tambah Data
        btnAdd.addActionListener(this::actionPerformed);

        // Event Handler: Export ke DOCX
        btnExport.addActionListener(e -> {
            String fileName = JOptionPane.showInputDialog("Masukkan nama file (tanpa ekstensi):");
            if (fileName == null || fileName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nama file tidak boleh kosong.");
                return;
            }

            fileName += ".docx";

            File file = new File(fileName);
            if (file.exists()) {
                JOptionPane.showMessageDialog(null, "Nama file \"" + fileName + "\" sudah digunakan. Masukkan nama lain.");
                return;
            }

            try (XWPFDocument document = new XWPFDocument(); FileOutputStream out = new FileOutputStream(fileName)) {
                XWPFParagraph title = document.createParagraph();
                title.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun titleRun = title.createRun();
                titleRun.setText("Rekomendasi BMI");
                titleRun.setBold(true);
                titleRun.setFontSize(16);

                // Ambil data terbaru (baris terakhir dari tableModel)
                int lastRow = tableModel.getRowCount() - 1; // Indeks baris terakhir
                if (lastRow >= 0) { // Pastikan ada data
                    String date = tableModel.getValueAt(lastRow, 0).toString();
                    String time = tableModel.getValueAt(lastRow, 1).toString();
                    String height = tableModel.getValueAt(lastRow, 2).toString();
                    String weight = tableModel.getValueAt(lastRow, 3).toString();
                    String bmi = tableModel.getValueAt(lastRow, 4).toString();
                    String recommendation = tableModel.getValueAt(lastRow, 5).toString();

                    XWPFParagraph paragraph = document.createParagraph();
                    XWPFRun run = paragraph.createRun();

                    run.setText("Tanggal: " + date);
                    run.addBreak();
                    run.setText("Jam: " + time);
                    run.addBreak();
                    run.setText("Tinggi: " + height + " cm");
                    run.addBreak();
                    run.setText("Berat: " + weight + " kg");
                    run.addBreak();
                    run.setText("BMI: " + bmi);
                    run.addBreak();
                    run.setText("Rekomendasi: " + recommendation);
                    run.addBreak();
                }

                document.write(out);
                JOptionPane.showMessageDialog(null, "Dokumen berhasil diekspor dengan data terbaru!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + ex.getMessage());
            }
        });

        // Event Handler: Update Progres
        btnUpdate.addActionListener(e -> {
            String fileName = JOptionPane.showInputDialog("Masukkan nama file yang ingin di-update (tanpa ekstensi):");
            if (fileName == null || fileName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nama file tidak boleh kosong.");
                return;
            }

            fileName += ".docx";

            File file = new File(fileName);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null, "File dengan nama \"" + fileName + "\" tidak ditemukan. Pastikan Anda sudah membuat file tersebut.");
                return;
            }

            try (XWPFDocument document = new XWPFDocument(new FileInputStream(file)); FileOutputStream out = new FileOutputStream(fileName)) {
                // Ambil data terbaru dan data sebelumnya
                BMIRecord latestRecord = manager.getAllRecords().get(manager.getTotalRecords() - 1); // Data terbaru
                BMIRecord previousRecord = manager.getAllRecords().size() > 1
                        ? manager.getAllRecords().get(manager.getTotalRecords() - 2) // Data sebelumnya
                        : null;

                XWPFParagraph paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();

                String progressMessage;

                if (previousRecord == null) {
                    progressMessage = "Tidak ada data sebelumnya untuk dibandingkan.";
                } else {
                    // Logika pesan berdasarkan rekomendasi sebelumnya
                    if ("Diet".equals(previousRecord.getRecommendation())) {
                        if (latestRecord.getBmi() >= 18.5 && latestRecord.getBmi() <= 25) {
                            progressMessage = "Yeayy, kamu berhasil mencapai ideal.";
                        } else if (latestRecord.getWeight() < previousRecord.getWeight()) {
                            progressMessage = "Progres diet berhasil, lebih semangat lagi untuk mencapai ideal.";
                        } else if (latestRecord.getWeight() == previousRecord.getWeight()) {
                            progressMessage = "Tidak ada progres, pastikan untuk melaksanakan tips di atas.";
                        } else {
                            progressMessage = "Progres tidak sesuai harapan, coba evaluasi lagi.";
                        }
                    } else if ("Bulking".equals(previousRecord.getRecommendation())) {
                        if (latestRecord.getBmi() >= 18.5 && latestRecord.getBmi() <= 25) {
                            progressMessage = "Yeayy, kamu berhasil mencapai ideal.";
                        } else if (latestRecord.getWeight() > previousRecord.getWeight()) {
                            progressMessage = "Progres bulking berhasil, lebih semangat lagi untuk mencapai ideal.";
                        } else if (latestRecord.getWeight() == previousRecord.getWeight()) {
                            progressMessage = "Tidak ada progres, pastikan untuk melaksanakan tips di atas.";
                        } else {
                            progressMessage = "Progres tidak sesuai harapan, coba evaluasi lagi.";
                        }
                    } else {
                        progressMessage = "Data progres tidak sesuai untuk dianalisis lebih lanjut.";
                    }
                }

                // Tambahkan data terbaru ke file
                run.setText("Tanggal: " + latestRecord.getDate());
                run.addBreak();
                run.setText("Jam: " + latestRecord.getTime());
                run.addBreak();
                run.setText("Tinggi: " + (int) latestRecord.getHeight() + " cm");
                run.addBreak();
                run.setText("Berat: " + (int) latestRecord.getWeight() + " kg");
                run.addBreak();
                run.setText("BMI: " + String.format("%.2f", latestRecord.getBmi()));
                run.addBreak();
                run.setText("Pesan: " + progressMessage);
                run.addBreak();

                document.write(out);

                JOptionPane.showMessageDialog(null, "File berhasil di-update dengan data terbaru!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + ex.getMessage());
            }
        });

        // Event Handler: Hapus Data
        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                // Hapus data dari Program.BMIRecordsManager
                manager.deleteRecord(selectedRow);

                // Hapus data dari tabel
                tableModel.removeRow(selectedRow);

                JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
            } else {
                JOptionPane.showMessageDialog(null, "Pilih baris data yang ingin dihapus.");
            }
        });

    }

    /**
     * Main method untuk menjalankan aplikasi BMI Tracker.
     *
     * @param args Argumen baris perintah (tidak digunakan).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BMITrackerGUI().setVisible(true));
    }

    private void actionPerformed(ActionEvent e) {
        try {
            // Validasi input tinggi dan berat badan
            int height = Integer.parseInt(tfHeight.getText());
            int weight = Integer.parseInt(tfWeight.getText());

            if (height <= 0 || weight <= 0) {
                throw new IllegalArgumentException("Tinggi dan berat badan harus lebih dari 0.");
            }

            double bmi = weight / Math.pow(height / 100.0, 2);

            if (bmi < 10 || bmi > 100) {
                throw new IllegalArgumentException("Hasil BMI tidak logis. Pastikan input benar.");
            }

            // Menentukan rekomendasi berdasarkan BMI
            String recommendation;
            if (bmi < 18.5) {
                recommendation = "Bulking";
            } else if (bmi > 25) {
                recommendation = "Diet";
            } else {
                recommendation = "Ideal";
            }

            // Menambahkan data ke manajer dan tabel
            BMIRecord record = new BMIRecord(height, weight, bmi, recommendation);
            manager.addRecord(record);

            tableModel.addRow(new Object[]{record.getDate(), record.getTime(), height, weight, String.format("%.2f", bmi), recommendation});

            tfHeight.setText("");
            tfWeight.setText("");

            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Masukkan angka yang valid untuk tinggi dan berat badan.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
    }
}
