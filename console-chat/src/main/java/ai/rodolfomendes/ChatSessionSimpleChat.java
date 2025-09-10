package ai.rodolfomendes;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ChatSessionSimpleChat implements ChatSession{
    private final SimpleChat chat;

    public ChatSessionSimpleChat(SimpleChat chat) {
        this.chat = chat;
    }

    // The chat logic depends only on the ChatModel interface
    @Override
    public void chat() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("*** Starting chat ***");

            while (true) {
                System.out.println("Type your question: ");

                String prompt = br.readLine();
                if (prompt.equalsIgnoreCase("/BYE")) {
                    break;
                }

                final String response = chat.response(prompt);              
                
                System.out.println();
                System.out.println("AI:");
                System.out.println(response);
            }

            System.out.println("*** END ***");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
