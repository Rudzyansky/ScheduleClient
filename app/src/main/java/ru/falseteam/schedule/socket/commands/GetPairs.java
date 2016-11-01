package ru.falseteam.schedule.socket.commands;

import android.content.Intent;

import java.util.ArrayList;
import java.util.Map;

import ru.falseteam.schedule.management.ListOfPairsActivity;
import ru.falseteam.schedule.socket.CommandAbstract;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.schedule.serializable.Pair;

public class GetPairs extends CommandAbstract {
    public GetPairs() {
        super("get_pairs");
    }

    public static ArrayList<Pair> pairs = new ArrayList<>();

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public void exec(Map<String, Object> map) {
        pairs.clear();
        for (int i = 0; i < (int) map.get("count"); ++i) {
            Pair pair = (Pair) map.get(String.valueOf(i));
            pairs.add(pair);
        }
        Intent intent = new Intent(Worker.get().context, ListOfPairsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Worker.get().context.startActivity(intent);
    }
}
