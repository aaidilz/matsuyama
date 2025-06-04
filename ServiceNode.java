import java.time.LocalDate;

public class ServiceNode {
    private int serviceId;
    private String customerName;
    private String deviceType;
    private String problemDescription;
    private LocalDate serviceDate;
    private double cost;
    private String status; // 'pending', 'on_going', 'completed'
    private String priority;
    private ServiceNode next;

    // Constructor for default service
    public ServiceNode() {
        this.serviceId = 1;
        this.customerName = "";
        this.deviceType = "";
        this.problemDescription = "";
        this.serviceDate = LocalDate.now();
        this.cost = 0.0;
        this.status = "pending";
        this.priority = ""; // Default priority
        this.next = null;
    }

    // Constructor for new service
    public ServiceNode(String customerName, String deviceType, String problemDescription, double cost) {
        this.customerName = customerName;
        this.deviceType = deviceType;
        this.problemDescription = problemDescription;
        this.cost = cost;
        this.status = "pending";
        this.priority = "";
        this.serviceDate = LocalDate.now();
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public LocalDate getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ServiceNode getNext() {
        return next;
    }

    public void setNext(ServiceNode next) {
        this.next = next;
    }

    public String toString() {
        return "Service ID: " + serviceId +
                ", Customer: " + customerName +
                ", Device: " + deviceType +
                ", Problem: " + problemDescription +
                ", Date: " + serviceDate +
                ", Cost: Rp" + cost +
                ", Status: " + status +
                ", Prioritas: " + priority;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}