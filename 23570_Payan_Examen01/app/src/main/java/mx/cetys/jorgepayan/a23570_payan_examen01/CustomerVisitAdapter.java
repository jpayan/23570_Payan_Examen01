package mx.cetys.jorgepayan.a23570_payan_examen01;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by jorge.payan on 9/8/17.
 */

public class CustomerVisitAdapter extends ArrayAdapter<CustomerVisit> {

    public CustomerVisitAdapter(Context context){
        super(context, R.layout.customer_visit_row, R.id.customerPosition);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View oView = super.getView(position, convertView, parent);

        TextView customerPosition = (TextView) oView.findViewById(R.id.customerPosition);
        TextView customerName = (TextView) oView.findViewById(R.id.customerName);
        TextView operations = (TextView) oView.findViewById(R.id.operations);

        CustomerVisit customerVisit = this.getItem(position);
        customerPosition.setText("Num: " + String.valueOf(customerVisit.getPosition()));
        customerName.setText("Customer: " + customerVisit.getCustomer());
        operations.setText("Operations: " + String.valueOf(customerVisit.getOperations()[0]));

        return oView;
    }
}
