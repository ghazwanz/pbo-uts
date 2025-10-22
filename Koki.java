public class Koki extends Karyawan {
    private int levelMemasak;

    public Koki(String nama, String idKaryawan, double gaji, int levelMemasak) {
        super(nama, idKaryawan, gaji);
        this.levelMemasak = levelMemasak;
    }

    public int getLevelMemasak() { return levelMemasak; }

    /**
     * Polimorfisme: Implementasi spesifik Koki untuk metode bekerja().
     */
    @Override
    public void bekerja(Pesanan pesanan, RestoranCepatSaji restoran) {
        System.out.println("\n--- Koki " + nama + " mulai bekerja pada Pesanan ID " + pesanan.getId() + " ---");
        
        boolean stokCukup = true;
        for (Makanan makanan : pesanan.getListMakanan()) {
            if (!restoran.cekStok(makanan.getBahanBaku())) {
                stokCukup = false;
                break;
            }
        }
        
        if (stokCukup) {
            System.out.println("Semua bahan tersedia. Memulai memasak...");
            try {
                int totalWaktu = pesanan.getTotalWaktuMemasak();
                
                for (Makanan makanan : pesanan.getListMakanan()) {
                    makanan.setStatus(StatusMakanan.SEDANG_DIMASAK);
                    restoran.kurangiStok(makanan.getBahanBaku());
                }
                
                long waktuMemasakMs = (long) (totalWaktu / (double)levelMemasak * 100); 
                System.out.println("Est. waktu memasak: " + (double)waktuMemasakMs/1000 + " detik (dipercepat)");
                
                Thread.sleep(waktuMemasakMs); 
                
                for (Makanan makanan : pesanan.getListMakanan()) {
                    makanan.sajikan(); 
                }
                
                System.out.println("Pesanan ID " + pesanan.getId() + " telah Selesai Dimasak.");
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Proses memasak terganggu.");
            }
        } else {
            System.out.println("❌ Gagal Memasak! Stok bahan baku untuk Pesanan ID " + pesanan.getId() + " TIDAK CUKUP.");
            pesanan.setDibatalkan(true);
        }
    }
    
    /**
     * Polimorfisme: Overriding hitungGaji() untuk bonus berdasarkan level.
     */
    @Override
    public double hitungGaji() {
        return super.hitungGaji() + (levelMemasak * 100000.0); // Bonus level
    }
}
