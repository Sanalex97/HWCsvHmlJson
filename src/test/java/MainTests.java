import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainTests {
   static Employee employee1;
   static Employee employee2;

    @BeforeAll
    public static void Employee(){
        employee1 = new Employee(1, "John", "Smith", "USA", 25);
        employee2 = new Employee(2, "Ivan", "Petrov", "RU", 23);
    }
    @Test
    public void testListToJson() {
        // given:
        List<Employee> list = new ArrayList<>();
        list.add(employee1);
        list.add(employee2);

        String expected = "[\n" +
                "  {\n" +
                "    \"id\": 1,\n" +
                "    \"firstName\": \"John\",\n" +
                "    \"lastName\": \"Smith\",\n" +
                "    \"country\": \"USA\",\n" +
                "    \"age\": 25\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 2,\n" +
                "    \"firstName\": \"Ivan\",\n" +
                "    \"lastName\": \"Petrov\",\n" +
                "    \"country\": \"RU\",\n" +
                "    \"age\": 23\n" +
                "  }\n" +
                "]";
        // when:
        String result = Main.listToJson(list);
        // then:
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testReadString() {
        String nameFileJson = "data2.json";
        String expected = "[  {    \"id\": 1,    \"firstName\": \"John\",    \"lastName\": \"Smith\"," +
                "    \"country\": \"USA\",    \"age\": 25  }," +
                "  {    \"id\": 2,    \"firstName\": \"Ivan\",    \"lastName\": \"Petrov\"," +
                "    \"country\": \"RU\",    \"age\": 23  }]";

        String result = Main.readString(nameFileJson);

        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testJsonToList() {
        String json = "data2.json";
        List<Employee> expected = new ArrayList<>();
        expected.add(employee1);
        expected.add(employee2);

        List<Employee> result = Main.jsonToList(json);

        Assertions.assertArrayEquals(expected.toArray(), result.toArray());

    }

    @Test
    public void testWriteString() {
        String json = "hello";
        String nameFileJson = "h.json";

        Main.writeString(json, nameFileJson);

        Assertions.assertEquals(Main.readString(nameFileJson), json);
    }

}
