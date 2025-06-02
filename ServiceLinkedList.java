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

}
