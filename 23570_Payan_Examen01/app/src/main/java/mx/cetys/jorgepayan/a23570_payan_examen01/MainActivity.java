package mx.cetys.jorgepayan.a23570_payan_examen01;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

import mx.cetys.jorgepayan.a23570_payan_examen01.Utils.CustomerHelper;
import mx.cetys.jorgepayan.a23570_payan_examen01.Utils.DBUtils;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_KEY = "Message";
    private int visitNo = 0;

    private EditText edit_text_customer;
    private EditText edit_text_operations;
    private ArrayList<CustomerVisit> customerVisits;
    private ArrayList<Turn> turns;
    private ListView list_view_customers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_text_customer = (EditText) findViewById(R.id.edit_text_customer);
        edit_text_operations = (EditText) findViewById(R.id.edit_text_operations);

        Button button_addCustomer = (Button) findViewById(R.id.button_addCustomer);
        Button button_calculateQueue = (Button) findViewById(R.id.button_calculateQueue);
        Button button_reset = (Button) findViewById(R.id.button_reset);

        list_view_customers = (ListView) findViewById(R.id.list_view_customers);
        final CustomerHelper helper = new CustomerHelper(getApplicationContext());

        final CustomerVisitAdapter customerVisitAdapter = new CustomerVisitAdapter(this);
        list_view_customers.setAdapter(customerVisitAdapter);

        customerVisits = new ArrayList<CustomerVisit>();
        getCustomerVisits(helper, customerVisitAdapter);

        turns = new ArrayList<Turn>();

        button_addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String customerName = edit_text_customer.getText().toString();
                String operationsField = edit_text_operations.getText().toString();
                if(!customerName.isEmpty() && !operationsField.isEmpty()) {
                    visitNo++;
                    int numberOfOperations = Integer.parseInt(operationsField);
                    addCustomerVisit(helper, customerVisitAdapter, visitNo, customerName, numberOfOperations);
                    edit_text_customer.setText("");
                    edit_text_operations.setText("");
                }
                else {
                    Context context = getApplicationContext();
                    CharSequence text = "Please fill out all the fields.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

        button_calculateQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(customerVisits.size() > 0) {
                    turns = calculateQueue(customerVisits);
                    Intent intent = new Intent(getApplicationContext(), QueueListActivity.class);
                    intent.putExtra(EXTRA_KEY, turns);
                    startActivity(intent);
                }
                else {
                    Context context = getApplicationContext();
                    CharSequence text = "You must add customers first.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerVisits.clear();
                customerVisitAdapter.clear();
                clearTable(helper, DBUtils.CUSTOMER_VISITS_TABLE_NAME);
                visitNo = 0;
            }
        });

        list_view_customers.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomerVisit customerVisit = customerVisits.get(position);
                deleteCustomerVisit(helper, customerVisit.getPosition());
                getCustomerVisits(helper, customerVisitAdapter);
                if(customerVisits.isEmpty()) {
                    visitNo = 0;
                }
            }
        });
    }

    private ArrayList<Turn> calculateQueue(ArrayList<CustomerVisit> customerVisitArray) {
        ArrayList<Turn> customerTurns = new ArrayList<>();
        int iterations = 0;
        for(CustomerVisit visit : customerVisitArray) {
            iterations += visit.getOperations()[0];
        }
        int listIndex = 0;
        int turn = 1;
        while(iterations != 0) {
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
                iterations--;
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

    private void fillCustomerVisitView(CustomerVisitAdapter customerVisitAdapter, ArrayList<CustomerVisit> customerVisitList) {
        customerVisitAdapter.clear();

        for(CustomerVisit visit : customerVisitList) {
            customerVisitAdapter.add(visit);
        }
    }

    private void getCustomerVisits(CustomerHelper helper, CustomerVisitAdapter customerVisitAdapter) {
        helper.open();
        if(helper.getAllCustomerVisits().size() >= 0) {
            customerVisits = helper.getAllCustomerVisits();
            fillCustomerVisitView(customerVisitAdapter, customerVisits);
        }
        helper.close();
    }

    private void addCustomerVisit(CustomerHelper helper, CustomerVisitAdapter customerVisitAdapter, int position, String name, int operations) {
        helper.open();
        helper.addCustomerVisit(position, name, operations, 1);
        getCustomerVisits(helper, customerVisitAdapter);
        helper.close();
    }

    private void deleteCustomerVisit(CustomerHelper helper, int position) {
        helper.open();
        int customerVisitId = helper.getCustomerVisitId(position);
        helper.deleteCustomerVisit(customerVisitId);
        helper.close();
    }

    private void clearTable(CustomerHelper helper, String tableName) {
        helper.open();
        helper.clearTable(tableName);
        helper.close();
    }
}
