import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.EventListener;
import java.util.EventObject;

/**
 * Created by one on 9/21/17.
 */
public class Cell extends Rectangle {

    private static final Color ALIVE_COLOR = Color.YELLOW;
    private static final Color DEAD_COLOR = Color.GRAY;

    private boolean isAlive;
    private boolean isAliveNext;



    public Cell(boolean isAlive, boolean isAliveNext, double size) {
        super(size, size);
        setFill(DEAD_COLOR);
        this.isAlive = isAlive;
        this.isAliveNext = isAliveNext;
    }

    public void updateNextState(int numLiveNeighbours) {
        if (isAlive) {
            liveTransition(numLiveNeighbours);
        } else {
            deadTransition(numLiveNeighbours);
        }
    }

    public void updateCurrentState() {
        isAlive = isAliveNext;
        updateColor();
    }

    private void onClick(){

    }


    private void updateColor() {
        if (isAlive) {
            setFill(ALIVE_COLOR);
        } else {
            setFill(DEAD_COLOR);
        }
    }

    private void liveTransition(int numLiveNeighbours) {
        if (numLiveNeighbours < 2 || numLiveNeighbours > 3) { // underpopulation and overpopulation
            isAliveNext = false;
        } else {
            isAliveNext = true;
        }
    }

    private void deadTransition(int numLiveNeighbours) {
        if (numLiveNeighbours == 3) { // reproduction
            isAliveNext = true;
        } else {
            isAliveNext = false;
        }
    }


    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
        updateColor();
    }


    public void actionPerformed(ActionEvent e){

    }

}

/*
class MyEvent extends EventObject{
    public MyEvent(Object source){
        super(source);
    }
}
interface MyEventlClassListener{
    public void handleMyEventClassEvent(EventObject e);
}

class MyEventListener implements MyEventlClassListener{
    public void handleMyEventClassEvent(EventObject e){

    }
}
class myEventSource{
    private List _listeners = new ArrayList();
    public synchronized void addEventListener(MyEventlClassListener listener){
        _listeners.add(listener);

    }
    public synchronized void removeEventListener(MyEventlClassListener listener){
        _listeners.remove(listener);
    }

    private synchronized void fireEvent(){
        MyEvent event = new MyEvent(this);
        Iterator i = _listeners.iterator();
        while(i.hasNext()){
            ((MyEventlClassListener) i.next().handleMyEventClassEvent(event))
    }
}

*/

