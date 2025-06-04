import java.util.Scanner;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ServiceLinkedList {
    private ServiceNode head;
    private static final String ARSIP_FILE = "data_service.txt";
    private static final String DELIMITER = "|";

    // Fungsi size untuk menghitung total dan per status
    public void statistik() {
        muatDariArsip(); // Isi linked list dari file

        int total = 0;
        int pending = 0;
        int onGoing = 0;
        int completed = 0;

        ServiceNode current = head;
        while (current != null) {
            total++;
            String status = current.getStatus().toLowerCase();

            switch (status) {
                case "pending":
                    pending++;
                    break;
                case "on_going":
                case "ongoing": // jika ada variasi penulisan
                    onGoing++;
                    break;
                case "completed":
                    completed++;
                    break;
            }
            current = current.getNext();
        }

        // Tampilkan hasil
        System.out.println("===== Statistik Layanan =====");
        System.out.println("Total data      : " + total);
        System.out.println("Pending         : " + pending);
        System.out.println("On Going        : " + onGoing);
        System.out.println("Completed       : " + completed);
        System.out.println("=============================");
    }

    // Fungsi tambahDataService untuk menambahkan data service baru
    public void tambahDataService(Scanner scanner) {
        System.out.println("Masukkan nama pelanggan:");
        String nama = scanner.nextLine();

        System.out.println("Masukkan jenis perangkat:");
        String perangkat = scanner.nextLine();

        System.out.println("Masukkan deskripsi masalah:");
        String masalah = scanner.nextLine();

        String prioritas;
        while (true) {
            System.out.println("Masukkan prioritas (mudah, menengah, sulit):");
            prioritas = scanner.nextLine().toLowerCase();
            if (prioritas.equals("mudah") || prioritas.equals("menengah") || prioritas.equals("sulit")) {
            break;
            } else {
            System.out.println("Prioritas tidak valid. Harap masukkan salah satu dari: mudah, menengah, sulit.");
            }
        }

        System.out.println("Masukkan biaya servis:");
        double biaya = 0;
        while (true) {
            try {
                biaya = Double.parseDouble(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Masukkan angka valid untuk biaya servis:");
            }
        }

        // Buat node baru
        ServiceNode newNode = new ServiceNode(nama, perangkat, masalah, biaya, prioritas);

        // Set ID otomatis
        newNode.setServiceId(generateNewId());

        // Tambahkan node ke akhir linked list
        if (head == null) {
            head = newNode;
        } else {
            ServiceNode current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }

        // Otomatis simpan ke arsip
        simpanKeArsip(newNode);

        System.out.println("Data servis berhasil ditambahkan dan disimpan ke arsip");
    }

    // Fungsi generateNewId untuk menghasilkan ID baru
    private int generateNewId() {
        // Baca ID terakhir dari file arsip
        int lastId = bacaIdTerakhirDariArsip();
        return lastId + 1;
    }

    // Fungsi tampilDataService untuk menampilkan data service yang ada di memori
    public void tampilDataService() {
        if (head == null) {
            System.out.println("Tidak ada data servis.");
            return;
        }

        ServiceNode current = head;
        while (current != null) {
            System.out.println("ID: " + current.getServiceId());
            System.out.println("Nama Pelanggan: " + current.getCustomerName());
            System.out.println("Jenis Perangkat: " + current.getDeviceType());
            System.out.println("Deskripsi Masalah: " + current.getProblemDescription());
            System.out.println("Tanggal Servis: " + current.getServiceDate());
            System.out.println("Biaya Servis: Rp" + current.getCost());
            System.out.println("Status: " + current.getStatus());
            System.out.println("Prioritas: " + current.getPriority());
            System.out.println("-----------------------------");
            current = current.getNext();
        }
    }

    // Fungsi untuk menyimpan data ke file arsip
    private void simpanKeArsip(ServiceNode node) {
        try (FileWriter writer = new FileWriter(ARSIP_FILE, true);
                BufferedWriter bufferedWriter = new BufferedWriter(writer)) {

            String data = node.getServiceId() + DELIMITER +
                    node.getCustomerName() + DELIMITER +
                    node.getDeviceType() + DELIMITER +
                    node.getProblemDescription() + DELIMITER +
                    node.getServiceDate().toString() + DELIMITER +
                    node.getCost() + DELIMITER +
                    node.getStatus() + DELIMITER +
                    node.getPriority();

            bufferedWriter.write(data);
            bufferedWriter.newLine();

        } catch (IOException e) {
            System.err.println("Error saat menyimpan ke arsip: " + e.getMessage());
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
                    System.out.println("Prioritas: " + data[7]);
                    System.out.println("-----------------------------");
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File arsip belum ada. Akan dibuat saat ada data baru.");
        } catch (IOException e) {
            System.err.println("Error saat membaca file arsip: " + e.getMessage());
        }
    }

    // Fungsi untuk membaca ID terakhir dari arsip
    private int bacaIdTerakhirDariArsip() {
        int lastId = 0;
        try (FileReader reader = new FileReader(ARSIP_FILE);
                BufferedReader bufferedReader = new BufferedReader(reader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split("\\" + DELIMITER);
                if (data.length > 0) {
                    try {
                        int currentId = Integer.parseInt(data[0]);
                        if (currentId > lastId) {
                            lastId = currentId;
                        }
                    } catch (NumberFormatException e) {
                        // Skip baris yang tidak valid
                    }
                }
            }

        } catch (FileNotFoundException e) {
            // File belum ada, return 0
            return 0;
        } catch (IOException e) {
            System.err.println("Error saat membaca ID dari arsip: " + e.getMessage());
        }

        return lastId;
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
                        String prioritas = data[7];

                        ServiceNode newNode = new ServiceNode(nama, perangkat, masalah, biaya, prioritas);
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

    // Fungsi Hapus data service berdasarkan ID
    public void hapusDataServiceById(Scanner scanner) {
        muatDariArsip();
        tampilDataService();

        if (head == null) {
            System.out.println("Tidak ada data servis untuk dihapus.");
            return;
        }

        System.out.print("Masukkan ID servis yang ingin dihapus: ");
        int id = Integer.parseInt(scanner.nextLine());

        ServiceNode current = head;
        ServiceNode prev = null;

        while (current != null) {
            if (current.getServiceId() == id) {
                System.out.print("Apakah Anda yakin ingin menghapus data dengan ID " + id + "? (y/n): ");
                String confirm = scanner.nextLine();
                if (confirm.equalsIgnoreCase("y")) {
                    if (prev == null) {
                        head = current.getNext();
                    } else {
                        prev.setNext(current.getNext());
                    }
                    tulisUlangSeluruhArsip();
                    System.out.println("Data berhasil dihapus.");
                } else {
                    System.out.println("Penghapusan dibatalkan.");
                }
                return;
            }
            prev = current;
            current = current.getNext();
        }

        System.out.println("Data dengan ID tersebut tidak ditemukan.");
    }

    // Fungsi Hapus semua data service
    public void hapusSemuaDataService(Scanner scanner) {
        muatDariArsip();

        if (head == null) {
            System.out.println("Tidak ada data servis untuk dihapus.");
            return;
        }

        System.out.print("Apakah Anda yakin ingin menghapus SEMUA data servis? (y/n): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            head = null;

            // Kosongkan isi file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARSIP_FILE))) {
                writer.write(""); // tulis kosong
            } catch (IOException e) {
                System.err.println("Gagal menghapus arsip: " + e.getMessage());
                return;
            }

            System.out.println("Semua data servis berhasil dihapus.");
        } else {
            System.out.println("Penghapusan semua data dibatalkan.");
        }
    }

    public void updateDataService() {
        muatDariArsip(); // Ambil data dari file ke memori (linked list)

        Scanner scanner = new Scanner(System.in);

        // Input ID Service yang ingin diupdate
        System.out.print("Masukkan Service ID yang ingin diupdate: ");
        int serviceId = 0;
        try {
            serviceId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Input ID tidak valid!");
            return; // keluar dari fungsi jika input salah
        }

        ServiceNode current = head;
        boolean ditemukan = false;

        while (current != null) {
            if (current.getServiceId() == serviceId) {
                ditemukan = true;

                // Tampilkan data lama
                System.out.println("Data ditemukan:");
                System.out.println(current);

                // Input data baru, jika kosong maka data lama tetap
                System.out.print("Nama Customer [" + current.getCustomerName() + "]: ");
                String nama = scanner.nextLine();
                if (!nama.isEmpty())
                    current.setCustomerName(nama);

                System.out.print("Tipe Device [" + current.getDeviceType() + "]: ");
                String device = scanner.nextLine();
                if (!device.isEmpty())
                    current.setDeviceType(device);

                System.out.print("Deskripsi Masalah [" + current.getProblemDescription() + "]: ");
                String masalah = scanner.nextLine();
                if (!masalah.isEmpty())
                    current.setProblemDescription(masalah);

                System.out.print("Biaya [" + current.getCost() + "]: ");
                String biayaInput = scanner.nextLine();
                if (!biayaInput.isEmpty()) {
                    try {
                        current.setCost(Double.parseDouble(biayaInput));
                    } catch (NumberFormatException e) {
                        System.out.println("Biaya tidak valid, tidak diubah.");
                    }
                }

                // Status menggunakan pilihan
                System.out.println("Status sekarang: [" + current.getStatus() + "]");
                System.out.println("Pilih status baru:");
                current.setStatus(pilihStatusBaru(scanner));

                System.out.println("Data berhasil diupdate.");
                break;
            }
            current = current.getNext();
        }

        if (!ditemukan) {
            System.out.println("Service ID " + serviceId + " tidak ditemukan.");
        }
        tulisUlangSeluruhArsip(); // Simpan semua perubahan ke file
    }

    private String pilihStatusBaru(Scanner scanner) {
        while (true) {
            System.out.println("1. pending");
            System.out.println("2. on_going");
            System.out.println("3. completed");
            System.out.print("Pilih (1-3): ");
            String pilih = scanner.nextLine();

            switch (pilih) {
                case "1":
                    return "pending";
                case "2":
                    return "on_going";
                case "3":
                    return "completed";
                default:
                    System.out.println("Pilihan tidak valid, coba lagi.");
            }
        }
    }

    // Helper untuk menyimpan ulang (menimpa) semua data dari linked list ke arsip
    private void tulisUlangSeluruhArsip() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARSIP_FILE))) {
            ServiceNode current = head;
            while (current != null) {
                String data = current.getServiceId() + DELIMITER +
                        current.getCustomerName() + DELIMITER +
                        current.getDeviceType() + DELIMITER +
                        current.getProblemDescription() + DELIMITER +
                        current.getServiceDate() + DELIMITER +
                        current.getCost() + DELIMITER +
                        current.getStatus();
                writer.write(data);
                writer.newLine();
                current = current.getNext();
            }
        } catch (IOException e) {
            System.err.println("Gagal menyimpan ulang data ke arsip: " + e.getMessage());
        }
    }

    public void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== SISTEM PENCATATAN DATA SERVICE ===");
            System.out.println("1. Tambah Data Service");
            System.out.println("2. Tampil Data Service (Memory)");
            System.out.println("3. Baca File Arsip");
            System.out.println("4. Statistik Service");
            System.out.println("5. Hapus Data Service berdasarkan ID");
            System.out.println("6. Hapus Semua Data Service");
            System.out.println("7. Update Data Service");
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
                    this.tampilDataService();
                    break;
                case 3:
                    System.out.println("======");
                    this.bacaFile();
                    break;
                case 4:
                    System.out.println("======");
                    this.statistik();
                    break;
                case 5:
                    System.out.println("======");
                    this.hapusDataServiceById(scanner);
                    break;
                case 6:
                    System.out.println("======");
                    this.hapusSemuaDataService(scanner);
                    break;
                case 7:
                    System.out.println("======");
                    this.updateDataService();
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
