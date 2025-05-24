package logic;

import controller.ScreenController;
import module.Player;

import java.util.List;

public class TurnManage {
    private int step = 0;
    private ScreenController screenController;
    public TurnManage(ScreenController screenController){
        step = 3;
        this.screenController = screenController;
    }


    public void useStep(){
        step--;
    }

    public void showRemainSteps(){
        if(step==0){
            screenController.getRemainSteps().setText("Remain Steps: 3");
        }else{
        screenController.getRemainSteps().setText("Remain Steps: " + step);}
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
