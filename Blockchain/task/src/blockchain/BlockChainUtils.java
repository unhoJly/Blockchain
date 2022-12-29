package blockchain;

import java.io.*;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

public class BlockChainUtils {
    public static String applySha256(String input, BlockChain blockChain) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (byte elem: hash) {
                String hex = Integer.toHexString(0xff & elem);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            return hexString.substring(0, (hexString.length() - blockChain.currentN));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean blockChainValidate(BlockChain blockChain) {
        if (blockChain.chain.size() > 1)
            for (Block blk : blockChain.chain)
                if (!blk.getCurrentHash().equals(blk.previousHash)
                        || (blockChain.currentN > 0 && !blk.getCurrentHash().startsWith("0".repeat(blockChain.currentN))))
                    return false;

        return true;
    }

    public static void serialize(Object obj) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream("blockchain.data")
                ))
        ) {
            oos.writeObject(obj);
        } catch (IOException e) {
            System.out.println("Blockchain save error!");
        }
    }

    public static Object deserialize() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(
                        new FileInputStream("blockchain.data")
                ));
        return ois.readObject();
    }

    public static PrivateKey getPrivateKey() {
        PrivateKey privateKey = null;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            privateKey = keyPairGenerator.generateKeyPair().getPrivate();

            File f = new File("Keys/privateKey.key");
            f.getParentFile().mkdirs();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Keys/privateKey.key"));
            out.writeObject(privateKey);
            out.close();
        } catch (NoSuchAlgorithmException | IOException e) {
            System.out.println("Ошибка генерации закрытого ключа!");
        }
        return privateKey;
    }
}