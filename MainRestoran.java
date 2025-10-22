import java.util.List;
import java.util.Random;

public class MainRestoran {
    public static void main(String[] args) {
        System.out.println("STARTING: Simulasi Restoran Cepat Saji...");
        
        // 1. Inisialisasi Restoran
        RestoranCepatSaji mcDewa = new RestoranCepatSaji("McDewa", 10000000.0);
        
        // Dapatkan Koki dan Kasir (untuk menggerakkan simulasi)
        Koki kokiUtama = (Koki) mcDewa.daftarKaryawan.stream().filter(k -> k instanceof Koki).findFirst().orElse(null);
        Kasir kasirUtama = (Kasir) mcDewa.daftarKaryawan.stream().filter(k -> k instanceof Kasir).findFirst().orElse(null);
        
        if (kokiUtama == null || kasirUtama == null) {
            System.err.println("Error: Karyawan tidak ditemukan untuk simulasi.");
            return;
        }

        List<String> namaPelanggan = List.of("Joko", "Susi", "Ahmad", "Budi", "Citra", "Dewi", "Eko");
        Random rand = new Random();
        int jumlahPelangganMaks = 5; 

        // =========================================================================
        // PHASE 1: KEDATANGAN PELANGGAN & PEMESANAN (MENGISI QUEUE)
        // =========================================================================
        System.out.println("\n=======================================================");
        System.out.println("PHASE 1: PELANGGAN DATANG DAN MEMESAN (MENGISI QUEUE)");
        System.out.println("=======================================================");
        
        for (int i = 0; i < jumlahPelangganMaks; i++) {
            Pelanggan pelangganBaru = new Pelanggan(namaPelanggan.get(rand.nextInt(namaPelanggan.size())) + "-" + (i + 1));
            Pesanan pesananBaru = pelangganBaru.pesanMakanan(mcDewa.getMenuRestoran());
            
            // Masukkan ke Queue menggunakan offer()
            mcDewa.terimaPesanan(pesananBaru);
            
            // Simulasi jeda antara kedatangan pelanggan
            try {
                Thread.sleep(300 + rand.nextInt(500));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // =========================================================================
        // PHASE 2: PROSES ANTRIAN (FIFO) DIMULAI
        // =========================================================================
        System.out.println("\n=======================================================");
        System.out.println("PHASE 2: PROSES ANTRIAN (FIFO) DIMULAI");
        System.out.println("=======================================================");
        
        while (!mcDewa.antrianPesanan.isEmpty()) {
            // Mengambil pesanan dari depan antrian menggunakan poll() (FIFO)
            Pesanan pesananDiproses = mcDewa.antrianPesanan.poll(); 
            
            if (pesananDiproses == null) {
                break; 
            }
            
            System.out.println("\n--- MEMPROSES PESANAN ID: " + pesananDiproses.getId() + " (Queue Sisa: " + mcDewa.antrianPesanan.size() + ") ---");
            
            // C. Pengelolaan Pesanan & Pembayaran (oleh Kasir)
            kasirUtama.bekerja(pesananDiproses, mcDewa); 
            
            // D. Proses Memasak (oleh Koki)
            kokiUtama.bekerja(pesananDiproses, mcDewa); 
            
            // E. Penyelesaian & Review
            if (!pesananDiproses.isDibatalkan() && 
                pesananDiproses.getListMakanan().stream().allMatch(m -> m.getStatusMakanan() == StatusMakanan.SIAP_SAJI)) {
                mcDewa.sajikanPesanan(pesananDiproses);
            } else if (pesananDiproses.isDibatalkan()) {
                System.out.println("Pesanan ID " + pesananDiproses.getId() + " dibatalkan karena masalah stok.");
                mcDewa.riwayatPesanan.add(pesananDiproses); 
                mcDewa.hitungKepuasanPelanggan(0); 
            }

            // Simulasi penanganan stok habis
            if (pesananDiproses.getId() == 3) {
                BahanBakuStok dagingStok = mcDewa.getStok("Daging");
                if (dagingStok != null && dagingStok.getJumlah() < 5) {
                    System.out.println("\n*** Peringatan Stok Rendah: Daging. ***");
                    mcDewa.tambahStok("Daging", 20, 15000.0); 
                }
            }
        }

        // 3. Laporan Akhir
        System.out.println("\n=======================================================");
        System.out.println("SIMULASI SELESAI");
        mcDewa.laporanKeuangan();
    }
}
