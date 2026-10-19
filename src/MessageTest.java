import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testCheckMessageLengthValid() {
        Message msg = new Message(0, "+2782123", "Hello world");
        assertEquals("Message ready to send.", msg.checkMessageLength());
    }

    @Test
    public void testCheckMessageLengthInvalid() {
        String longText = "a".repeat(260);
        Message msg = new Message(0, "+2782123", longText);
        assertTrue(msg.checkMessageLength().contains("exceeds 250 characters"));
    }

    @Test
    public void testCheckRecipientCellValid() {
        Message msg = new Message(0, "+2782123", "Test message");
        assertEquals("Cell phone number successfully captured.", msg.checkRecipientCell());
    }

    @Test
    public void testCheckRecipientCellInvalid() {
        Message msg = new Message(0, "082123456789", "Test message");
        assertTrue(msg.checkRecipientCell().contains("incorrectly formatted"));
    }

    @Test
    public void testCreateMessageHash() {
        Message msg = new Message(1, "+2782123", "Hi Mike, can you join us for dinner tonight?");
        String hash = msg.createMessageHash();
        assertNotNull(hash);
        assertTrue(hash.contains("HIMICHAEL") || hash.contains("HITONIGHT"));
    }

    @Test
    public void testStoreMessageArray() {
        Message msg = new Message(0, "+2782123", "Storage test");
        boolean stored = Message.storeMessage(msg);
        assertTrue(stored);
    }
}