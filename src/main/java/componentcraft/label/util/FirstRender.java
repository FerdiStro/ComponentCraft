package componentcraft.label.util;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class FirstRender {


    private boolean first = false;
    private Timestamp timestamp;


    public void firstRenderDone(){
        this.first = true;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

}
