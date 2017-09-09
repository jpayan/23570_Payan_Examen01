package mx.cetys.jorgepayan.a23570_payan_examen01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class QueueListActivity extends AppCompatActivity {

    private TurnAdapter turnAdapter;
    private ListView listView;
    private ArrayList<Turn> turns = new ArrayList<Turn>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_list);

        Intent intent = getIntent();

        listView = (ListView) findViewById(R.id.list_view_queue);
        turnAdapter = new TurnAdapter(this);
        listView.setAdapter(turnAdapter);

        turns = intent.getParcelableArrayListExtra(MainActivity.EXTRA_KEY);
        fillMovieView(turns);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void fillMovieView(ArrayList<Turn> turnList) {
        turnAdapter.clear();

        for(Turn turn : turns) {
            turnAdapter.add(turn);
        }
    }
}
