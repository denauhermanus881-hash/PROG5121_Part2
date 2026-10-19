import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Message {
    private String messageID;
    private int messageNumber;
    private String recipientCell;
    private String messageContent;
    private String messageHash;
    private String messageStatus;

    private static Message[] storageArray = new Message[100];
    private static int messageCount = 0;

    public Message(int messageNumber, String recipientCell, String messageContent) {
        this.messageID = generateID();
        this.messageNumber = messageNumber;
        this.recipientCell = recipientCell;
        this.messageContent = messageContent;
        this.messageHash = createMessageHash();
        this.messageStatus = "Pending";
    }

    private String generateID() {
        Random r = new Random();
        long num = 1000000000L + (long)(r.nextDouble() * 9000000000L);
        return String.valueOf(num);
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        if (recipientCell != null && recipientCell.startsWith("+") && recipientCell.length() <= 12) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    public String checkMessageLength() {
        if (messageContent == null) {
            return "Message ready to send.";
        }
        int length = messageContent.length();
        if (length <= 250) {
            return "Message ready to send.";
        } else {
            int excess = length - 250;
            return "Message exceeds 250 characters by " + excess + " characters; please reduce the size.";
        }
    }

    public String createMessageHash() {
        if (messageContent == null || messageContent.trim().isEmpty()) {
            return messageID.substring(0, 2).toUpperCase() + ":" + messageNumber + ":NONAME";
        }
        String[] words = messageContent.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];
        
        String prefix = messageID.substring(0, 2);
        return (prefix + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    public static boolean storeMessage(Message message) {
        if (messageCount < storageArray.length) {
            storageArray[messageCount] = message;
            messageCount++;
            return true;
        } else {
            return false;
        }
    }

    public void saveToFile() {
        String json = "{\n" +
                      "  \"messageID\": \"" + messageID + "\",\n" +
                      "  \"messageNumber\": " + messageNumber + ",\n" +
                      "  \"recipientCell\": \"" + recipientCell + "\",\n" +
                      "  \"messageContent\": \"" + messageContent + "\",\n" +
                      "  \"messageHash\": \"" + messageHash + "\",\n" +
                      "  \"status\": \"" + messageStatus + "\"\n" +
                      "}\n";

        try {
            FileWriter writer = new FileWriter("messages.json", true);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving file");
        }
    }

    public String SentMessage(int choice) {
        if (choice == 1) {
            storeMessage(this);
            saveToFile();
            this.messageStatus = "Sent";
            return "Message successfully sent.";
        } else if (choice == 2) {
            this.messageStatus = "Disregarded";
            return "Press 0 to delete the message.";
        } else if (choice == 3) {
            storeMessage(this);
            saveToFile();
            this.messageStatus = "Stored";
            return "Message successfully stored.";
        }
        return "Invalid selection.";
    }

    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < messageCount; i++) {
            Message m = storageArray[i];
            sb.append("Message ID: ").append(m.getMessageID()).append("\n")
              .append("Message Hash: ").append(m.getMessageHash()).append("\n")
              .append("Recipient: ").append(m.getRecipientCell()).append("\n")
              .append("Message: ").append(m.getMessageContent()).append("\n")
              .append("Status: ").append(m.messageStatus).append("\n\n");
        }
        return sb.toString();
    }

    public static int returnTotalMessagess() {
        return messageCount;
    }

    // Getters and Setters
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getRecipientCell() { return recipientCell; }
    public String getMessageContent() { return messageContent; }
    public void setMessageStatus(String status) { this.messageStatus = status; }
}