package logic;

import module.Player;

import java.util.List;

public class TurnManage {
    private int step = 0;
    public TurnManage(){
        step = 3;
    }


    public void useStep(){
        step--;
    }


    public int getIndex(int index,List<Player> players){
        step=3;
        index++;
        if(index == players.size()){
            index = 0;
        }
        return index;
    }

    public int getStep() {
        return step;
    }
}
