import java.util.List;

public class Makanan implements SiapSaji {
    private String nama;
    private double harga;
    private int waktuPersiapan; // Waktu yang dibutuhkan untuk memasak (detik)
    private List<String> bahanBaku;
    private StatusMakanan status;

    // Polimorfisme: Overloading Konstruktor 1 (Lengkap)
    public Makanan(String nama, double harga, int waktuPersiapan, List<String> bahanBaku) {
        this.nama = nama;
        this.harga = harga;
        this.waktuPersiapan = waktuPersiapan;
        this.bahanBaku = bahanBaku;
        this.status = StatusMakanan.MENTAH;
    }

    // Polimorfisme: Overloading Konstruktor 2 (Tanpa Bahan Baku spesifik, pakai default)
    public Makanan(String nama, double harga, int waktuPersiapan) {
        this(nama, harga, waktuPersiapan, List.of("Daging", "Roti", "Sayuran"));
    }

    public double getHarga() { return harga; }
    public String getNama() { return nama; }
    public int getWaktuPersiapan() { return waktuPersiapan; }
    public List<String> getBahanBaku() { return bahanBaku; }
    public StatusMakanan getStatusMakanan() { return status; }
    public void setStatus(StatusMakanan status) { this.status = status; }

    public int hitungWaktuPersiapan() {
        return this.waktuPersiapan;
    }

    @Override
    public void sajikan() {
        if (this.status == StatusMakanan.SEDANG_DIMASAK) {
            this.status = StatusMakanan.SIAP_SAJI;
            System.out.println("-> Makanan '" + nama + "' kini " + getStatus());
        }
    }

    @Override
    public String getStatus() {
        return this.status.toString();
    }

    public void displayDetail() {
        System.out.println("  - " + nama + " (Rp" + harga + ")");
    }
}
