package com.campusdual.classroom;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Exercise27 {
    private static final Map<String, Integer> SHOPPING_ITEMS = new HashMap<>();

    static {
        SHOPPING_ITEMS.put("Manzana", 2);
        SHOPPING_ITEMS.put("Leche", 1);
        SHOPPING_ITEMS.put("Pan", 3);
        SHOPPING_ITEMS.put("Huevo", 2);
        SHOPPING_ITEMS.put("Queso", 1);
        SHOPPING_ITEMS.put("Cereal", 1);
        SHOPPING_ITEMS.put("Agua", 4);
        SHOPPING_ITEMS.put("Yogur", 6);
        SHOPPING_ITEMS.put("Arroz", 2);
    }

    public static void main(String[] args) {
        try {
            // Crear directorio resources si no existe
            Files.createDirectories(Paths.get("src/main/resources"));

            // Generar archivo XML
            createXMLFile();

            // Generar archivo JSON
            createJSONFile();

            System.out.println("Archivos generados correctamente.");
        } catch (Exception e) {
            System.err.println("Error al generar los archivos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createXMLFile() throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // Crear documento XML
        Document doc = docBuilder.newDocument();

        // Crear elemento raíz
        Element rootElement = doc.createElement("shoppinglist");
        doc.appendChild(rootElement);

        // Crear elemento items
        Element items = doc.createElement("items");
        rootElement.appendChild(items);

        // Añadir cada item
        for (Map.Entry<String, Integer> entry : SHOPPING_ITEMS.entrySet()) {
            Element item = doc.createElement("item");
            item.setAttribute("quantity", String.valueOf(entry.getValue()));
            item.setTextContent(entry.getKey());
            items.appendChild(item);
        }

        // Escribir el contenido en archivo XML
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("src/main/resources/shoppingList.xml"));
        transformer.transform(source, result);
    }

    private static void createJSONFile() throws Exception {
        JsonObject root = new JsonObject();
        JsonArray items = new JsonArray();

        // Añadir cada item al array JSON
        for (Map.Entry<String, Integer> entry : SHOPPING_ITEMS.entrySet()) {
            JsonObject item = new JsonObject();
            item.addProperty("quantity", entry.getValue());
            item.addProperty("text", entry.getKey());
            items.add(item);
        }

        root.add("items", items);

        // Crear Gson con formato bonito
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Escribir el contenido en archivo JSON
        try (FileWriter file = new FileWriter("src/main/resources/shoppingList.json")) {
            file.write(gson.toJson(root));
        }
    }
}