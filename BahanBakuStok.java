public class BahanBakuStok {
    private String nama;
    private int jumlah;

    public BahanBakuStok(String nama, int jumlah) {
        this.nama = nama;
        this.jumlah = jumlah;
    }

    public String getNama() { return nama; }
    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }

    @Override
    public String toString() {
        return nama + ": " + jumlah;
    }
}
