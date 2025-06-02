import java.util.Scanner;

public class ServiceLinkedList {
    private ServiceNode head;
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

    public void tambahDataService(Scanner scanner) {
        System.out.println("Masukkan nama pelanggan:");
        String nama = scanner.nextLine();
    
        System.out.println("Masukkan jenis perangkat:");
        String perangkat = scanner.nextLine();
    
        System.out.println("Masukkan deskripsi masalah:");
        String masalah = scanner.nextLine();
    
        System.out.println("Masukkan biaya servis:");
        double biaya = 0;
        while(true) {
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
    
        System.out.println("Data servis berhasil ditambahkan");
    }

    private int generateNewId() {
        if (head == null) {
            return 1; // ID pertama
        }
        ServiceNode current = head;
        while (current.getNext() != null) {
            current = current.getNext();
        }
        return current.getServiceId() + 1; // ID berikutnya
    }

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
            System.out.println("Biaya Servis: " + current.getCost());
            System.out.println("Status: " + current.getStatus());
            System.out.println("-----------------------------");
            current = current.getNext();
        }
    }
    
}
