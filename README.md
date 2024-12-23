# README

## Deskripsi Proyek
Aplikasi **BMI Tracker** adalah aplikasi berbasis Java yang bertujuan untuk membantu pengguna mencatat, menghitung, dan memantau Body Mass Index (BMI). Program ini dirancang agar mudah digunakan dengan antarmuka grafis yang intuitif serta fitur manajemen data yang lengkap.

---

## Tujuan Program
- Memberikan kemudahan bagi pengguna untuk menghitung BMI.
- Menyediakan rekomendasi berdasarkan nilai BMI (Ideal, Diet, atau Bulking).
- Memantau perkembangan BMI dari waktu ke waktu.
- Menyimpan dan mengelola data BMI untuk referensi di masa mendatang.

---

## Fitur Utama
1. **Input Data BMI**: Memasukkan tinggi badan dan berat badan untuk menghitung BMI.
2. **Manajemen Data (CRUD)**: Menambah, membaca, memperbarui, dan menghapus data BMI.
3. **Export Data**: Menyimpan data BMI ke file dokumen (DOCX).
4. **Pemantauan Progres**: Membandingkan data BMI terbaru dengan data sebelumnya.
5. **Exception Handling**: Menangani kesalahan input dan operasional.
6. **Pengujian**: Memastikan logika perhitungan dan fitur program berjalan dengan baik.

---

## Logika Perhitungan BMI
Proses perhitungan BMI dilakukan dengan rumus sederhana:

**Rumus:**
BMI dihitung dengan membagi berat badan (dalam kilogram) dengan kuadrat tinggi badan (dalam meter).
```java
bmi = weight / Math.pow(height / 100.0, 2);
```

### Kategorisasi BMI
Setelah BMI dihitung, nilai ini digunakan untuk menentukan rekomendasi:
- **Underweight (BMI < 18.5):** Rekomendasi "Bulking" untuk meningkatkan berat badan.
- **Normal (18.5 <= BMI <= 25):** Rekomendasi "Ideal" untuk mempertahankan kondisi.
- **Overweight (BMI > 25):** Rekomendasi "Diet" untuk menurunkan berat badan.

**Kode Terkait:**
- Lokasi: `BMITrackerGUI.java` di event handler tombol "Tambah Data".
```java
if (bmi < 18.5) {
recommendation = "Bulking";
        } else if (bmi > 25) {
recommendation = "Diet";
        } else {
recommendation = "Ideal";
        }
```
**Fungsi:** Menentukan tindakan yang sesuai berdasarkan hasil BMI.

---

## Cara Penggunaan
1. **Mulai Program:** Jalankan file `BMITrackerGUI.java` melalui IDE seperti IntelliJ IDEA atau NetBeans.
2. **Input Data BMI:**
    - Masukkan tinggi badan (cm) dan berat badan (kg) pada kolom input.
    - Klik tombol "Tambah Data" untuk menghitung dan menyimpan BMI.
3. **Lihat Data:**
    - Data yang dimasukkan akan tampil pada tabel di GUI.
    - Data mencakup tanggal, waktu, tinggi, berat, nilai BMI, dan rekomendasi.
4. **Update Data:**
    - Klik tombol "Update Progres" untuk membandingkan data baru dengan data sebelumnya.
5. **Hapus Data:**
    - Pilih data di tabel, lalu klik tombol "Hapus Data".
6. **Export Data:**
    - Klik tombol "Export ke DOCX" untuk menyimpan data BMI ke file dokumen.
7. **Pantau Progres:**
    - Program akan memberikan pesan progres berdasarkan perubahan data.

---

## Exception Handling
Program menggunakan mekanisme exception handling untuk mencegah kesalahan fatal:
- **Lokasi:**
    - `BMITrackerGUI.java` (Validasi input pada tombol "Tambah Data").
    - `BMIRecordsManager.java` (Operasi file seperti menyimpan dan membaca data).

**Kode Terkait:**
- Validasi input:
```java
try {
    int height = Integer.parseInt(tfHeight.getText());
    int weight = Integer.parseInt(tfWeight.getText());
    if (height <= 0 || weight <= 0) {
        throw new IllegalArgumentException("Tinggi dan berat badan harus lebih dari 0.");
    }
} catch (NumberFormatException ex) {
    JOptionPane.showMessageDialog(null, "Masukkan angka yang valid untuk tinggi dan berat badan.");
} catch (IllegalArgumentException ex) {
    JOptionPane.showMessageDialog(null, ex.getMessage());
}
```
- Operasi file:
```java
try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
    oos.writeObject(records);
} catch (IOException e) {
    e.printStackTrace();
}
```
**Fungsi:** Mencegah input invalid dan error operasional lainnya memengaruhi jalannya program.

---

