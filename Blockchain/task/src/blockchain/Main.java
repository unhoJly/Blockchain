package blockchain;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        BlockChain blockChain = new BlockChain();
        ExecutorService miners = Executors.newCachedThreadPool();
        blockChain.privateKey = BlockChainUtils.getPrivateKey();
        int blocksNumber = blockChain.chain.size();

        for (int i = 0; i < 15 - blocksNumber; i++) {
            if (blockChain.chain.size() > 0) {
                try {
                    blockChain.chain = (CopyOnWriteArrayList<Block>) BlockChainUtils.deserialize();
                    if (!BlockChainUtils.blockChainValidate(blockChain)) {
                        System.out.println("Blockchain had been change, information compromised");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            miners.submit(() -> {
                blockChain.chain.add(blockChain.initBlock());
            });
            BlockChainUtils.serialize(blockChain.chain);
        }


        miners.shutdown();
        boolean isShutdown;
        do {
            try {
                isShutdown = miners.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                isShutdown = true;
            }
        } while (!isShutdown);
    }
}