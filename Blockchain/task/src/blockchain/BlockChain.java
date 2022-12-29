package blockchain;

import java.security.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class BlockChain {
    List<Block> chain = new CopyOnWriteArrayList<>();
    List<byte[]> signedBlocks = new CopyOnWriteArrayList<>();
    PrivateKey privateKey;
    String hash;
    long minerId;
    int currentN = 0;
    int idCounter = 1;

    public synchronized Block initBlock() {
        Block block = new Block();
        minerId = Thread.currentThread().getId();

        long startTime = System.currentTimeMillis();

        block.id = idCounter;
        block.creationTime = new Date().getTime();
        block.randomNumber = System.currentTimeMillis() % 1000;
        block.currentHash = "0".repeat(currentN)
                + BlockChainUtils.applySha256(block.id + block.previousHash + block.randomNumber, this);

        if (idCounter == 1) {
            block.previousHash = "0";
        } else {
            block.previousHash = hash;
        }

        hash = block.currentHash;

        block.timeToGenerate = (System.currentTimeMillis() - startTime) / 1000;

        idCounter++;

        System.out.println("Block: ");
        System.out.println("Created by miner" + minerId);
        System.out.println("miner" + minerId + " gets 100 VC");
        System.out.println(block);

        System.out.println("Block data:\nКто-то кому-то перевёл сколько-то VC");

        System.out.println("Block was generating for " + block.timeToGenerate + " seconds");

        if (block.timeToGenerate > 60) {
            --currentN;
            System.out.println("N was decreased by 1\n");
        } else if (block.timeToGenerate < 10) {
            ++currentN;
            System.out.println("N was increased to " + currentN + "\n");
        } else {
            System.out.println("N stays the same\n");
        }

        signedBlocks.add(sign(block.getCurrentHash(), privateKey));

        return block;
    }

    public byte[] sign(String data, PrivateKey privateKey) {
        try {
            Signature rsa = Signature.getInstance("SHA1withRSA");
            rsa.initSign(privateKey);
            rsa.update(data.getBytes());
            return rsa.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            System.out.println("Не удалось подписать блок!");
        }
        return null;
    }
}