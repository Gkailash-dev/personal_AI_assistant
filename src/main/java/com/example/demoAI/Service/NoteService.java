package com.example.demoAI.Service;




import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class NoteService {

    private final String FILE_PATH =
            "notes.txt";

    // ADD NOTE
    public void addNote(
            String note
    ) throws IOException {

        Path path =
                Paths.get(FILE_PATH);

        Files.writeString(

                path,

                note + "\n",

                StandardOpenOption.CREATE,

                StandardOpenOption.APPEND
        );
    }

    // READ NOTES
    public String getNotes()
            throws IOException {

        Path path =
                Paths.get(FILE_PATH);

        // if file doesn't exist
        if(!Files.exists(path)) {

            return "";
        }

        return Files.readString(path);
    }
}