package mx.cetys.jorgepayan.a23570_payan_examen01;

/**
 * Created by jorge.payan on 9/8/17.
 */

public class CustomerVisit {

    private String customerCode;
    private int position;
    private String name;
    private int[] operations = new int[2];
    private String dateAdded;

    public CustomerVisit(String customerCode, int position, String name, int numberOfOperations, int currentOperation, String dateAdded) {
        this.customerCode = customerCode;
        this.position = position;
        this.name = name;
        this.operations[0] = numberOfOperations;
        this.operations[1] = currentOperation;
        this.dateAdded = dateAdded;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String customer) {
        this.name = customer;
    }

    public int[] getOperations() {
        return operations;
    }

    public void setOperations(int[] operations) {
        this.operations = operations;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
}
