package mx.cetys.jorgepayan.a23570_payan_examen01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_KEY = "Message";
    private int visitNo = 0;

    private EditText edit_text_customer;
    private EditText edit_text_operations;
    private ArrayList<CustomerVisit> customerVisits;
    private ArrayList<Turn> turns;
    private CustomerVisitAdapter customerVisitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_text_customer = (EditText) findViewById(R.id.edit_text_customer);
        edit_text_operations = (EditText) findViewById(R.id.edit_text_operations);
        Button button_addCustomer = (Button) findViewById(R.id.button_addCustomer);
        Button button_calculateQueue = (Button) findViewById(R.id.button_calculateQueue);
        Button button_reset = (Button) findViewById(R.id.button_reset);
        final ListView list_view_customers = (ListView) findViewById(R.id.list_view_customers);

        customerVisits = new ArrayList<CustomerVisit>();
        customerVisitAdapter = new CustomerVisitAdapter(this);
        list_view_customers.setAdapter(customerVisitAdapter);

        turns = new ArrayList<Turn>();

        button_addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visitNo++;
                String customerName = edit_text_customer.getText().toString();
                int numberOfOperations =
                        Integer.parseInt(edit_text_operations.getText().toString());
                CustomerVisit visit = makeCustomerVisit(visitNo, customerName, numberOfOperations);
                customerVisits.add(visit);
                fillCustomerVisitView(customerVisits);
                edit_text_customer.setText("");
                edit_text_operations.setText("");
            }
        });

        button_calculateQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turns = calculateQueue(customerVisits);
                Intent intent = new Intent(getApplicationContext(), QueueListActivity.class);
                intent.putExtra(EXTRA_KEY, turns);
                startActivity(intent);
            }
        });

        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerVisits.clear();
                customerVisitAdapter.clear();
            }
        });

    }
    private CustomerVisit makeCustomerVisit(int position, String customerName, int numberOfOperations) {
        CustomerVisit visit = new CustomerVisit(
            position,
            customerName,
            numberOfOperations,
            1
        );
        return visit;
    }

    private ArrayList<Turn> calculateQueue(ArrayList<CustomerVisit> customerVisitArray) {
        ArrayList<Turn> customerTurns = new ArrayList<>();
        int iterations = 0;
        for(CustomerVisit visit : customerVisitArray) {
            iterations += visit.getOperations()[0];
        }
        int listIndex = 0;
        int turn = 1;
        for (int i = 0; i <= iterations; i++) {
            if(listIndex >= customerVisitArray.size()) {
                listIndex = 0;
            }
            CustomerVisit visit = customerVisitArray.get(listIndex);
            int[] operations = visit.getOperations();
            if(operations[0] == 0) {
                customerVisitArray.remove(visit);
            }
            else {
                customerTurns.add(makeTurnFromVisit(visit, turn));
                operations[0]--;
                operations[1]++;
                visit.setOperations(operations);
                turn++;
            }
            listIndex++;
        }
        return customerTurns;
    }

    private Turn makeTurnFromVisit(CustomerVisit visit, int turnIndex) {
        Turn turn = new Turn(
            turnIndex,
            visit.getCustomer(),
            visit.getOperations()[1]
        );
        return turn;
    }

    private void fillCustomerVisitView(ArrayList<CustomerVisit> customerVisitList) {
        customerVisitAdapter.clear();

        for(CustomerVisit visit : customerVisitList) {
            customerVisitAdapter.add(visit);
        }
    }
}
