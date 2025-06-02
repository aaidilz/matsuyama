import java.time.LocalDate;

public class ServiceNode {
    private int serviceId;
    private String customerName;
    private String deviceType;
    private String problemDescription;
    private LocalDate serviceDate;
    private double cost;
    private String status; // 'pending', 'on_going', 'completed'

    // Constructor for new service
    public ServiceNode(String customerName, String deviceType, String problemDescription, double cost) {
        this.customerName = customerName;
        this.deviceType = deviceType;
        this.problemDescription = problemDescription;
        this.cost = cost;
        this.status = "pending";
        this.serviceDate = LocalDate.now();
    }

    public String toString() {
        return "Service ID: " + serviceId +
                ", Customer: " + customerName +
                ", Device: " + deviceType +
                ", Problem: " + problemDescription +
                ", Date: " + serviceDate +
                ", Cost: Rp" + cost +
                ", Status: " + status;
    }
}