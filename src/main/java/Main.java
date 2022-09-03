import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> listCSV = parseCSV(columnMapping, fileName);
        String json = listToJson(listCSV);
        writeString(json, "data.json");

        List<Employee> listXML = parseXML("data.xml");
        writeString(listToJson(listXML), "data2.json");

        String json2 = readString("data.json");
        List<Employee> employeeList = jsonToList(json2);
        for (Employee employee : employeeList) {
            System.out.println(employee);
        }

    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> staff = null;

        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();

            staff = csv.parse();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return staff;
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.toJson(list, listType);
    }

    private static void writeString(String json, String nameFileJson) {
        try (FileWriter fileWriter = new FileWriter(nameFileJson)) {
            fileWriter.write(json);
            fileWriter.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static List<Employee> parseXML(String nameFileXml) {
        List<Employee> employeeList = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document doc = documentBuilder.parse(new File(nameFileXml));

            Node root = doc.getDocumentElement();
            employeeList = read(root, employeeList);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }

        return employeeList;
    }

    private static List<Employee> read(Node root, List<Employee> employeeList) {
        NodeList nodeList = root.getChildNodes();

        long id = 0;
        String firstName = null;
        String lastName = null;
        String country = null;
        int age = 0;

        for (int i = 0; i < nodeList.getLength(); i++) {
            if (Node.ELEMENT_NODE == nodeList.item(i).getNodeType()) {
                Node node_ = nodeList.item(i);

                switch (node_.getNodeName()) {
                    case "id" -> id = Long.parseLong(node_.getTextContent());
                    case "firstName" -> firstName = node_.getTextContent();
                    case "lastName" -> lastName = node_.getTextContent();
                    case "country" -> country = node_.getTextContent();
                    case "age" -> {
                        age = Integer.parseInt(node_.getTextContent());
                        if (id != 0 && firstName != null && lastName != null && country != null && age != 0) {
                            Employee employee = new Employee(id, firstName, lastName, country, age);
                            employeeList.add(employee);
                        }
                    }
                }

                read(node_, employeeList);
            }
        }
        return employeeList;
    }

    private static String readString(String nameFileJson) {

        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(nameFileJson))) {
            String s;
            while ((s = br.readLine()) != null) {
                stringBuilder.append(s);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return String.valueOf(stringBuilder);
    }

    private static List<Employee> jsonToList(String json2) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.fromJson(json2, listType);
    }
}


