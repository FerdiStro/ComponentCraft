package componentcraft.util.log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogMassagesBuilder {


    private SimpleDateFormat simpleDateFormat;
    private boolean enableSimpleDate;

    public LogMassagesBuilder toggleSimpleDate() {
        this.enableSimpleDate = true;
        return this;
    }

    public LogMassagesBuilder setEnableSimpleDate(boolean enableSimpleDate) {
        this.enableSimpleDate = enableSimpleDate;
        return this;
    }

    public LogMassagesBuilder setSimpleDateFormat(SimpleDateFormat simpleDateFormat) {
        this.simpleDateFormat = simpleDateFormat;
        return this;
    }

    private String message;

    public LogMassagesBuilder setMessage(String message) {
        this.message = message;
        return this;
    }


    public String build() {
        if (enableSimpleDate) {
            message = message + simpleDateFormat.format(new Date());
        }
        return message;
    }


    public static String initMessage(Class initClass) {
        return create()
                .setEnableSimpleDate(true)
                .setMessage("> Init : " + initClass.getName())
                .build();
    }

    private LogMassagesBuilder() {
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        message = "";
    }

    public static LogMassagesBuilder create() {
        return new LogMassagesBuilder();
    }


}

