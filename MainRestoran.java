import java.util.List;
import java.util.Scanner;

public class MainRestoran {
    static RestoranCepatSaji restoran;
    static Koki kokiUtama;
    static Kasir kasirUtama;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║        SISTEM MANAJEMEN RESTORAN CEPAT SAJI        ║");
        System.out.println("╚════════════════════════════════════════════════════╝\n");

        // Inisialisasi Restoran
        System.out.print("Masukkan nama restoran: ");
        String namaRestoran = scanner.nextLine();

        System.out.print("Masukkan modal awal (Rp): ");
        double modalAwal = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        restoran = new RestoranCepatSaji(namaRestoran, modalAwal);

        // Dapatkan Koki dan Kasir
        Koki kokiDitemukan = null;
        for (Karyawan k : restoran.daftarKaryawan) {
            if (k instanceof Koki) {
                kokiDitemukan = (Koki) k;
                break;
            }
        }
        kokiUtama = kokiDitemukan;

        Kasir kasirDitemukan = null;
        for (Karyawan k : restoran.daftarKaryawan) {
            if (k instanceof Kasir) {
                kasirDitemukan = (Kasir) k;
                break;
            }
        }
        kasirUtama = kasirDitemukan;

        if (kokiUtama == null || kasirUtama == null) {
            System.err.println("Error: Karyawan tidak ditemukan.");
            return;
        }

        System.out.println("\n✓ Restoran '" + namaRestoran + "' berhasil diinisialisasi!");
        System.out.println("═══════════════════════════════════════════════════════\n");

        // Menu Utama dengan do-while
        int pilihan;
        do {
            tampilkanMenu();
            pilihan = inputInteger("Pilih menu (1-7): ");
            scanner.nextLine(); // consume newline

            System.out.println(); // spacing

            switch (pilihan) {
                case 1:
                    tambahPelangganDanPesanan();
                    break;
                case 2:
                    prosesPesanan();
                    break;
                case 3:
                    lihatStatusRestoran();
                    break;
                case 4:
                    kelolaBahanBaku();
                    break;
                case 5:
                    lihatMenu();
                    break;
                case 6:
                    laporanAkhir();
                    break;
                case 7:
                    System.out.println("╔════════════════════════════════════════════════════╗");
                    System.out.println("║     Terima kasih telah menggunakan sistem ini!     ║");
                    System.out.println("╚════════════════════════════════════════════════════╝");
                    break;
                default:
                    System.out.println("❌ Pilihan tidak valid! Silakan coba lagi.\n");
            }

        } while (pilihan != 7);

