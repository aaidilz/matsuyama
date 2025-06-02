import java.util.Scanner;

public class ServiceMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); 
        ServiceLinkedList serviceList = new ServiceLinkedList();

        serviceList.size();
        serviceList.tambahDataService(scanner);
        serviceList.tampilDataService();
        serviceList.size();
    }
}