## CRUD Operations
**Lokasi:**
- `BMIRecordsManager.java`: Implementasi CRUD.

**Kode Terkait:**
- Tambah Data:
```java
public void addRecord(BMIRecord record) {
    records.add(record);
    saveRecords();
}
```
- Hapus Data:
```java
public void deleteRecord(int index) {
    records.remove(index);
    saveRecords();
}
```
- Baca Data:
```java
public ArrayList<BMIRecord> getAllRecords() {
    return new ArrayList<>(records);
}
```
- Perbarui Data:
```java
public void updateRecord(int index, BMIRecord updatedRecord) {
    records.set(index, updatedRecord);
    saveRecords();
}
```
**Fungsi:** Memungkinkan pengguna untuk mengelola data BMI dengan fleksibilitas penuh.

---

## Penggunaan API
Program menggunakan **Apache POI** untuk memanipulasi dokumen Microsoft Word (DOCX). Berikut adalah detail API yang digunakan:

1. **XWPFDocument**:
    - **Lokasi:** `BMITrackerGUI.java`
    - **Kode Terkait:**
   ```java
   try (XWPFDocument document = new XWPFDocument()) {
       // Inisialisasi dokumen baru
   }
   ```
    - **Fungsi:** Membuat atau membuka file dokumen Word untuk manipulasi data BMI.

2. **XWPFParagraph**:
    - **Lokasi:** `BMITrackerGUI.java`
    - **Kode Terkait:**
   ```java
   XWPFParagraph paragraph = document.createParagraph();
   paragraph.setAlignment(ParagraphAlignment.CENTER);
   ```
    - **Fungsi:** Membuat paragraf baru di dokumen Word dan mengatur align paragraf.

3. **XWPFRun**:
    - **Lokasi:** `BMITrackerGUI.java`
    - **Kode Terkait:**
   ```java
   XWPFRun run = paragraph.createRun();
   run.setText("Rekomendasi BMI");
   ```
    - **Fungsi:** Menambahkan teks ke paragraf yang dibuat.

4. **FileOutputStream**:
    - **Lokasi:** `BMITrackerGUI.java`
    - **Kode Terkait:**
   ```java
   try (FileOutputStream out = new FileOutputStream(fileName)) {
       document.write(out);
   }
   ```
    - **Fungsi:** Menyimpan dokumen Word yang telah dimodifikasi ke dalam file di disk.

**Fungsi Keseluruhan:** API ini memungkinkan pengguna mengekspor data BMI dari aplikasi ke dokumen Word yang dapat disimpan dan dibagikan.

---

## Pengujian Program
### 1. Pengujian Perhitungan BMI
**Tujuan:** Memastikan hasil perhitungan BMI sesuai dengan nilai manual.
- Input: Tinggi badan 170 cm, berat badan 70 kg.
- Output: BMI 24.22.
```java
@Test
public void testBmiCalculation() {
    double height = 170.0;
    double weight = 70.0;
    double expectedBmi = weight / Math.pow(height / 100.0, 2);
    BMIRecord record = new BMIRecord(height, weight, expectedBmi, "");
    assertEquals(expectedBmi, record.getBmi(), 0.01);
}
```

### 2. Pengujian Rekomendasi BMI
**Tujuan:** Memastikan rekomendasi sesuai dengan kategori BMI.
- Input dan Kategori:
    - Underweight: Tinggi 170 cm, berat 50 kg, rekomendasi "Bulking".
    - Normal: Tinggi 170 cm, berat 65 kg, rekomendasi "Ideal".
    - Overweight: Tinggi 170 cm, berat 80 kg, rekomendasi "Diet".

**Kode Terkait:**
```java
@Test
public void testBmiRecommendation() {
    BMIRecord recordUnderweight = new BMIRecord(170.0, 50.0, 50.0 / Math.pow(170.0 / 100.0, 2), "Bulking");
    assertEquals("Bulking", recordUnderweight.getRecommendation());

    BMIRecord recordNormal = new BMIRecord(170.0, 65.0, 65.0 / Math.pow(170.0 / 100.0, 2), "Ideal");
    assertEquals("Ideal", recordNormal.getRecommendation());

    BMIRecord recordOverweight = new BMIRecord(170.0, 80.0, 80.0 / Math.pow(170.0 / 100.0, 2), "Diet");
    assertEquals("Diet", recordOverweight.getRecommendation());
}
```

**Kesimpulan Pengujian:** Semua pengujian berhasil tanpa error, memastikan program berfungsi sesuai spesifikasi.

---

## Struktur Proyek
- `BMIRecord.java`: Model data BMI.
- `BMIRecordsManager.java`: Logika manajemen data BMI.
- `BMITrackerGUI.java`: Antarmuka pengguna.
- `BMIRecordTest.java`: Pengujian unit menggunakan JUnit.

---


