import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RestoranCepatSaji {
    protected String namaRestoran;
    protected double keuangan;
    // Agregasi: Inventori bahan baku (menggunakan ArrayList)
    protected List<BahanBakuStok> inventoriBahanBaku; 
    protected double reputasi; // Skor 0-100
    // Agregasi: Daftar Karyawan
    protected List<Karyawan> daftarKaryawan;
    // Antrian Pesanan (menggunakan Queue)
    protected Queue<Pesanan> antrianPesanan;
    protected List<Pesanan> riwayatPesanan;
    protected List<Pelanggan> daftarPelanggan;
    protected List<Makanan> menuRestoran;

    public RestoranCepatSaji(String namaRestoran, double modalAwal) {
        this.namaRestoran = namaRestoran;
        this.keuangan = modalAwal;
        this.reputasi = 75.0; // Reputasi awal
        this.inventoriBahanBaku = new ArrayList<>();
        this.daftarKaryawan = new ArrayList<>();
        this.antrianPesanan = new LinkedList<>(); // Implementasi Queue
        this.riwayatPesanan = new ArrayList<>();
        this.daftarPelanggan = new ArrayList<>();
        
        initInventori();
        initKaryawan();
        initMenu();
    }
    
    public void initInventori() {
        inventoriBahanBaku.add(new BahanBakuStok("Daging", 50));
        inventoriBahanBaku.add(new BahanBakuStok("Roti", 100));
        inventoriBahanBaku.add(new BahanBakuStok("Sayuran", 70));
        inventoriBahanBaku.add(new BahanBakuStok("Kentang", 60));
        inventoriBahanBaku.add(new BahanBakuStok("Minuman", 100));
    }
    
    public void initKaryawan() {
        daftarKaryawan.add(new Koki("Bima", "K001", 3000000, 3));
        daftarKaryawan.add(new Kasir("Santi", "C001", 2500000, 5));
        daftarKaryawan.add(new Koki("Rudi", "K002", 3200000, 4));
        System.out.println("Karyawan telah diinisialisasi (" + daftarKaryawan.size() + " orang).");
    }
    
    public void initMenu() {
        menuRestoran = new ArrayList<>();
        menuRestoran.add(new Makanan("Burger Klasik", 25000, 10, List.of("Daging", "Roti", "Sayuran")));
        menuRestoran.add(new Makanan("Kentang Goreng", 15000, 5, List.of("Kentang")));
        menuRestoran.add(new Makanan("Minuman Soda", 10000, 1, List.of("Minuman")));
        menuRestoran.add(new Makanan("Burger Keju", 30000, 12, List.of("Daging", "Roti", "Sayuran")));
    }
    
    public List<Makanan> getMenuRestoran() {
        return menuRestoran;
    }

    /** Helper: Mendapatkan objek BahanBakuStok dari inventori berdasarkan nama. */
    public BahanBakuStok getStok(String namaBahan) {
        return inventoriBahanBaku.stream()
                .filter(b -> b.getNama().equals(namaBahan))
                .findFirst()
                .orElse(null);
    }

    /** Restoran menerima pesanan baru dari Pelanggan, menambahkannya ke Queue. */
    public void terimaPesanan(Pesanan pesanan) {
        antrianPesanan.offer(pesanan); // offer() untuk Queue (Menambahkan di belakang)
        daftarPelanggan.add(pesanan.getPelanggan());
        System.out.println("Pesanan ID " + pesanan.getId() + " ditambahkan ke antrian. (Ukuran Queue: " + antrianPesanan.size() + ")");
    }
    
    public void lakukanPembayaran(Pesanan pesanan) {
        this.keuangan += pesanan.getTotalHarga();
    }
    
    /** Memeriksa ketersediaan bahan baku (menggunakan ArrayList stok). */
    public boolean cekStok(List<String> bahan) {
        for (String b : bahan) {
            BahanBakuStok stokItem = getStok(b);
            if (stokItem == null || stokItem.getJumlah() <= 0) {
                return false;
            }
        }
        return true;
    }
    
    /** Mengurangi stok (menggunakan ArrayList stok). */
    public void kurangiStok(List<String> bahan) {
        for (String b : bahan) {
            BahanBakuStok stokItem = getStok(b);
            if (stokItem != null) {
                stokItem.setJumlah(stokItem.getJumlah() - 1);
            }
        }
    }

    /** Menambah stok bahan baku (menggunakan ArrayList stok dan memengaruhi keuangan). */
    public void tambahStok(String bahan, int jumlah, double biayaPerUnit) {
        BahanBakuStok stokItem = getStok(bahan);
        if (stokItem != null) {
            stokItem.setJumlah(stokItem.getJumlah() + jumlah);
        } else {
            inventoriBahanBaku.add(new BahanBakuStok(bahan, jumlah));
        }
        
        this.keuangan -= (jumlah * biayaPerUnit);
        System.out.printf("Stok %s ditambahkan sebanyak %d. Biaya: Rp%.2f. Saldo baru: Rp%.2f\n", 
                          bahan, jumlah, (jumlah * biayaPerUnit), this.keuangan);
    }

    public void hitungKepuasanPelanggan(int skorReview) {
        double perubahan = (skorReview - 3) * 2.0; 
        this.reputasi = Math.max(0, Math.min(100, this.reputasi + perubahan));
        System.out.printf("Reputasi restoran berubah %.2f. Reputasi saat ini: %.2f/100.\n", perubahan, this.reputasi);
    }
    
    /** Menyajikan pesanan yang sudah selesai dimasak. */
    public void sajikanPesanan(Pesanan pesanan) {
        pesanan.setWaktuSelesai(System.currentTimeMillis());
        // Penghapusan dari antrian dilakukan di main loop menggunakan poll()
        riwayatPesanan.add(pesanan);
        
        System.out.println("\n🎉 Pesanan ID " + pesanan.getId() + " disajikan kepada " + pesanan.getPelanggan().getNama() + "!");
        int review = pesanan.getPelanggan().tinggalkanReview(pesanan);
        hitungKepuasanPelanggan(review);
    }

    /** Menampilkan laporan keuangan dan operasional akhir. */
    public void laporanKeuangan() {
        long totalPelangganTerlayani = riwayatPesanan.stream().filter(p -> !p.isDibatalkan()).count();
        double totalPendapatan = riwayatPesanan.stream().filter(p -> !p.isDibatalkan()).mapToDouble(Pesanan::getTotalHarga).sum();
        
        System.out.println("\n=======================================================");
        System.out.println("            LAPORAN AKHIR SIMULASI " + namaRestoran.toUpperCase());
        System.out.println("=======================================================");
        System.out.printf("Total Pendapatan (Pesanan Sukses) : Rp%.2f\n", totalPendapatan);
        System.out.printf("Saldo Keuangan Akhir              : Rp%.2f\n", this.keuangan);
        System.out.println("Jumlah Pelanggan Terlayani        : " + totalPelangganTerlayani);
        System.out.println("Jumlah Pesanan Dibatalkan         : " + (riwayatPesanan.size() - totalPelangganTerlayani));
        System.out.printf("Skor Reputasi Akhir               : %.2f/100\n", this.reputasi);
        System.out.println("Stok Bahan Baku Tersisa           : " + inventoriBahanBaku);
        System.out.println("Gaji Total Karyawan (Tahun/Bulan) :");
        
        double totalGaji = daftarKaryawan.stream().mapToDouble(Karyawan::hitungGaji).sum();
        System.out.printf("- Total Gaji Karyawan             : Rp%.2f (per bulan)\n", totalGaji);
        System.out.println("=======================================================");
    }
}
