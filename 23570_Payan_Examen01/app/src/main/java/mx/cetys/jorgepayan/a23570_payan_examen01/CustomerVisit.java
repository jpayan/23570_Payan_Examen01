package mx.cetys.jorgepayan.a23570_payan_examen01;

/**
 * Created by jorge.payan on 9/8/17.
 */

public class CustomerVisit {
    private int position;
    private String customer;
    private int[] operations = new int[2];

    public CustomerVisit(int position, String customer, int numberOfOperations, int currentOperation) {
        this.position = position;
        this.customer = customer;
        this.operations[0] = numberOfOperations;
        this.operations[1] = currentOperation;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public int[] getOperations() {
        return operations;
    }

    public void setOperations(int[] operations) {
        this.operations = operations;
    }
}
