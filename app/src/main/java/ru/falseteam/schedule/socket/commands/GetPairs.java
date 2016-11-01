package ru.falseteam.schedule.socket.commands;

import android.content.Intent;

import java.util.ArrayList;
import java.util.Map;

import ru.falseteam.schedule.management.ListOfPairsActivity;
import ru.falseteam.schedule.socket.CommandAbstract;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.schedule.management.Pair;

public class GetPairs extends CommandAbstract {
    public GetPairs() {
        super("get_pairs");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public void exec(Worker worker, Map<String, Object> map) {
        for (int i = 0; i < (int) map.get("count"); ++i) {
            pairs.add((Pair) map.get(String.valueOf(i)));
        }
        Intent intent = new Intent(worker.context, ListOfPairsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        worker.context.startActivity(intent);
    }

    public static ArrayList<Pair> pairs = new ArrayList<>();
}
