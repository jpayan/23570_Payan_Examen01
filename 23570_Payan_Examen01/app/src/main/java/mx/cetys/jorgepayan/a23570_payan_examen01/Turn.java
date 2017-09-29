package mx.cetys.jorgepayan.a23570_payan_examen01;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jorge.payan on 9/8/17.
 */

public class Turn implements Parcelable {
    private int turn;
    private String customer;
    private int currentOperation;

    public Turn(int turn, String customer, int currentOperation) {
        this.turn = turn;
        this.customer = customer;
        this.currentOperation = currentOperation;
    }

    public Turn(Parcel in) {
        turn = in.readInt();
        customer = in.readString();
        currentOperation = in.readInt();
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public int getCurrentOperation() {
        return currentOperation;
    }

    public void setCurrentOperation(int currentOperation) {
        this.currentOperation = currentOperation;
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(turn);
        dest.writeString(customer);
        dest.writeInt(currentOperation);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Turn createFromParcel(Parcel in) {
            return new Turn(in);
        }

        @Override
        public Turn[] newArray(int size) {
            return new Turn[size];
        }
    };
}
