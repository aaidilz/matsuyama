import java.util.Scanner;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ServiceLinkedList {
    private ServiceNode head;
    private static final String ARSIP_FILE = "data_service.txt";
    private static final String DELIMITER = "|";

    public ServiceLinkedList() {
        this.head = null;
        muatDariArsip();
    }

    // Fungsi size untuk menghitung total dan per status
    public void statistik() {

        int total = 0;
        int pending = 0;
        int onGoing = 0;
        int completed = 0;

        ServiceNode current = head;
        while (current != null) {
            total++;

            switch (current.getStatus()) {
                case "pending":
                    pending++;
                    break;
                case "on_going":
                    onGoing++;
                    break;
                case "completed":
                    completed++;
                    break;
            }
            current = current.getNext();
        }

        // Tampilkan hasil
        System.out.println("\n===== Statistik Layanan =====");
        System.out.println("Total data      : " + total);
        System.out.println("Pending         : " + pending);
        System.out.println("On Going        : " + onGoing);
        System.out.println("Completed       : " + completed);
        System.out.println("=============================");
    }

    // Fungsi tambahDataService untuk menambahkan data service baru
    // ======================= TAMBAH DATA =======================
    public void tambahDataService(Scanner scanner) {
        try {
            System.out.println("\n=== Tambah Data Service ===");
            System.out.println("Masukkan nama pelanggan:");
            String nama = scanner.nextLine();

            System.out.println("Masukkan jenis perangkat:");
            String perangkat = scanner.nextLine();

            System.out.println("Masukkan deskripsi masalah:");
            String masalah = scanner.nextLine();

            String difficulty;
            while (true) {
                System.out.println("Masukkan difficulty (mudah, menengah, sulit):");
                difficulty = scanner.nextLine().toLowerCase();
                if (difficulty.equals("mudah") || difficulty.equals("menengah") || difficulty.equals("sulit")) {
                    break;
                } else {
                    System.out.println("difficulty tidak valid!");
                }
            }

            System.out.println("Masukkan biaya servis:");
            double biaya = 0;
            while (true) {
                try {
                    biaya = Double.parseDouble(scanner.nextLine());
                    if (biaya < 0)
                        throw new NumberFormatException();
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Masukkan angka yang valid:");
                }
            }

            ServiceNode newNode = new ServiceNode(nama, perangkat, masalah, biaya, difficulty);
            newNode.setServiceId(generateNewId());

            // Penambahan berdasarkan difficulty

            // check apakah arsip sudah ada, jika belum buat baru
            if (head == null) {
                head = newNode;

                // kalau difficulty adalah "mudah", langsung simpan ke head paling depan
            } else if (difficulty.equals("mudah")) {
                newNode.setNext(head);
                head = newNode;

                // kalau difficulty adalah "menengah".
            } else if (difficulty.equals("menengah")) {
                ServiceNode current = head;
                ServiceNode prev = null;

                // Jalan terus ke depan sampai menemukan node dengan difficulty "sulit"
                while (current != null && !current.getDifficulty().equals("sulit")) {
                    prev = current;
                    current = current.getNext();
                }

                if (prev == null) {
                    newNode.setNext(head);
                    head = newNode;
                } else {
                    newNode.setNext(current);
                    prev.setNext(newNode);
                }
                // Karena ini yang paling berat, maka kita tempatkan pelanggan ini di paling
                // akhir.
            } else {
                ServiceNode current = head;
                while (current.getNext() != null) {
                    current = current.getNext();
                }
                current.setNext(newNode);
            }

            simpanKeArsip();
            System.out.println("\nData berhasil ditambahkan!");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Fungsi generateNewId untuk menghasilkan ID baru
    private int generateNewId() {
        int maxId = 0;
        ServiceNode current = head;
        while (current != null) {
            if (current.getServiceId() > maxId) {
                maxId = current.getServiceId();
            }
            current = current.getNext();
        }
        return maxId + 1;
    }

    // Fungsi untuk menyimpan data ke file arsip

    public void simpanKeArsip() {
        if (head == null) {
            System.out.println("\nMemori kosong, tidak ada data untuk disimpan.");
            // Jika ingin mengosongkan file juga, bisa tambahkan logic di sini
            try (FileWriter writer = new FileWriter(ARSIP_FILE, false)) {
                // File akan dikosongkan
            } catch (IOException e) {
                System.err.println("Gagal mengosongkan file arsip: " + e.getMessage());
            }
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARSIP_FILE, false))) { // false untuk overwrite
            ServiceNode current = head;
            while (current != null) {
                String data = current.getServiceId() + DELIMITER +
                        current.getCustomerName() + DELIMITER +
                        current.getDeviceType() + DELIMITER +
                        current.getProblemDescription() + DELIMITER +
                        current.getServiceDate() + DELIMITER +
                        current.getCost() + DELIMITER +
                        current.getStatus() + DELIMITER +
                        current.getDifficulty();
                writer.write(data);
                writer.newLine();
                current = current.getNext();
            }
            System.out.println("\nData dari memori berhasil disimpan ke dalam file arsip!");
        } catch (IOException e) {
            System.err.println("Gagal menyimpan data ke arsip: " + e.getMessage());
        }
    }

    // Fungsi untuk membaca semua data dari file arsip
    public void bacaFile() {
        try (FileReader reader = new FileReader(ARSIP_FILE);
                BufferedReader bufferedReader = new BufferedReader(reader)) {

            String line;
            System.out.println("=== DATA ARSIP SERVICE ===");
            System.out.println();

            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split("\\" + DELIMITER);
                if (data.length == 8) {
                    System.out.println("ID: " + data[0]);
                    System.out.println("Nama Pelanggan: " + data[1]);
                    System.out.println("Jenis Perangkat: " + data[2]);
                    System.out.println("Deskripsi Masalah: " + data[3]);
                    System.out.println("Tanggal Servis: " + data[4]);
                    System.out.println("Biaya Servis: Rp" + data[5]);
                    System.out.println("Status: " + data[6]);
                    System.out.println("difficulty: " + data[7]);
                    System.out.println("-----------------------------");
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File arsip belum ada. Akan dibuat saat ada data baru.");
        } catch (IOException e) {
            System.err.println("Error saat membaca file arsip: " + e.getMessage());
        }
    }


    // Helper untuk ambil data service di arsip, lalu simpan ke head
    private void muatDariArsip() {
        // Bersihkan head sebelum memuat ulang dari file
        head = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(ARSIP_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\" + DELIMITER);
                if (data.length == 8) {
                    try {
                        int id = Integer.parseInt(data[0]);
                        String nama = data[1];
                        String perangkat = data[2];
                        String masalah = data[3];
                        LocalDate tanggal = LocalDate.parse(data[4]); // Konversi dari String ke LocalDate
                        double biaya = Double.parseDouble(data[5]);
                        String status = data[6];
                        String difficulty = data[7];

                        ServiceNode newNode = new ServiceNode(nama, perangkat, masalah, biaya, difficulty);
                        newNode.setServiceId(id);
                        newNode.setServiceDate(tanggal);
                        newNode.setStatus(status);

                        if (head == null) {
                            head = newNode;
                        } else {
                            ServiceNode current = head;
                            while (current.getNext() != null) {
                                current = current.getNext();
                            }
                            current.setNext(newNode);
                        }

                    } catch (NumberFormatException | DateTimeParseException e) {
                        System.err.println("Lewati baris rusak: " + line);
                    }
                }
            }

            System.out.println("Data berhasil dimuat dari arsip ke memori.");
        } catch (IOException e) {
            System.err.println("Gagal memuat data dari arsip: " + e.getMessage());
        }
    }

    // ======================= HAPUS DATA =======================
    private void hapusHead() {
        if (head != null) {
            head = head.getNext();
            simpanKeArsip();
            System.out.println("Data berhasil dihapus (head)!");
        }
    }

    private void hapusMid(ServiceNode before, ServiceNode target) {
        if (before != null && target != null) {
            before.setNext(target.getNext()); // Lewati node target
            simpanKeArsip();
            System.out.println("Data berhasil dihapus (mid)!");
        }
    }

    private void hapusTail() {
        if (head == null)
            return;

        ServiceNode current = head;
        ServiceNode before = null;

        while (current.getNext() != null) {
            before = current;
            current = current.getNext();
        }

        if (before == null) {
            head = null;
        } else {
            before.setNext(null);
        }
        simpanKeArsip();
        System.out.println("Data berhasil dihapus (tail)!");
    }

    public void hapusById(Scanner scanner) {
        if (head == null) {
            System.out.println("\nData kosong!");
            return;
        }

        System.out.print("\nMasukkan ID service: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID tidak valid!");
            return;
        }

        ServiceNode current = head;
        ServiceNode before = null;

        while (current != null) {
            if (current.getServiceId() == id) {
                System.out.println("\nData ditemukan:");
                System.out.println(current);
                System.out.print("Yakin ingin menghapus? (y/n): ");
                if (!scanner.nextLine().equalsIgnoreCase("y")) {
                    System.out.println("Penghapusan dibatalkan");
                    return;
                }

                if (current == head && current.getNext() == null) {
                    head = null;
                    simpanKeArsip();
                    System.out.println("Data berhasil dihapus!");
                } else if (current == head) {
                    hapusHead();
                } else if (current.getNext() == null) {
                    hapusTail();
                } else {
                    hapusMid(before, current);
                }
                return;
            }
            before = current;
            current = current.getNext();
        }

        System.out.println("Data tidak ditemukan!");
    }

    public void hapusAll(Scanner scanner) {
        if (head == null) {
            System.out.println("\nData kosong!");
            return;
        }

        System.out.print("\nYakin ingin menghapus semua data? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            head = null;
            // simpanKeArsip();
            System.out.println("Semua data berhasil dihapus!");
        } else {
            System.out.println("Penghapusan dibatalkan");
        }
    }

    public void updateDataService(Scanner scanner) {
        System.out.println("\n=== Update Data Service ===");

        if (head == null) {
            System.out.println("Data kosong!");
            return;
        }

        System.out.print("Masukkan ID service: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID tidak valid!");
            return;
        }

        ServiceNode current = head;
        while (current != null) {
            if (current.getServiceId() == id) {
                System.out.println("\nData ditemukan:");
                System.out.println(current);

                System.out.println("\nMasukkan data baru (kosongkan jika tidak berubah):");
                System.out.print("Nama [" + current.getCustomerName() + "]: ");
                String nama = scanner.nextLine();
                if (!nama.isEmpty()) current.setCustomerName(nama);

                System.out.print("Perangkat [" + current.getDeviceType() + "]: ");
                String perangkat = scanner.nextLine();
                if (!perangkat.isEmpty()) 
                    current.setDeviceType(perangkat);

                System.out.print("Masalah [" + current.getProblemDescription() + "]: ");
                String masalah = scanner.nextLine();
                if (!masalah.isEmpty()) current.setProblemDescription(masalah);

                System.out.print("Biaya [" + current.getCost() + "]: ");
                String biayaStr = scanner.nextLine();
                if (!biayaStr.isEmpty()) {
                    try {
                        double biaya = Double.parseDouble(biayaStr);
                        if (biaya >= 0) {
                            current.setCost(biaya);
                        } else {
                            System.out.println("Biaya tidak valid, data tidak diubah.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Input tidak valid, data tidak diubah.");
                    }
                }

                System.out.println("Status saat ini: " + current.getStatus());
                System.out.println("Pilih status baru:");
                System.out.println("1. pending");
                System.out.println("2. on_going");
                System.out.println("3. completed");
                System.out.print("Pilihan (1-3): ");
                String statusPilihan = scanner.nextLine();
                switch (statusPilihan) {
                    case "1":
                        current.setStatus("pending");
                        break;
                    case "2":
                        current.setStatus("on_going");
                        break;
                    case "3":
                        current.setStatus("completed");
                        break;
                    default:
                        System.out.println("Status tidak berubah");
                }
                System.out.println("\nData berhasil diupdate!");
                return;
            }
            current = current.getNext();
        }
        System.out.println("Data tidak ditemukan!");
    }

    // ======================= FILTER DATA =======================
    public void filterByStatus(Scanner scanner) {
        System.out.println("\n=== Filter Berdasarkan Status ===");
        System.out.println("1. pending");
        System.out.println("2. on_going");
        System.out.println("3. completed");
        System.out.print("Pilih status (1-3): ");

        String status;
        switch (scanner.nextLine()) {
            case "1":
                status = "pending";
                break;
            case "2":
                status = "on_going";
                break;
            case "3":
                status = "completed";
                break;
            default:
                System.out.println("Pilihan tidak valid!");
                return;
        }

        ServiceNode current = head;
        boolean ditemukan = false;
        int count = 0;

        System.out.println("\nHasil Filter:");
        while (current != null) {
            if (current.getStatus().equals(status)) {
                System.out.println(current);
                System.out.println("-------------------");
                ditemukan = true;
                count++;
            }
            current = current.getNext();
        }

        if (!ditemukan) {
            System.out.println("Tidak ada data dengan status " + status);
        } else {
            System.out.println("Total ditemukan: " + count);
        }
    }

    public void filterByDifficulty(Scanner scanner) {
        System.out.println("\n=== Filter Berdasarkan difficulty ===");
        System.out.println("1. mudah");
        System.out.println("2. menengah");
        System.out.println("3. sulit");
        System.out.print("Pilih difficulty (1-3): ");

        String difficulty;
        switch (scanner.nextLine()) {
            case "1":
                difficulty = "mudah";
                break;
            case "2":
                difficulty = "menengah";
                break;
            case "3":
                difficulty = "sulit";
                break;
            default:
                System.out.println("Pilihan tidak valid!");
                return;
        }

        ServiceNode current = head;
        boolean ditemukan = false;
        int count = 0;

        System.out.println("\nHasil Filter:");
        while (current != null) {
            if (current.getDifficulty().equals(difficulty)) {
                System.out.println(current);
                System.out.println("-------------------");
                ditemukan = true;
                count++;
            }
            current = current.getNext();
        }

        if (!ditemukan) {
            System.out.println("Tidak ada data dengan difficulty " + difficulty);
        } else {
            System.out.println("Total ditemukan: " + count);
        }
    }

    public void filterData(Scanner scanner) {
        System.out.println("\n=== Filter Data Service ===");
        System.out.println("1. Berdasarkan Status");
        System.out.println("2. Berdasarkan difficulty");
        System.out.print("Pilih filter (1-2): ");

        String pilihan = scanner.nextLine();
        switch (pilihan) {
            case "1":
                filterByStatus(scanner);
                break;
            case "2":
                filterByDifficulty(scanner);
                break;
            default:
                System.out.println("Pilihan tidak valid!");
        }
    }

    public void hapusMenu(Scanner scanner) {
        System.out.println("\n=== Hapus Data Service ===");
        System.out.println("1. Hapus berdasarkan ID");
        System.out.println("2. Hapus semua data");
        System.out.print("Pilih opsi (1-2): ");

        String pilihan = scanner.nextLine();
        switch (pilihan) {
            case "1":
                hapusById(scanner);
                break;
            case "2":
                hapusAll(scanner);
                break;
            default:
                System.out.println("Pilihan tidak valid!");
        }
    }

    public void mainMenu() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        System.out.println("\n[Shutdown Hook] Program akan berhenti, menyimpan data secara otomatis...");
        simpanKeArsip();
        System.out.println("[Shutdown Hook] Data berhasil disimpan.");
        }));

        System.out.println("Selamat datang di Sistem Pencatatan Service!");
        System.out.println("Data akan disimpan secara otomatis saat program ditutup.");
        System.out.println("Silakan pilih menu berikut untuk melanjutkan:");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== SISTEM PENCATATAN DATA SERVICE ===");
            System.out.println("1. Tambah Data Service");
            System.out.println("2. Baca File Arsip");
            System.out.println("3. Statistik Service");
            System.out.println("4. Hapus Data Service");
            System.out.println("5. Update Data Service");
            System.out.println("6. Filter Data Service");
            System.out.println("7. Simpan Perubahan ke Arsip");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");
            int pilihan = 0;
            try {
                pilihan = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Pilihan tidak valid!");
                continue;
            }

            switch (pilihan) {
                case 1:
                    System.out.println("======");
                    this.tambahDataService(scanner);
                    break;
                case 2:
                    System.out.println("======");
                    this.bacaFile();
                    break;
                case 3:
                    System.out.println("======");
                    this.statistik();
                    break;
                case 4:
                    System.out.println("======");
                    this.hapusMenu(scanner);
                    break;
                case 5:
                    System.out.println("======");
                    this.updateDataService(scanner);
                    break;
                case 6:
                    System.out.println("======");
                    this.filterData(scanner);
                    break;
                case 7:
                    System.out.println("======");
                    this.simpanKeArsip();
                    break;
                case 0:
                    System.out.println("======");
                    System.out.println("Terima kasih!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }
}
