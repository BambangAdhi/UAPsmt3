import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Class BMIRecordTest digunakan untuk menguji logika perhitungan BMI
 * dan rekomendasi yang dihasilkan dari data BMI.
 */
public class BMIRecordTest {

    /**
     * Metode ini menguji perhitungan BMI dengan nilai input valid.
     */
    @Test
    public void testBmiCalculation() {
        double height = 170.0; // dalam cm
        double weight = 70.0; // dalam kg

        // Hitung BMI secara manual
        double expectedBmi = weight / Math.pow(height / 100.0, 2);

        // Buat objek BMIRecord dan cek nilai BMI
        BMIRecord record = new BMIRecord(height, weight, expectedBmi, "");
        assertEquals(expectedBmi, record.getBmi(), 0.01, "BMI tidak sesuai dengan perhitungan manual.");
    }

    /**
     * Metode ini menguji rekomendasi yang dihasilkan berdasarkan nilai BMI.
     */
    @Test
    public void testBmiRecommendation() {
        // Test untuk BMI kurang dari 18.5 (Underweight)
        BMIRecord recordUnderweight = new BMIRecord(170.0, 50.0, 50.0 / Math.pow(170.0 / 100.0, 2), "Bulking");
        assertEquals("Bulking", recordUnderweight.getRecommendation(), "Rekomendasi untuk underweight salah.");

        // Test untuk BMI antara 18.5 dan 25 (Normal)
        BMIRecord recordNormal = new BMIRecord(170.0, 65.0, 65.0 / Math.pow(170.0 / 100.0, 2), "Ideal");
        assertEquals("Ideal", recordNormal.getRecommendation(), "Rekomendasi untuk BMI normal salah.");

        // Test untuk BMI lebih dari 25 (Overweight)
        BMIRecord recordOverweight = new BMIRecord(170.0, 80.0, 80.0 / Math.pow(170.0 / 100.0, 2), "Diet");
        assertEquals("Diet", recordOverweight.getRecommendation(), "Rekomendasi untuk overweight salah.");
    }

    /**
     * Metode ini menguji perbandingan progres antara dua data BMIRecord.
     */
    @Test
    public void testProgressComparison() {
        BMIRecord previousRecord = new BMIRecord(170.0, 80.0, 80.0 / Math.pow(170.0 / 100.0, 2), "Diet");
        BMIRecord currentRecord = new BMIRecord(170.0, 75.0, 75.0 / Math.pow(170.0 / 100.0, 2), "Diet");

        // Cek pesan progres
        String expectedMessage = "Progres diet berhasil, lebih semangat lagi untuk mencapai ideal.";
        assertEquals(expectedMessage, currentRecord.compareProgress(previousRecord), "Pesan progres tidak sesuai.");
    }

    /**
     * Metode ini menguji skenario jika tidak ada data sebelumnya untuk dibandingkan.
     */
    @Test
    public void testProgressComparisonNoPreviousRecord() {
        BMIRecord currentRecord = new BMIRecord(170.0, 75.0, 75.0 / Math.pow(170.0 / 100.0, 2), "Diet");

        // Cek pesan progres
        String expectedMessage = "Tidak ada data sebelumnya untuk dibandingkan.";
        assertEquals(expectedMessage, currentRecord.compareProgress(null), "Pesan untuk data sebelumnya tidak sesuai.");
    }
}
