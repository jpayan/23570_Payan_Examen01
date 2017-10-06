package mx.cetys.jorgepayan.a23570_payan_examen01;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.Calendar;

import mx.cetys.jorgepayan.a23570_payan_examen01.Utils.CustomerHelper;
import mx.cetys.jorgepayan.a23570_payan_examen01.Utils.DBUtils;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_KEY = "Message";
    private int visitNo = 0;

    private EditText edit_text_code;
    private EditText edit_text_customer;
    private EditText edit_text_operations;
    private ListView list_view_customers;

    private ArrayList<CustomerVisit> customerVisits;
    private ArrayList<Turn> turns;

    private int year, month, day;
    static final int DIALOG_ID = 0;
    Button datePicker;

    CustomerHelper helper;
    CustomerVisitAdapter customerVisitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_text_code = (EditText) findViewById(R.id.edit_text_customer_code);
        edit_text_customer = (EditText) findViewById(R.id.edit_text_customer_name);
        edit_text_operations = (EditText) findViewById(R.id.edit_text_operations);

        Button button_addCustomer = (Button) findViewById(R.id.button_addCustomer);
        Button button_calculateQueue = (Button) findViewById(R.id.button_calculateQueue);
        Button button_reset = (Button) findViewById(R.id.button_reset);

        list_view_customers = (ListView) findViewById(R.id.list_view_customers);
        helper = new CustomerHelper(getApplicationContext());

        customerVisitAdapter = new CustomerVisitAdapter(this);
        list_view_customers.setAdapter(customerVisitAdapter);

        final Calendar calendar = Calendar.getInstance();
        showDialogOnButtonClick();
        setDate(calendar);

        customerVisits = new ArrayList<CustomerVisit>();
        getCustomerVisits(helper, customerVisitAdapter);

        turns = new ArrayList<Turn>();

        button_addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String customerCode = edit_text_code.getText().toString();
                String customerName = edit_text_customer.getText().toString();
                String operationsField = edit_text_operations.getText().toString();
                if(!customerName.isEmpty() && !operationsField.isEmpty() && !customerCode.isEmpty()) {
                    visitNo++;
                    int numberOfOperations = Integer.parseInt(operationsField);
                    String dateAdded = getDate();
                    addCustomerVisit(helper, customerVisitAdapter, customerCode, visitNo,
                            customerName, numberOfOperations, dateAdded);
                    edit_text_code.setText("");
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
                deleteCustomerVisit(helper, customerVisit.getCustomerCode());
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
            visit.getName(),
            visit.getOperations()[1]
        );
        return turn;
    }

    private void getCustomerVisits(CustomerHelper helper, CustomerVisitAdapter customerVisitAdapter) {
        helper.open();
        if(helper.getCustomerVisits(getDate()).size() >= 0) {
            customerVisits = helper.getCustomerVisits(getDate());
            fillCustomerVisitView(customerVisitAdapter, customerVisits);
        }
        helper.close();
    }

    private void fillCustomerVisitView(CustomerVisitAdapter customerVisitAdapter,
                                       ArrayList<CustomerVisit> customerVisitList) {
        customerVisitAdapter.clear();

        for(CustomerVisit visit : customerVisitList) {
            customerVisitAdapter.add(visit);
        }
    }

    private void addCustomerVisit(CustomerHelper helper, CustomerVisitAdapter customerVisitAdapter,
                                  String code, int position, String name, int operations,
                                  String dateAdded) {
        helper.open();
        helper.addCustomerVisit(code, position, name, operations, 1, dateAdded);
        getCustomerVisits(helper, customerVisitAdapter);
        helper.close();
    }

    private void deleteCustomerVisit(CustomerHelper helper, String customerCode) {
        helper.open();
        int customerVisitId = helper.getCustomerVisitId(customerCode);
        helper.deleteCustomerVisit(customerVisitId);
        helper.close();
    }

    private void clearTable(CustomerHelper helper, String tableName) {
        helper.open();
        helper.clearTable(tableName);
        helper.close();
    }

    public void showDialogOnButtonClick() {
        datePicker = (Button) findViewById(R.id.button_date);

        datePicker.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(DIALOG_ID);
                    }
                }
        );
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            return new DatePickerDialog(this, datePickerListener, year, month, day);
        }
        else {
            return null;
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            year = i;
            month = i1;
            day = i2;
            setDate(i, i1, i2);
            getCustomerVisits(helper, customerVisitAdapter);
        }
    };

    private void setDate(Calendar calendar) {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.setText(year + " / " + month + " / " + day);
    }

    private void setDate(int y, int m, int d) {
        year = y;
        month = m;
        day = d;
        datePicker.setText(year + " / " + month + " / " + day);
    }

    private String getDate() {
        return year + " / " + month + " / " + day;
    }
}
