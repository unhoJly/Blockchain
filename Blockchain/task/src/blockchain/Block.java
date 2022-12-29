package blockchain;

import java.io.Serializable;

public class Block implements Serializable {
    private static final long serialVersionUID = 1L;
    int id;
    long creationTime;
    long randomNumber;
    long timeToGenerate;
    String previousHash;
    String currentHash;
    String message;

    public String getCurrentHash() {
        return currentHash;
    }

    @Override
    public String toString() {
        return "Id: " + id
                + "\nTimestamp: " + creationTime
                + "\nMagic number: " + randomNumber
                + "\nHash of the previous block:\n"
                + previousHash
                + "\nHash of the block:\n"
                + currentHash;
    }
}