import java.util.Scanner;
import java.io.*;

public class ServiceLinkedList {
    private ServiceNode head;
    private static final String ARSIP_FILE = "data_service.txt";
    private static final String DELIMITER = "|";

    // Fungsi size untuk menghitung total dan per status
    public void size() {
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
                    onGoing++;
                    break;
                case "completed":
                    completed++;
                    break;
            }
            current = current.getNext();
        }

        System.out.println("Total Services: " + total);
        System.out.println("Pending: " + pending);
        System.out.println("On Going: " + onGoing);
        System.out.println("Completed: " + completed);
    }

    // Fungsi tambahDataService untuk menambahkan data service baru
    public void tambahDataService(Scanner scanner) {
        System.out.println("Masukkan nama pelanggan:");
        String nama = scanner.nextLine();

        System.out.println("Masukkan jenis perangkat:");
        String perangkat = scanner.nextLine();

        System.out.println("Masukkan deskripsi masalah:");
        String masalah = scanner.nextLine();

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
        ServiceNode newNode = new ServiceNode(nama, perangkat, masalah, biaya);

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
                    node.getStatus();

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
                if (data.length == 7) {
                    System.out.println("ID: " + data[0]);
                    System.out.println("Nama Pelanggan: " + data[1]);
                    System.out.println("Jenis Perangkat: " + data[2]);
                    System.out.println("Deskripsi Masalah: " + data[3]);
                    System.out.println("Tanggal Servis: " + data[4]);
                    System.out.println("Biaya Servis: Rp" + data[5]);
                    System.out.println("Status: " + data[6]);
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
}