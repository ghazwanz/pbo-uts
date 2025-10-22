public class Kasir extends Karyawan {
    private int kecepatanMelayani;

    public Kasir(String nama, String idKaryawan, double gaji, int kecepatanMelayani) {
        super(nama, idKaryawan, gaji);
        this.kecepatanMelayani = kecepatanMelayani;
    }

    /**
     * Polimorfisme: Implementasi spesifik Kasir untuk metode bekerja().
     */
    @Override
    public void bekerja(Pesanan pesanan, RestoranCepatSaji restoran) {
        System.out.println("\n--- Kasir " + nama + " memproses Pesanan ID " + pesanan.getId() + " ---");
        
        try {
            Thread.sleep(1000 / kecepatanMelayani);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        restoran.lakukanPembayaran(pesanan);
        System.out.printf("Pembayaran Rp%.2f diterima. Pesanan diteruskan ke Koki.\n", pesanan.getTotalHarga());
    }
    
    /**
     * Polimorfisme: Overriding hitungGaji() untuk bonus berdasarkan kecepatan.
     */
    @Override
    public double hitungGaji() {
        return super.hitungGaji() + (kecepatanMelayani * 50000.0); // Bonus kecepatan
    }
}