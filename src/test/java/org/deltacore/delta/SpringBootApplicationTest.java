package org.deltacore.delta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SpringBootApplicationTest {

    @Test
    public void contextLoads() {
        String token = "wery37ry873fhw8fh97u8whf789f784yr78uwurhf8378ry8";
        String authHeader = "Bearer wery37ry873fhw8fh97u8whf789f784yr78uwurhf8378ry8";
        String tokenReplace = authHeader.replaceFirst("Bearer", "").trim();


        Assertions.assertEquals(token, tokenReplace);
    }

}
