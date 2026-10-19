import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat.");

        // --- 1. REGISTRATION PHASE ---
        String regUsername = JOptionPane.showInputDialog("Register - Enter username (must contain '_' and <= 5 chars):");
        String regPassword = JOptionPane.showInputDialog("Register - Enter password (>= 8 chars, 1 capital, 1 number, 1 special):");
        String regFirst = JOptionPane.showInputDialog("Register - Enter your First Name:");
        String regLast = JOptionPane.showInputDialog("Register - Enter your Last Name:");
        String regCell = JOptionPane.showInputDialog("Register - Enter cell phone number (+ code, max 12 chars):");

        Login login = new Login(regUsername, regPassword, regFirst, regLast, regCell);
        String regStatus = login.registerUser();
        JOptionPane.showMessageDialog(null, regStatus);

        if (!regStatus.equals("User successfully registered")) {
            return;
        }

        // --- 2. LOGIN PHASE ---
        String loginUser = JOptionPane.showInputDialog("Login - Enter your username:");
        String loginPass = JOptionPane.showInputDialog("Login - Enter your password:");

        boolean isLoggedIn = login.loginUser(loginUser, loginPass);
        JOptionPane.showMessageDialog(null, login.returnLoginStatus(isLoggedIn));

        if (!isLoggedIn) {
            return;
        }

        // --- 3. MAIN MENU LOOP (PART 2) ---
        boolean running = true;

        while (running) {
            String act = JOptionPane.showInputDialog(
                "Please choose an option:\n" +
                "1) Send Messages\n" +
                "2) Show recently sent messages\n" +
                "3) Quit"
            );

            if (act == null || act.equals("3")) {
                JOptionPane.showMessageDialog(null, "Goodbye!");
                running = false;
                break;
            }

            if (act.equals("1")) {
                // Prompt user how many messages they wish to enter
                String numInput = JOptionPane.showInputDialog("Enter how many messages you wish to send:");
                if (numInput == null) continue;

                int totalMessages;
                try {
                    totalMessages = Integer.parseInt(numInput);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid number entered.");
                    continue;
                }

                // Loop for the set number of messages
                for (int i = 0; i < totalMessages; i++) {
                    String cell = JOptionPane.showInputDialog("Message " + (i + 1) + " - Enter Recipient cell (+ code, max 12 chars):");
                    String content = JOptionPane.showInputDialog("Message " + (i + 1) + " - Enter message content (max 250 characters):");

                    Message msg = new Message(i, cell, content);

                    // Validate Cell
                    String cellValidation = msg.checkRecipientCell();
                    if (!cellValidation.startsWith("Cell phone number successfully")) {
                        JOptionPane.showMessageDialog(null, cellValidation);
                        i--; // Retry this message iteration
                        continue;
                    }

                    // Validate Length
                    String lengthValidation = msg.checkMessageLength();
                    if (!lengthValidation.equals("Message ready to send.")) {
                        JOptionPane.showMessageDialog(null, lengthValidation);
                        i--; // Retry this message iteration
                        continue;
                    }

                    // Sub-menu for Send / Discard / Store
                    String choiceStr = JOptionPane.showInputDialog(
                        "Message validated successfully!\n" +
                        "Choose an option:\n" +
                        "1. Send Message\n" +
                        "2. Disregard Message\n" +
                        "3. Store Message to send later"
                    );

                    if (choiceStr != null) {
                        int choice = Integer.parseInt(choiceStr);
                        String result = msg.SentMessage(choice);
                        
                        if (choice == 2) {
                            String del = JOptionPane.showInputDialog(result); // "Press 0 to delete the message."
                            if (del != null && del.equals("0")) {
                                JOptionPane.showMessageDialog(null, "Message successfully deleted.");
                            } else {
                                JOptionPane.showMessageDialog(null, "Message status changed to Disregarded.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, 
                                "--- Message Details ---\n" +
                                "Message ID: " + msg.getMessageID() + "\n" +
                                "Message Hash: " + msg.getMessageHash() + "\n" +
                                "Recipient: " + msg.getRecipientCell() + "\n" +
                                "Message: " + msg.getMessageContent() + "\n\n" +
                                result
                            );
                        }
                    }
                }

                // Display total accumulated messages sent/stored
                JOptionPane.showMessageDialog(null, "Total messages processed: " + Message.returnTotalMessagess() + "\n\n" + Message.printMessages());

            } else if (act.equals("2")) {
                JOptionPane.showMessageDialog(null, "Coming Soon.");
            }
        }
    }
}