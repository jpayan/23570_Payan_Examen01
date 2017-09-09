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

public class TurnAdapter extends ArrayAdapter<Turn> {

    public TurnAdapter(Context context){
        super(context, R.layout.turn_row, R.id.currentTurn);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View oView = super.getView(position, convertView, parent);

        TextView currentTurn = (TextView) oView.findViewById(R.id.currentTurn);
        TextView customerName = (TextView) oView.findViewById(R.id.customerName);
        TextView currentOperation = (TextView) oView.findViewById(R.id.currentOperation);

        Turn turn = this.getItem(position);
        currentTurn.setText("Turn: " + String.valueOf(turn.getTurn()));
        customerName.setText("Customer: " + turn.getCustomer());
        currentOperation.setText("No. of Operation: " + String.valueOf(turn.getCurrentOperation()));

        return oView;
    }

}
