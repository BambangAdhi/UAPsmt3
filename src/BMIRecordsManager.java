import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;
import java.util.ArrayList;

/**
 * Class Program.BMIRecordsManager digunakan untuk mengelola daftar data Program.BMIRecord.
 * Kelas ini mendukung operasi CRUD (Create, Read, Update, Delete) serta
 * memperbarui file DOCX dengan data terbaru.
 */
public class BMIRecordsManager {
    private ArrayList<BMIRecord> records; // Daftar untuk menyimpan Program.BMIRecord
    private final String dataFile = "bmi_records.dat"; // Nama file untuk persistensi data

    /**
     * Constructor untuk menginisialisasi daftar Program.BMIRecord.
     * Memuat data dari file jika sudah ada, atau membuat daftar kosong.
     */
    public BMIRecordsManager() {
        records = new ArrayList<>();
        loadRecords(); // Memuat data dari file saat inisialisasi
    }

    /**
     * Menambahkan Program.BMIRecord baru ke daftar.
     *
     * @param record Objek Program.BMIRecord yang akan ditambahkan.
     * @throws IllegalArgumentException jika record bernilai null.
     */
    public void addRecord(BMIRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("Record tidak boleh null.");
        }
        records.add(record);
        saveRecords(); // Menyimpan data setelah menambahkan
    }

    /**
     * Mendapatkan seluruh daftar Program.BMIRecord yang ada dalam bentuk ArrayList.
     *
     * @return Salinan daftar Program.BMIRecord.
     */
    public ArrayList<BMIRecord> getAllRecords() {
        return new ArrayList<>(records); // Mengembalikan salinan untuk melindungi data asli
    }

    /**
     * Memperbarui Program.BMIRecord pada index tertentu.
     *
     * @param index        Indeks data yang akan diperbarui.
     * @param updatedRecord Program.BMIRecord yang baru.
     * @throws IndexOutOfBoundsException jika index tidak valid.
     * @throws IllegalArgumentException  jika updatedRecord bernilai null.
     */
    public void updateRecord(int index, BMIRecord updatedRecord) {
        if (index < 0 || index >= records.size()) {
            throw new IndexOutOfBoundsException("Index tidak valid.");
        }
        if (updatedRecord == null) {
            throw new IllegalArgumentException("Updated record tidak boleh null.");
        }
        records.set(index, updatedRecord);
        saveRecords(); // Menyimpan data setelah memperbarui
    }

    /**
     * Menghapus Program.BMIRecord berdasarkan indeks.
     *
     * @param index Indeks dari data yang akan dihapus.
     * @throws IndexOutOfBoundsException jika index tidak valid.
     */
    public void deleteRecord(int index) {
        if (index < 0 || index >= records.size()) {
            throw new IndexOutOfBoundsException("Index tidak valid.");
        }
        records.remove(index);
        saveRecords(); // Menyimpan data setelah penghapusan
    }

    /**
     * Menyimpan daftar Program.BMIRecord ke file untuk persistensi.
     * Jika terjadi kesalahan selama penyimpanan, maka error akan dicetak di konsol.
     */
    private void saveRecords() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(records);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Memuat daftar Program.BMIRecord dari file jika file tersedia.
     * Jika file tidak ditemukan, maka daftar akan tetap kosong.
     */
    private void loadRecords() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            records = (ArrayList<BMIRecord>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Jika file belum ada, abaikan.
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mengembalikan jumlah total Program.BMIRecord yang tersimpan dalam daftar.
     *
     * @return Jumlah total data dalam daftar.
     */
    public int getTotalRecords() {
        return records.size();
    }

    /**
     * Menampilkan semua data Program.BMIRecord ke konsol untuk debugging.
     * Jika tidak ada data, pesan akan menunjukkan daftar kosong.
     */
    public void printAllRecords() {
        if (records.isEmpty()) {
            System.out.println("Tidak ada data yang tersedia.");
        } else {
            System.out.println("Daftar Program.BMIRecord:");
            for (int i = 0; i < records.size(); i++) {
                System.out.println((i + 1) + ". " + records.get(i));
            }
        }
    }

    /**
     * Memperbarui file DOCX dengan data Program.BMIRecord terbaru.
     * Data terbaru akan ditambahkan ke dalam dokumen, termasuk pesan progres.
     *
     * @param fileName  Nama file DOCX yang akan diperbarui (termasuk ekstensi).
     * @param newRecord Data Program.BMIRecord terbaru yang akan ditambahkan.
     * @return true jika file ditemukan dan diperbarui, false jika file tidak ditemukan.
     */
    public boolean updateDocx(String fileName, BMIRecord newRecord) {
        File file = new File(fileName);

        if (!file.exists()) {
            return false; // File tidak ditemukan
        }

        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument document = new XWPFDocument(fis)) {

            // Ambil data terakhir untuk dibandingkan
            BMIRecord previousRecord = records.get(records.size() - 1);

            // Tambahkan data terbaru ke dokumen
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();

            run.setText("Tanggal: " + newRecord.getDate());
            run.addBreak();
            run.setText("Jam: " + newRecord.getTime());
            run.addBreak();
            run.setText("Tinggi: " + (int) newRecord.getHeight() + " cm");
            run.addBreak();
            run.setText("Berat: " + (int) newRecord.getWeight() + " kg");
            run.addBreak();
            run.setText("BMI: " + String.format("%.2f", newRecord.getBmi()));
            run.addBreak();

            // Tambahkan pesan berdasarkan progres
            String message;

            if (newRecord.getBmi() >= 18.5 && newRecord.getBmi() <= 25) {
                message = "Yeayy, kamu berhasil mencapai ideal.";
            } else if ("Diet".equals(previousRecord.getRecommendation())) {
                if (newRecord.getWeight() < previousRecord.getWeight()) {
                    message = "Progres diet berhasil, lebih semangat lagi untuk mencapai ideal.";
                } else if (newRecord.getWeight() == previousRecord.getWeight() && newRecord.getHeight() == previousRecord.getHeight()) {
                    message = "Tidak ada progres, pastikan untuk melaksanakan tips di atas.";
                } else {
                    message = "Progres tidak sesuai harapan, coba evaluasi lagi.";
                }
            } else if ("Bulking".equals(previousRecord.getRecommendation())) {
                if (newRecord.getWeight() > previousRecord.getWeight()) {
                    message = "Progres bulking berhasil, lebih semangat lagi untuk mencapai ideal.";
                } else if (newRecord.getWeight() == previousRecord.getWeight() && newRecord.getHeight() == previousRecord.getHeight()) {
                    message = "Tidak ada progres, pastikan untuk melaksanakan tips di atas.";
                } else {
                    message = "Progres tidak sesuai harapan, coba evaluasi lagi.";
                }
            } else {
                message = "Data progres tidak sesuai untuk dianalisis lebih lanjut.";
            }

            run.setText("Pesan: " + message);
            run.addBreak();

            // Simpan dokumen
            try (FileOutputStream fos = new FileOutputStream(file)) {
                document.write(fos);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
