import java.util.Scanner;

public class ServiceMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ServiceLinkedList serviceList = new ServiceLinkedList();

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
                    serviceList.tambahDataService(scanner);
                    break;
                case 2:
                    System.out.println("======");
                    serviceList.tampilDataService();
                    break;
                case 3:
                    System.out.println("======");
                    serviceList.bacaFile();
                    break;
                case 4:
                    System.out.println("======");
                    serviceList.size();
                    break;
                case 5:
                    System.out.println("======");
                    serviceList.hapusDataServiceById(scanner);
                    break;
                case 6:
                    System.out.println("======");
                    serviceList.hapusSemuaDataService(scanner);
                    break;
                case 7:
                    System.out.println("======");
                    serviceList.updateDataService();
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