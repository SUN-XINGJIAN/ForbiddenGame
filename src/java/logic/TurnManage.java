package logic;

import controller.ScreenController;
import module.Player;

import java.util.List;

public class TurnManage {
    public int step = 0;
    private ScreenController screenController;

    // Initializing with 3 steps each turn
    public TurnManage(ScreenController screenController){
        step = 3;
        this.screenController = screenController;
    }

    public void useStep(){
        if (step > 0) step--;
    }

    public void showRemainSteps(){
        if(step == 0){
            screenController.getRemainSteps().setText("Remain Steps: 3");
        }else{
            screenController.getRemainSteps().setText("Remain Steps: " + step);}
    }

    // Moves to next player's index
    public int getIndex(int index,List<Player> players){
        step = 3; // Reset actions
        index++;
        if(index == players.size()){
            index = 0;
        }
        return index;
    }

    // Moves to previous player's index
    public int getIndex1(int index,List<Player> players){
        index--;
        if(index == -1){
            index = players.size()-1;
        }
        return index;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    // Reset to initial steps
    public void resetSteps() {
        step = 3;
    }
}
