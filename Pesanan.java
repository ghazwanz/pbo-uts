import java.util.List;

public class Pesanan {
    private static int nextId = 1;
    private int id;
    private Pelanggan pelanggan;
    private List<Makanan> listMakanan;
    private double totalHarga;
    private long waktuPesan;
    private long waktuSelesai = 0;
    private int totalWaktuMemasak;
    private boolean dibatalkan = false;

    public Pesanan(Pelanggan pelanggan, List<Makanan> listMakanan) {
        this.id = nextId++;
        this.pelanggan = pelanggan;
        this.listMakanan = listMakanan;
        this.waktuPesan = System.currentTimeMillis();
        hitungTotalHarga();
        hitungTotalWaktuMemasak();
    }

    private void hitungTotalHarga() {
        double total = 0.0;

        // Ulangi (loop) setiap Makanan dalam listMakanan
        for (Makanan m : listMakanan) {
            // Tambahkan harga makanan 'm' ke variabel total
            total = total + m.getHarga();
        }

        this.totalHarga = total;
    }

    private void hitungTotalWaktuMemasak() {
    int totalWaktu = 0;
    
    for (Makanan m : listMakanan) {
        totalWaktu = totalWaktu + m.getWaktuPersiapan();
    }
    this.totalWaktuMemasak = totalWaktu;
}

    public int getId() {
        return id;
    }

    public Pelanggan getPelanggan() {
        return pelanggan;
    }

    public List<Makanan> getListMakanan() {
        return listMakanan;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public int getTotalWaktuMemasak() {
        return totalWaktuMemasak;
    }

    public long getWaktuPesan() {
        return waktuPesan;
    }

    public long getWaktuSelesai() {
        return waktuSelesai;
    }

    public void setWaktuSelesai(long waktuSelesai) {
        this.waktuSelesai = waktuSelesai;
    }

    public boolean isDibatalkan() {
        return dibatalkan;
    }

    public void setDibatalkan(boolean dibatalkan) {
        this.dibatalkan = dibatalkan;
    }

    public void displayDetail() {
        System.out.println("--- Pesanan ID: " + id + " oleh " + pelanggan.getNama() + " ---");
        listMakanan.forEach(Makanan::displayDetail);
        System.out.printf("Total: Rp%.2f | Est. Masak: %d detik\n", totalHarga, totalWaktuMemasak);
    }
}