        scanner.close();
    }

    private static void tampilkanMenu() {
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║                 MENU UTAMA RESTORAN                ║");
        System.out.println("╠════════════════════════════════════════════════════╣");
        System.out.println("║  1. Tambah Pelanggan dan Pesanan                   ║");
        System.out.println("║  2. Proses Pesanan di Antrian (FIFO)               ║");
        System.out.println("║  3. Lihat Status Restoran                          ║");
        System.out.println("║  4. Kelola Bahan Baku                              ║");
        System.out.println("║  5. Lihat Menu Makanan                             ║");
        System.out.println("║  6. Laporan Akhir                                  ║");
        System.out.println("║  7. Keluar                                         ║");
        System.out.println("╚════════════════════════════════════════════════════╝");
    }

    private static void tambahPelangganDanPesanan() {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("         TAMBAH PELANGGAN DAN PESANAN");
        System.out.println("═══════════════════════════════════════════════════════");

        System.out.print("Masukkan nama pelanggan: ");
        String namaPelanggan = scanner.nextLine();

        Pelanggan pelanggan = new Pelanggan(namaPelanggan);

        // Pilih makanan untuk pesanan
        System.out.println("\n--- Menu Tersedia ---");
        List<Makanan> menu = restoran.getMenuRestoran();
        for (int i = 0; i < menu.size(); i++) {
            Makanan m = menu.get(i);
            System.out.printf("%d. %s - Rp%.2f (Waktu masak: %ds)\n",
                    i + 1, m.getNama(), m.getHarga(), m.getWaktuPersiapan());
        }

        // Input pesanan
        System.out.print("\nBerapa item yang ingin dipesan? ");
        int jumlahItem = inputInteger("");
        scanner.nextLine(); // consume newline

        List<Makanan> itemPesanan = new java.util.ArrayList<>();
        for (int i = 0; i < jumlahItem; i++) {
            System.out.print("Pilih menu ke-" + (i + 1) + " (1-" + menu.size() + "): ");
            int pilihan = inputInteger("") - 1;
            scanner.nextLine(); // consume newline

            if (pilihan >= 0 && pilihan < menu.size()) {
                Makanan makananDipilih = menu.get(pilihan);
                itemPesanan.add(new Makanan(
                        makananDipilih.getNama(),
                        makananDipilih.getHarga(),
                        makananDipilih.getWaktuPersiapan(),
                        makananDipilih.getBahanBaku()));
                System.out.println("✓ " + makananDipilih.getNama() + " ditambahkan.");
            } else {
                System.out.println("❌ Pilihan tidak valid!");
                i--; // retry
            }
        }

        if (!itemPesanan.isEmpty()) {
            Pesanan pesanan = new Pesanan(pelanggan, itemPesanan);
            restoran.terimaPesanan(pesanan);
            System.out.println("\n✓ Pesanan berhasil ditambahkan ke antrian!");
            pesanan.displayDetail();
        }

        System.out.println("═══════════════════════════════════════════════════════\n");
    }

    private static void prosesPesanan() {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("          PROSES PESANAN DI ANTRIAN (FIFO)");
        System.out.println("═══════════════════════════════════════════════════════");

        if (restoran.antrianPesanan.isEmpty()) {
            System.out.println("ℹ Tidak ada pesanan dalam antrian.\n");
            return;
        }

        System.out.println("Jumlah pesanan dalam antrian: " + restoran.antrianPesanan.size());
        System.out.print("Berapa pesanan yang ingin diproses? (0 = semua): ");
        int jumlahProses = inputInteger("");
        scanner.nextLine(); // consume newline

        if (jumlahProses == 0) {
            jumlahProses = restoran.antrianPesanan.size();
        }

        int diproses = 0;
        while (!restoran.antrianPesanan.isEmpty() && diproses < jumlahProses) {
            Pesanan pesananDiproses = restoran.antrianPesanan.poll();

            if (pesananDiproses == null)
                break;

            System.out.println("\n--- MEMPROSES PESANAN ID: " + pesananDiproses.getId() +
                    " (Queue Sisa: " + restoran.antrianPesanan.size() + ") ---");

            // Kasir memproses pembayaran
            kasirUtama.bekerja(pesananDiproses, restoran);

            // Koki memasak
            kokiUtama.bekerja(pesananDiproses, restoran);

            // Penyelesaian
            boolean semuaMakananSiap = true;

            for (Makanan m : pesananDiproses.getListMakanan()) {
                if (m.getStatusMakanan() != StatusMakanan.SIAP_SAJI) {
                    semuaMakananSiap = false;
                    break;
                }
            }

            if (!pesananDiproses.isDibatalkan() && semuaMakananSiap) {
                restoran.sajikanPesanan(pesananDiproses);
            } else if (pesananDiproses.isDibatalkan()) {
                System.out.println("❌ Pesanan ID " + pesananDiproses.getId() +
                        " dibatalkan karena stok tidak cukup.");
                restoran.riwayatPesanan.add(pesananDiproses);
                restoran.hitungKepuasanPelanggan(0);
            }

            diproses++;
        }

        System.out.println("\n✓ Selesai memproses " + diproses + " pesanan.");
        System.out.println("═══════════════════════════════════════════════════════\n");
    }

    private static void lihatStatusRestoran() {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("             STATUS RESTORAN SAAT INI");
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.printf("Saldo Keuangan      : Rp%.2f\n", restoran.keuangan);
        System.out.printf("Reputasi            : %.2f/100\n", restoran.reputasi);
        System.out.println("Pesanan di Antrian  : " + restoran.antrianPesanan.size());
        System.out.println("Riwayat Pesanan     : " + restoran.riwayatPesanan.size());
        System.out.println("\n--- Stok Bahan Baku ---");
        for (BahanBakuStok stok : restoran.inventoriBahanBaku) {
            System.out.println("  " + stok);
        }
        System.out.println("\n--- Karyawan ---");
        for (Karyawan k : restoran.daftarKaryawan) {
            System.out.printf("  %s (Gaji: Rp%.2f)\n", k.getNama(), k.hitungGaji());
        }
        System.out.println("═══════════════════════════════════════════════════════\n");
    }

    private static void kelolaBahanBaku() {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("            KELOLA BAHAN BAKU");
        System.out.println("═══════════════════════════════════════════════════════");

        System.out.println("Stok Bahan Baku Saat Ini:");
        for (int i = 0; i < restoran.inventoriBahanBaku.size(); i++) {
            BahanBakuStok stok = restoran.inventoriBahanBaku.get(i);
            System.out.printf("%d. %s\n", i + 1, stok);
        }

        System.out.print("\nPilih bahan baku yang ingin ditambah (1-" +
                restoran.inventoriBahanBaku.size() + "): ");
        int pilihan = inputInteger("") - 1;
        scanner.nextLine(); // consume newline

        if (pilihan >= 0 && pilihan < restoran.inventoriBahanBaku.size()) {
            BahanBakuStok stokDipilih = restoran.inventoriBahanBaku.get(pilihan);

            System.out.print("Jumlah yang ingin ditambah: ");
            int jumlah = inputInteger("");
            scanner.nextLine(); // consume newline

            System.out.print("Biaya per unit (Rp): ");
            double biaya = scanner.nextDouble();
            scanner.nextLine(); // consume newline

            restoran.tambahStok(stokDipilih.getNama(), jumlah, biaya);
            System.out.println("✓ Stok berhasil ditambahkan!");
        } else {
            System.out.println("❌ Pilihan tidak valid!");
        }

        System.out.println("═══════════════════════════════════════════════════════\n");
    }

    private static void lihatMenu() {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("             MENU MAKANAN TERSEDIA");
        System.out.println("═══════════════════════════════════════════════════════");

        List<Makanan> menu = restoran.getMenuRestoran();
        for (int i = 0; i < menu.size(); i++) {
            Makanan m = menu.get(i);
            System.out.printf("%d. %s\n", i + 1, m.getNama());
            System.out.printf("   Harga: Rp%.2f | Waktu: %ds\n",
                    m.getHarga(), m.getWaktuPersiapan());
            System.out.println("   Bahan: " + String.join(", ", m.getBahanBaku()));
            System.out.println();
        }

        System.out.println("═══════════════════════════════════════════════════════\n");
    }

    private static void laporanAkhir() {
        System.out.println();
        restoran.laporanKeuangan();
        System.out.println();
    }

    // Helper method untuk input integer dengan error handling
    private static int inputInteger(String prompt) {
        while (true) {
            try {
                if (!prompt.isEmpty()) {
                    System.out.print(prompt);
                }
                return scanner.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.print("❌ Input tidak valid! Masukkan angka: ");
                scanner.nextLine(); // clear buffer
            }
        }
    }
}