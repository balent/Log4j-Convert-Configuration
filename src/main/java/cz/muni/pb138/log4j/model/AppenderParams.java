package cz.muni.pb138.log4j.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum AppenderParams {

    consoleappender("Encoding", "ImmediateFlush", "Target"),
    fileappender("Append", "Encoding", "BufferedIO", "BufferSize", "File", "ImmediateFlush"),
    rollingfileappender("Append", "Encoding", "BufferedIO", "BufferSize", "File", "ImmediateFlush", 
            "MaxBackupIndex", "MaxFileSize"),
    dailyrollingfileappender("Append", "Encoding", "BufferedIO", "BufferSize", "File", "ImmediateFlush", 
            "DatePattern"),
    writerappender("Encoding", "ImmediateFlush");
    
    private List<String> params = new ArrayList<String>();

    AppenderParams(String... params) {
        this.params.addAll(Arrays.asList(params));
    }

    public List<String> getParams() {
        return params;
    }
}
