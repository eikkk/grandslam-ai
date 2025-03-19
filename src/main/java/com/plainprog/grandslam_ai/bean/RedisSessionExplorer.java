package com.plainprog.grandslam_ai.bean;

import com.plainprog.grandslam_ai.model.db.TestTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.Map;

public class RedisSessionExplorer {

    private RedisSerializer serializer;

    public RedisSessionExplorer(RedisSerializer serializer){
        this.serializer = serializer;
    }

//    // Convert byte array to an int
//    private static int byteArrayToInt(byte[] bytes) {
//        ByteBuffer buffer = ByteBuffer.wrap(bytes);
//        return buffer.getInt();
//    }    // Convert byte array to a long
//    private static long byteArrayToLong(byte[] bytes) {
//        ByteBuffer buffer = ByteBuffer.wrap(bytes);
//        return buffer.getLong();
//    }
//    // Deserialize the byte array back into an Object
//    public static Object deserialize(byte[] bytes) {
//        if (bytes == null || bytes.length < 2) return null;
//        if (bytes[0] != (byte)0xac || bytes[1] != (byte)0xed) {
//            // Not a serialized Java object
//            return new String(bytes);  // Attempt using the string representation
//        }
//        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
//             ObjectInputStream in = new ObjectInputStream(bis)) {
//            return in.readObject();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
    public void inspectSession(String redisKey) {
        Jedis jedis = new Jedis("localhost", 5050);  // Ensure the host and port match your Redis setup
        Map<String, String> sessionData = jedis.hgetAll(redisKey);
        Map<byte[], byte[]> sessionDataByte = jedis.hgetAll(redisKey.getBytes());

//        for (Map.Entry<String, String> entry : sessionData.entrySet()) {
//            String field = entry.getKey();
//            byte[] value = entry.getValue().getBytes();
//            try {
//                MessagePackDeserializer deserializer = new MessagePackDeserializer();
//                Object deserializedValue = deserializer.deserialize(value, Object.class);
//                System.out.println(field + " : " + deserializedValue);
//            } catch (Exception e) {
//                System.out.println(field + " : " + new String(value));
//            }
//        }
        for (Map.Entry<byte[], byte[]> entry : sessionDataByte.entrySet()) {
            String field = new String(entry.getKey()); // Assuming the key can be a readable string
            byte[] value = entry.getValue();

            try {
//                MessagePackDeserializer deserializer = new MessagePackDeserializer();
//                Object deserializedValue = deserializer.deserialize(value, Object.class);
                if(field.equals("sessionAttr:TEST_TABLE")) {
                    TestTable deserializedValue = (TestTable) serializer.deserialize(value);
                    System.out.println(field + " : " + deserializedValue);
                } else {
                    Object deserializedValue = serializer.deserialize(value);
                    System.out.println(field + " : " + deserializedValue);
                }
            } catch (Exception e) {
                System.out.println(field + " : " + "Could not deserialize");
                e.printStackTrace();
            }
        }
        jedis.close();
    }
}