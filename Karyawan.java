public abstract class Karyawan {
    protected String nama;
    protected String idKaryawan;
    protected double gaji;

    public Karyawan(String nama, String idKaryawan, double gaji) {
        this.nama = nama;
        this.idKaryawan = idKaryawan;
        this.gaji = gaji;
    }

    public String getNama() { return nama; }
    public double getGaji() { return gaji; }

    // Polimorfisme: Metode abstrak yang akan di-Override oleh subkelas.
    public abstract void bekerja(Pesanan pesanan, RestoranCepatSaji restoran);
    
    // Polimorfisme: Metode Overriding dasar
    public double hitungGaji() {
        return this.gaji;
    }
}