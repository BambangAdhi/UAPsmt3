import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class Program.BMIRecord digunakan untuk menyimpan data hasil kalkulasi BMI,
 * termasuk informasi tanggal, waktu, tinggi badan, berat badan, nilai BMI, dan rekomendasi.
 * Objek ini mendukung serialization.
 */
public class BMIRecord implements Serializable {
    private static final long serialVersionUID = 1L; // Untuk mendukung serialization
    private String date;           // Tanggal input
    private String time;           // Waktu input
    private double height;         // Tinggi badan (cm)
    private double weight;         // Berat badan (kg)
    private double bmi;            // Nilai BMI
    private String recommendation; // Rekomendasi berdasarkan BMI

    /**
     * Constructor untuk membuat objek Program.BMIRecord dengan data tinggi badan, berat badan,
     * nilai BMI, dan rekomendasi.
     *
     * @param height        Tinggi badan dalam satuan cm.
     * @param weight        Berat badan dalam satuan kg.
     * @param bmi           Nilai Body Mass Index (BMI) yang telah dikalkulasi.
     * @param recommendation Rekomendasi berdasarkan nilai BMI.
     */
    public BMIRecord(double height, double weight, double bmi, String recommendation) {
        this.date = getCurrentDate(); // Tanggal otomatis
        this.time = getCurrentTime(); // Waktu otomatis
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
        this.recommendation = recommendation;
    }

    /**
     * Mendapatkan tanggal ketika data Program.BMIRecord dibuat.
     *
     * @return Tanggal dalam format "yyyy-MM-dd".
     */
    // Getter untuk atribut
    public String getDate() {
        return date;
    }

    /**
     * Mendapatkan waktu ketika data Program.BMIRecord dibuat.
     *
     * @return Waktu dalam format "HH:mm:ss".
     */
    public String getTime() {
        return time;
    }

    /**
     * Mendapatkan tinggi badan yang tercatat dalam data Program.BMIRecord.
     *
     * @return Tinggi badan dalam satuan cm.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Mendapatkan berat badan yang tercatat dalam data Program.BMIRecord.
     *
     * @return Berat badan dalam satuan kg.
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Mendapatkan nilai Body Mass Index (BMI) dari data Program.BMIRecord.
     *
     * @return Nilai BMI.
     */
    public double getBmi() {
        return bmi;
    }

    /**
     * Mendapatkan rekomendasi berdasarkan nilai BMI.
     *
     * @return Rekomendasi dalam bentuk String.
     */
    public String getRecommendation() {
        return recommendation;
    }

    /**
     * Mengubah rekomendasi berdasarkan analisis terbaru.
     *
     * @param recommendation Rekomendasi baru untuk data Program.BMIRecord.
     */
    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    /**
     * Mendapatkan tanggal saat ini dalam format "yyyy-MM-dd".
     * Metode ini hanya digunakan secara internal oleh konstruktor.
     *
     * @return Tanggal saat ini.
     */
    // Mendapatkan tanggal saat ini
    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date());
    }

    /**
     * Mendapatkan waktu saat ini dalam format "HH:mm:ss".
     * Metode ini hanya digunakan secara internal oleh konstruktor.
     *
     * @return Waktu saat ini.
     */
    // Mendapatkan waktu saat ini
    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(new Date());
    }

    /**
     * Mengembalikan representasi String dari data Program.BMIRecord,
     * termasuk tanggal, waktu, tinggi badan, berat badan, BMI, dan rekomendasi.
     *
     * @return Representasi String dari objek Program.BMIRecord.
     */
    @Override
    public String toString() {
        return "Program.BMIRecord {Tanggal='" + date + "', Waktu='" + time + "', Tinggi='" + height +
                " cm', Berat='" + weight + " kg', BMI='" + bmi + "', Rekomendasi='" + recommendation + "'}";
    }

    /**
     * Membandingkan data Program.BMIRecord saat ini dengan data sebelumnya untuk
     * menentukan pesan progres berdasarkan perubahan berat badan dan tinggi badan.
     *
     * @param previousRecord Objek Program.BMIRecord sebelumnya untuk perbandingan.
     * @return Pesan progres berdasarkan perbandingan data.
     *         Jika tidak ada data sebelumnya, akan mengembalikan pesan default.
     */
    public String compareProgress(BMIRecord previousRecord) {
        if (previousRecord == null) {
            return "Tidak ada data sebelumnya untuk dibandingkan.";
        }

        // Mengambil nilai dari data sebelumnya
        double previousHeight = previousRecord.getHeight();
        double previousWeight = previousRecord.getWeight();

        // Periksa apakah BMI sudah ideal
        if (this.bmi >= 18.5 && this.bmi <= 25) {
            return "Yeayy, kamu berhasil mencapai ideal.";
        }

        // Logika berdasarkan rekomendasi sebelumnya
        if (previousRecord.getRecommendation().equals("Diet")) {
            if (this.weight < previousWeight) {
                return "Progres diet berhasil, lebih semangat lagi untuk mencapai ideal.";
            } else if (this.weight == previousWeight) {
                return "Tidak ada progres, pastikan untuk melaksanakan tips di atas.";
            } else {
                return "Progres tidak sesuai harapan, coba evaluasi lagi.";
            }
        } else if (previousRecord.getRecommendation().equals("Bulking")) {
            if (this.weight > previousWeight) {
                return "Progres bulking berhasil, lebih semangat lagi untuk mencapai ideal.";
            } else if (this.weight == previousWeight) {
                return "Tidak ada progres, pastikan untuk melaksanakan tips di atas.";
            } else {
                return "Progres tidak sesuai harapan, coba evaluasi lagi.";
            }
        }

        return "Data progres tidak sesuai untuk dianalisis lebih lanjut.";
    }
}
