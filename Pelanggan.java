import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Pelanggan {
    private String nama;
    private int tingkatKesabaran; 
    private Pesanan pesananSaatIni;
    private long waktuKedatangan;
    private Random random = new Random();

    public Pelanggan(String nama) {
        this.nama = nama;
        this.tingkatKesabaran = 5 + random.nextInt(11); // Kesabaran 5-15 detik
        this.waktuKedatangan = System.currentTimeMillis();
    }

    public String getNama() { return nama; }
    public int getTingkatKesabaran() { return tingkatKesabaran; }
    public long getWaktuKedatangan() { return waktuKedatangan; }
    public Pesanan getPesananSaatIni() { return pesananSaatIni; }
    public void setPesananSaatIni(Pesanan pesananSaatIni) { this.pesananSaatIni = pesananSaatIni; }

    /**
     * Pelanggan membuat Pesanan.
     */
    public Pesanan pesanMakanan(List<Makanan> menu) {
        int jumlahItem = random.nextInt(2) + 1; 
        List<Makanan> itemPesanan = new ArrayList<>();
        
        System.out.print("\n-> " + nama + " (Sabar: " + tingkatKesabaran + "s) memesan: ");
        for (int i = 0; i < jumlahItem; i++) {
            Makanan itemDipilih = menu.get(random.nextInt(menu.size()));
            itemPesanan.add(new Makanan(itemDipilih.getNama(), itemDipilih.getHarga(), 
                                        itemDipilih.getWaktuPersiapan(), itemDipilih.getBahanBaku()));
            System.out.print(itemDipilih.getNama() + (i < jumlahItem - 1 ? ", " : ""));
        }
        System.out.println(".");
        
        Pesanan pesanan = new Pesanan(this, itemPesanan);
        this.pesananSaatIni = pesanan;
        return pesanan;
    }
    
    /**
     * Pelanggan meninggalkan review berdasarkan waktu tunggu dan status pesanan.
     */
    public int tinggalkanReview(Pesanan pesanan) {
        if (pesanan.isDibatalkan()) {
            System.out.println("Review " + nama + ": Sangat buruk! Pesanan dibatalkan. (-5 poin reputasi)");
            return 0; 
        }
        
        long waktuTungguTotal = (pesanan.getWaktuSelesai() - pesanan.getWaktuPesan()) / 1000; 

        double skorRelatif = (double) (tingkatKesabaran + pesanan.getTotalWaktuMemasak()) / waktuTungguTotal;
        
        int skorReview;
        String komentar;

        if (skorRelatif >= 1.5) {
            skorReview = 5;
            komentar = "Sangat cepat dan enak!";
        } else if (skorRelatif >= 1.0) {
            skorReview = 4;
            komentar = "Pelayanan baik dan sesuai harapan.";
        } else if (skorRelatif >= 0.7) {
            skorReview = 3;
            komentar = "Agak lama, tapi makanan lezat.";
        } else {
            skorReview = 1;
            komentar = "Sangat lama dan membuat saya kesal.";
        }
        
        System.out.println("Review " + nama + ": Waktu tunggu " + waktuTungguTotal + "s. Skor: " + skorReview + "/5. (" + komentar + ")");
        return skorReview;
    }
}
