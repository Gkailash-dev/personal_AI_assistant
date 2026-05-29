package com.example.demoAI.controller;
import com.example.demoAI.Service.NoteService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class OpenAiController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private final ChatClient chatClient;

    public OpenAiController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }


    @GetMapping("/")
    public String home() {
        return "index";
    }
    @PostMapping("/add-note")
    public String addNote(
            @RequestParam String note
    ) throws Exception {

        noteService.addNote(note);

        return "redirect:/";
    }


    @PostMapping("/ask")
    public String askQuestion(
            @RequestParam(
                    value = "file",
                    required = false
            )
            MultipartFile file,

            @RequestParam String question,

            Model model

    ) throws Exception {

        // READ NOTES
        String notes =
                noteService.getNotes();

        String pdfText = "";

        // READ PDF IF EXISTS
        if (file != null &&
                !file.isEmpty()) {

            PDDocument document =
                    PDDocument.load(
                            file.getInputStream()
                    );

            PDFTextStripper stripper =
                    new PDFTextStripper();

            pdfText =
                    stripper.getText(document);

            document.close();
        }


        String finalPrompt =
                "You are a smart personal AI assistant.\n\n"
                        +"answer only what i Ask ,don't took the answern from notes for  general notes"

                        + "Personal Notes:\n"
                        + notes

                        + "\n\nPDF Content:\n"
                        + pdfText

                        + "\n\nUser Question:\n"
                        + question;
        // AI RESPONSE
        String response =
                chatClient.prompt()
                        .user(finalPrompt)
                        .call()
                        .content();

        // SEND TO UI
        model.addAttribute(
                "answer",
                response
        );
        return "index";
    }
}