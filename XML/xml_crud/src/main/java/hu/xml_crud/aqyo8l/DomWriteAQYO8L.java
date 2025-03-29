package hu.xml_crud.aqyo8l;

import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DomWriteAQYO8L {
    public static void main(String args[]) {
        try {
            // DocumentFactory inicializálása
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // DocumentBuilder inicializálása
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Létrehozza a dokumentumot
            Document doc = builder.newDocument();

            // Gyökér elem létrehozása
            Element rootElement = doc.createElement("Aruhaz-beszallito_AQYO8L");

            rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            rootElement.setAttribute("xsi:noNamespaceSchemaLocation", "XMLSchemaAQYO8L.xsd");

            // Gyökér elem hozzáadása a dokumentumhoz
            doc.appendChild(rootElement);

            // Áruházak "doboz" elem létrehozása
            Element aruhazak = createBoxElement(doc, rootElement, "Aruhazak");

            // Beszállítók "doboz" elem létrehozása
            Element beszallitok = createBoxElement(doc, rootElement, "Beszallitok");

            // Raktárak "doboz" elem létrehozása
            Element raktarak = createBoxElement(doc, rootElement, "Raktarak");

            // Akciós termékek "doboz" elem létrehozása
            Element akcios_termekek = createBoxElement(doc, rootElement, "Akcios_termekek");

            // Áruházak hozzáadása
            addAruhaz(doc, aruhazak, "1", "Coop", "3534, Miskolc, Szentpáli u, 1");
            addAruhaz(doc, aruhazak, "2", "Tesco", "3535, Miskolc, Példa u, 2");
            addAruhaz(doc, aruhazak, "3", "Auchan", "1001, Budapest, Mint u, 3");
            addAruhaz(doc, aruhazak, "4", "Spar", "2345, Székesfehérvár, Próba u, 4");
            addAruhaz(doc, aruhazak, "5", "Lidl", "9876, Szendrő, Teszt u, 5");

            // Beszállítók hozzáadása
            addBeszallito(doc, beszallitok, "1", "Kaja futár", "Élelmiszer", "nap", "2");
            addBeszallito(doc, beszallitok, "2", "Szomjoltó", "Üdítő", "nap", "1");
            addBeszallito(doc, beszallitok, "3", "Szomjoltó 2", "Üdítő", "nap", "1");
            addBeszallito(doc, beszallitok, "4", "Adalék-élelmiszer", "Élelmiszer", "nap", "2");
            addBeszallito(doc, beszallitok, "5", "Csokitanya", "Élelmiszer", "nap", "3");

            // Raktár termékek hozzáadása
            addAruhazRaktarTermek(doc, raktarak, "1", "1", "Nescafe", "100", "Kávé", "1000", "huf");
            addAruhazRaktarTermek(doc, raktarak, "1", "2", "Coca-Cola", "100", "Üdítő", "500", "huf");
            addAruhazRaktarTermek(doc, raktarak, "2", "3", "Pepsi", "100", "Üdítő", "500", "huf");
            addAruhazRaktarTermek(doc, raktarak, "3", "4", "Hellmann's", "100", "Élelmiszer", "1000", "huf");
            addAruhazRaktarTermek(doc, raktarak, "3", "5", "Mars", "100", "Élelmiszer", "500", "huf");

            addBeszallitoRaktarTermek(doc, raktarak, "1", "1", "Nescafe", "178", "Kávé");
            addBeszallitoRaktarTermek(doc, raktarak, "2", "2", "Coca-Cola", "21", "Üdítő");
            addBeszallitoRaktarTermek(doc, raktarak, "3", "3", "Pepsi", "76", "Üdítő");
            addBeszallitoRaktarTermek(doc, raktarak, "4", "4", "Hellmann's", "134", "Élelmiszer");
            addBeszallitoRaktarTermek(doc, raktarak, "5", "5", "Mars", "200", "Élelmiszer");

            // Akciós termékek hozzáadása
            // Akciós termékek árait HashMap-ben tárolom, így találtam a legegyszerűbbnek a felépítését
            HashMap<String, String> normalPrices1 = new HashMap<String, String>();
            normalPrices1.put("huf", "1000");
            normalPrices1.put("eu", "3");
            HashMap<String, String> discountPrices1 = new HashMap<String, String>();
            discountPrices1.put("huf", "500");
            discountPrices1.put("eu", "1.5");
            addAkciosTermek(doc, akcios_termekek, "1", "1", "Nescafe", "Finom, lágy és krémes: Ez a mi kávénk! Nescafe",
                    "Kávé", normalPrices1,
                    discountPrices1);

            HashMap<String, String> normalPrices2 = new HashMap<String, String>();
            normalPrices2.put("huf", "500");
            normalPrices2.put("eu", "1.5");
            HashMap<String, String> discountPrices2 = new HashMap<String, String>();
            discountPrices2.put("huf", "250");
            discountPrices2.put("eu", "0.75");
            addAkciosTermek(doc, akcios_termekek, "2", "2", "Coca-Cola",
                    "A Coca-Cola egy szénsavas üdítőital, amelyet a Coca-Cola Company gyárt.", "Üdítő", normalPrices2,
                    discountPrices2);

            HashMap<String, String> normalPrices3 = new HashMap<String, String>();
            normalPrices3.put("huf", "500");
            normalPrices3.put("eu", "1.5");
            HashMap<String, String> discountPrices3 = new HashMap<String, String>();
            discountPrices3.put("huf", "250");
            discountPrices3.put("eu", "0.75");
            addAkciosTermek(doc, akcios_termekek, "3", "3", "Pepsi",
                    "A Pepsi egy szénsavas üdítőital, amelyet a PepsiCo gyárt.", "Üdítő", normalPrices3,
                    discountPrices3);

            // Áruház-beszállító kapcsolatok
            addAruhazBeszallitoKapcsolat(doc, rootElement, "1", "1", "125");
            addAruhazBeszallitoKapcsolat(doc, rootElement, "1", "2", "200");
            addAruhazBeszallitoKapcsolat(doc, rootElement, "2", "3", "100");
            addAruhazBeszallitoKapcsolat(doc, rootElement, "3", "4", "20");
            addAruhazBeszallitoKapcsolat(doc, rootElement, "3", "5", "300");

            // Dokumentum kiírása, mentése
            File outputFile = new File("XML\\XMLAQYO8L_New.xml");

            PrintWriter file = new PrintWriter(outputFile, "UTF-8");

            printHeader(doc, file);

            readAruhaz(doc, file);

            readBeszallito(doc, file);

            readAruhazBeszallito(doc, file);

            readRaktarak(doc, file);

            readAkciosTermekek(doc, file);

            printToFileAndConsole("</Aruhaz-beszallito_AQYO8L>", System.out, file);

            file.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Kiíró, adatfeldolgozó rész kezdete */

    // Kiíró metódus
    private static void printToFileAndConsole(final String msg, PrintStream console, PrintWriter file) {
        console.println(msg);
        file.println(msg);
    }

    // Fejrész elkészítő metódus
    private static void printHeader(Document doc, PrintWriter file) {
        printToFileAndConsole("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", System.out, file);
        printToFileAndConsole(
                "<Aruhaz-beszallito_AQYO8L xmlns:xs=\"http://www.w3.org/2001/XMLSchema-instance\" xs:noNamespaceSchemaLocation=\"XMLSchemaAQYO8L.xsd\">",
                System.out, file);
    }

    // Elem kiírás formázó metódus
    private static void printElement(String elementName, String content, PrintWriter file) {
        printToFileAndConsole("            <" + elementName + ">" + content + "</" + elementName + ">", System.out,
                file);
    }

    // Cím kiírás formázó metódus
    private static void printCim(Element cimElement, PrintWriter file) {
        printToFileAndConsole("            <" + cimElement.getNodeName() + ">", System.out, file);
        printToFileAndConsole("               <" + cimElement.getElementsByTagName("Iranyitoszam").item(0).getNodeName()
                + ">" + cimElement.getElementsByTagName("Iranyitoszam").item(0).getTextContent() + "</"
                + cimElement.getElementsByTagName("Iranyitoszam").item(0).getNodeName() + ">",
                System.out, file);
        printToFileAndConsole("               <" + cimElement.getElementsByTagName("Telepules").item(0).getNodeName()
                + ">" + cimElement.getElementsByTagName("Telepules").item(0).getTextContent() + "</"
                + cimElement.getElementsByTagName("Telepules").item(0).getNodeName() + ">",
                System.out, file);
        printToFileAndConsole("               <" + cimElement.getElementsByTagName("Utca").item(0).getNodeName() + ">"
                + cimElement.getElementsByTagName("Utca").item(0).getTextContent() + "</"
                + cimElement.getElementsByTagName("Utca").item(0).getNodeName() + ">",
                System.out, file);
        printToFileAndConsole("               <" + cimElement.getElementsByTagName("Hazszam").item(0).getNodeName()
                + ">" + cimElement.getElementsByTagName("Hazszam").item(0).getTextContent() + "</"
                + cimElement.getElementsByTagName("Hazszam").item(0).getNodeName() + ">", System.out, file);
        printToFileAndConsole("            </" + cimElement.getNodeName() + ">", System.out, file);
    }

    // Áruházakat beolvasó metódus
    private static void readAruhaz(Document document, PrintWriter file) {
        NodeList aruhazList = document.getElementsByTagName("Aruhaz");
        printToFileAndConsole("  <Aruhazak>", System.out, file);
        for (int temp = 0; temp < aruhazList.getLength(); temp++) {
            Node node = aruhazList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element aruhazElement = (Element) node;
                String nev = aruhazElement.getElementsByTagName("Nev").item(0).getTextContent();
                String aruhazId = aruhazElement.getAttribute("aruhazid");
                Element cimElement = (Element) aruhazElement.getElementsByTagName("Cim").item(0);

                printToFileAndConsole("        <Aruhaz aruhazid=\"" + aruhazId + "\">", System.out, file);
                printElement("Nev", nev, file);
                printCim(cimElement, file);
                printToFileAndConsole("        </Aruhaz>", System.out, file);
            }
        }
        printToFileAndConsole("   </Aruhazak>", System.out, file);
    }

    // Beszállítókat beolvasó metódus
    private static void readBeszallito(Document document, PrintWriter file) {
        NodeList beszallitoList = document.getElementsByTagName("Beszallito");
        printToFileAndConsole("  <Beszallitok>", System.out, file);
        for (int temp = 0; temp < beszallitoList.getLength(); temp++) {
            Node node = beszallitoList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element beszallitoElement = (Element) node;
                String nev = beszallitoElement.getElementsByTagName("Nev").item(0).getTextContent();
                String beszallitoid = beszallitoElement.getAttribute("beszallitoid");
                String termekKategoria = beszallitoElement.getElementsByTagName("Termek_kategoria").item(0)
                        .getTextContent();
                Element atlagosSzallitasiIdoElement = (Element) beszallitoElement
                        .getElementsByTagName("Atlagos_szallitasi_ido").item(0);
                String atlagosSzallitasiIdoString = atlagosSzallitasiIdoElement.getTextContent();
                String atlagosSzallitasiIdoMertekegyseg = atlagosSzallitasiIdoElement.getAttribute("mertekegyseg");

                printToFileAndConsole("        <Beszallito beszallitoid=\"" + beszallitoid + "\">", System.out, file);
                printElement("Nev", nev, file);
                printElement("Termek_kategoria", termekKategoria, file);
                printToFileAndConsole(
                        "            <Atlagos_szallitasi_ido mertekegyseg=\"" + atlagosSzallitasiIdoMertekegyseg
                                + "\">" + atlagosSzallitasiIdoString + "</Atlagos_szallitasi_ido>",
                        System.out, file);
                printToFileAndConsole("        </Beszallito>", System.out, file);
            }
        }
        printToFileAndConsole("    </Beszallitok>", System.out, file);
    }

    // Áruház-Beszállító kapcsolatokat beolvasó metódus
    private static void readAruhazBeszallito(Document document, PrintWriter file) {
        NodeList aruhazBeszallitoList = document.getElementsByTagName("Aruhaz-Beszallito");
        for (int temp = 0; temp < aruhazBeszallitoList.getLength(); temp++) {
            Node node = aruhazBeszallitoList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element aruhazBeszallitoElement = (Element) node;
                String atlagos_Rendelt_Arumennyiseg = aruhazBeszallitoElement
                        .getElementsByTagName("Atlagos_Rendelt_Arumennyiseg").item(0).getTextContent();
                String beszallitoid = aruhazBeszallitoElement.getAttribute("beszallitoid");
                String aruhazid = aruhazBeszallitoElement.getAttribute("aruhazid");

                printToFileAndConsole("       <Aruhaz-Beszallito aruhazid=\"" + aruhazid + "\" beszallitoid=\""
                        + beszallitoid + "\">", System.out, file);
                printElement("Atlagos_Rendelt_Arumennyiseg", atlagos_Rendelt_Arumennyiseg, file);
                printToFileAndConsole("       </Aruhaz-Beszallito>", System.out, file);
            }
        }
    }

    // Raktárakat beolvasó metódus
    private static void readRaktarak(Document document, PrintWriter file) {
        NodeList aruhazRaktarTermekList = document.getElementsByTagName("Aruhaz_Raktar_Termek");
        NodeList beszallitoRaktarTermekList = document.getElementsByTagName("Beszallito_Raktar_Termek");
        printToFileAndConsole("  <Raktarak>", System.out, file);

        for (int temp = 0; temp < aruhazRaktarTermekList.getLength(); temp++) {
            Node node = aruhazRaktarTermekList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element aruhaRaktarElement = (Element) node;
                String termekid = aruhaRaktarElement.getAttribute("termekid");
                String aruhazid = aruhaRaktarElement.getAttribute("aruhazid");
                String nev = aruhaRaktarElement.getElementsByTagName("Nev").item(0).getTextContent();
                String darabszam = aruhaRaktarElement.getElementsByTagName("Darabszam").item(0).getTextContent();
                String kategoria = aruhaRaktarElement.getElementsByTagName("Kategoria").item(0).getTextContent();
                Element arElement = (Element) aruhaRaktarElement.getElementsByTagName("Ar").item(0);
                String ar = arElement.getTextContent();
                String penznem = arElement.getAttribute("penznem");

                printToFileAndConsole("        <Aruhaz_Raktar_Termek aruhazid=\"" + aruhazid + "\" termekid=\""
                        + termekid + "\">", System.out, file);
                printElement("Nev", nev, file);
                printElement("Darabszam", darabszam, file);
                printElement("Kategoria", kategoria, file);
                printToFileAndConsole("            <Ar penznem=\"" + penznem + "\">" + ar + "</Ar>", System.out, file);
                printToFileAndConsole("       </Aruhaz_Raktar_Termek>", System.out, file);
            }
        }

        for (int temp2 = 0; temp2 < beszallitoRaktarTermekList.getLength(); temp2++) {
            Node node2 = beszallitoRaktarTermekList.item(temp2);
            if (node2.getNodeType() == Node.ELEMENT_NODE) {
                Element beszallitoRaktarElement = (Element) node2;
                String termekid = beszallitoRaktarElement.getAttribute("termekid");
                String beszallitoid = beszallitoRaktarElement.getAttribute("beszallitoid");
                String nev = beszallitoRaktarElement.getElementsByTagName("Nev").item(0).getTextContent();
                String darabszam = beszallitoRaktarElement.getElementsByTagName("Darabszam").item(0)
                        .getTextContent();
                String kategoria = beszallitoRaktarElement.getElementsByTagName("Kategoria").item(0)
                        .getTextContent();

                printToFileAndConsole(
                        "        <Beszallito_Raktar_Termek beszallitoid=\"" + beszallitoid + "\" termekid=\""
                                + termekid + "\">",
                        System.out, file);
                printElement("Nev", nev, file);
                printElement("Darabszam", darabszam, file);
                printElement("Kategoria", kategoria, file);
                printToFileAndConsole("       </Beszallito_Raktar_Termek>", System.out, file);
            }
        }
        printToFileAndConsole("  </Raktarak>", System.out, file);
    }

    // Akciós termékeket beolvasó metódus
    private static void readAkciosTermekek(Document document, PrintWriter file) {
        NodeList akciosTermekekList = document.getElementsByTagName("Akcios_Termek");
        printToFileAndConsole("  <Akcios_Termekek>", System.out, file);
        for (int temp = 0; temp < akciosTermekekList.getLength(); temp++) {
            Node node = akciosTermekekList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element akciosTermekElement = (Element) node;
                String termekid = akciosTermekElement.getAttribute("termekid");
                String akciostermekid = akciosTermekElement.getAttribute("akciostermekid");
                String nev = akciosTermekElement.getElementsByTagName("Nev").item(0).getTextContent();
                String leiras = akciosTermekElement.getElementsByTagName("Leiras").item(0).getTextContent();
                String kategoria = akciosTermekElement.getElementsByTagName("Kategoria").item(0).getTextContent();

                NodeList eredetiArList = akciosTermekElement.getElementsByTagName("Eredeti_ar");
                NodeList akciosArList = akciosTermekElement.getElementsByTagName("Akcios_ar");

                printToFileAndConsole("        <Akcios_Termek termekid=\"" + termekid + "\" akciostermekid=\""
                        + akciostermekid + "\">", System.out, file);
                printElement("Nev", nev, file);
                printElement("Leiras", leiras, file);
                printElement("Kategoria", kategoria, file);

                printToFileAndConsole("            <Arak>", System.out, file);

                for (int temp2 = 0; temp2 < eredetiArList.getLength(); temp2++) {
                    Node node2 = eredetiArList.item(temp2);
                    if (node2.getNodeType() == Node.ELEMENT_NODE) {
                        Element eredetiAr = (Element) node2;
                        String penznem = eredetiAr.getAttribute("penznem");
                        String ar = eredetiAr.getTextContent();
                        printToFileAndConsole(
                                "                <Eredeti_ar penznem=\"" + penznem + "\">" + ar + "</Eredeti_ar>",
                                System.out, file);
                    }
                }

                for (int temp3 = 0; temp3 < akciosArList.getLength(); temp3++) {
                    Node node3 = akciosArList.item(temp3);
                    if (node3.getNodeType() == Node.ELEMENT_NODE) {
                        Element akciosAr = (Element) node3;
                        String penznem = akciosAr.getAttribute("penznem");
                        String ar = akciosAr.getTextContent();
                        printToFileAndConsole(
                                "                <Akcios_ar penznem=\"" + penznem + "\">" + ar + "</Akcios_ar>",
                                System.out, file);
                    }
                }

                printToFileAndConsole("            </Arak>", System.out, file);

                printToFileAndConsole("       </Akcios_Termek>", System.out, file);
            }
        }
        printToFileAndConsole("  </Akcios_Termekek>", System.out, file);
    }

    /* Hozzáadó rész kezdete */

    // "Doboz" elem létrehozása
    private static Element createBoxElement(Document doc, Element rootElement, String name) {
        Element box = doc.createElement(name);
        rootElement.appendChild(box);

        return box;
    }

    // Áruház hozzáadása
    private static void addAruhaz(Document doc, Element rootElement, String aruhaz_id, String name, String address) {
        Element aruhaz = doc.createElement("Aruhaz");
        aruhaz.setAttribute("aruhazid", aruhaz_id);

        Element nevElement = createElementAndAddToDoc(doc, "Nev", name);
        Element cimElement = doc.createElement("Cim");
        Element iranyitoszamElement = createElementAndAddToDoc(doc, "Iranyitoszam", address.split(",")[0].trim());
        Element telepulesElement = createElementAndAddToDoc(doc, "Telepules", address.split(",")[1].trim());
        Element utcaElement = createElementAndAddToDoc(doc, "Utca", address.split(",")[2].trim());
        Element hazszamElement = createElementAndAddToDoc(doc, "Hazszam", address.split(",")[3].trim());
        aruhaz.appendChild(nevElement);
        aruhaz.appendChild(cimElement);
        cimElement.appendChild(iranyitoszamElement);
        cimElement.appendChild(telepulesElement);
        cimElement.appendChild(utcaElement);
        cimElement.appendChild(hazszamElement);

        rootElement.appendChild(aruhaz);
    }

    // Beszállító hozzáadása
    private static void addBeszallito(Document doc, Element rootElement, String beszallito_id, String name,
            String category, String measureString, String time) {
        Element beszallito = doc.createElement("Beszallito");
        beszallito.setAttribute("beszallitoid", beszallito_id);

        Element nevElement = createElementAndAddToDoc(doc, "Nev", name);
        Element kategoriaElement = createElementAndAddToDoc(doc, "Termek_kategoria", category);
        Element szallitasiIdoElement = createElementAndAddToDoc(doc, "Atlagos_szallitasi_ido", time);
        szallitasiIdoElement.setAttribute("mertekegyseg", measureString);

        beszallito.appendChild(nevElement);
        beszallito.appendChild(kategoriaElement);
        beszallito.appendChild(szallitasiIdoElement);

        rootElement.appendChild(beszallito);
    }

    // Áruház-Berszállító kapcsolat hozzáadása
    private static void addAruhazBeszallitoKapcsolat(Document doc, Element rootElement, String beszallito_id,
            String aruhaz_id, String orderedGoods) {
        Element aruhazBeszallitoKapcs = doc.createElement("Aruhaz-Beszallito");
        aruhazBeszallitoKapcs.setAttribute("aruhazid", aruhaz_id);
        aruhazBeszallitoKapcs.setAttribute("beszallitoid", beszallito_id);

        Element rendeltTermekElement = createElementAndAddToDoc(doc, "Atlagos_Rendelt_Arumennyiseg", orderedGoods);
        aruhazBeszallitoKapcs.appendChild(rendeltTermekElement);

        rootElement.appendChild(aruhazBeszallitoKapcs);
    }

    // Áruház raktár termék hozzáadása
    private static void addAruhazRaktarTermek(Document doc, Element rootElement, String aruhaz_id, String termek_id,
            String name, String quantity, String category, String price, String priceExchange) {
        Element aruhazRaktarTermek = doc.createElement("Aruhaz_Raktar_Termek");
        aruhazRaktarTermek.setAttribute("aruhazid", aruhaz_id);
        aruhazRaktarTermek.setAttribute("termekid", termek_id);

        Element nevElement = createElementAndAddToDoc(doc, "Nev", name);
        Element quantityElement = createElementAndAddToDoc(doc, "Darabszam", quantity);
        Element kategoriaElement = createElementAndAddToDoc(doc, "Kategoria", category);
        Element arElement = createElementAndAddToDoc(doc, "Ar", price);
        arElement.setAttribute("penznem", priceExchange);

        aruhazRaktarTermek.appendChild(nevElement);
        aruhazRaktarTermek.appendChild(quantityElement);
        aruhazRaktarTermek.appendChild(kategoriaElement);
        aruhazRaktarTermek.appendChild(arElement);

        rootElement.appendChild(aruhazRaktarTermek);
    }

    // Beszállító raktár termék hozzáadása
    private static void addBeszallitoRaktarTermek(Document doc, Element rootElement, String beszallito_id,
            String termek_id, String name, String quantity, String category) {
        Element beszallitoRaktarTermek = doc.createElement("Beszallito_Raktar_Termek");
        beszallitoRaktarTermek.setAttribute("beszallitoid", beszallito_id);
        beszallitoRaktarTermek.setAttribute("termekid", termek_id);

        Element nevElement = createElementAndAddToDoc(doc, "Nev", name);
        Element quantityElement = createElementAndAddToDoc(doc, "Darabszam", quantity);
        Element kategoriaElement = createElementAndAddToDoc(doc, "Kategoria", category);

        beszallitoRaktarTermek.appendChild(nevElement);
        beszallitoRaktarTermek.appendChild(quantityElement);
        beszallitoRaktarTermek.appendChild(kategoriaElement);

        rootElement.appendChild(beszallitoRaktarTermek);
    }

    // Akciós termék hozzáadása
    private static void addAkciosTermek(Document doc, Element rootElement, String akcios_termek_id, String termek_id,
            String name, String description, String category, HashMap<String, String> normalPrices,
            HashMap<String, String> discountPrices) {
        Element akciosTermek = doc.createElement("Akcios_Termek");
        akciosTermek.setAttribute("akciostermekid", akcios_termek_id);
        akciosTermek.setAttribute("termekid", termek_id);

        Element nevElement = createElementAndAddToDoc(doc, "Nev", name);
        Element descriptionElement = createElementAndAddToDoc(doc, "Leiras", description);
        Element kategoriaElement = createElementAndAddToDoc(doc, "Kategoria", category);

        Element arakElement = doc.createElement("Arak");

        for (String key : normalPrices.keySet()) {
            Element arElement = createElementAndAddToDoc(doc, "Eredeti_ar", normalPrices.get(key));
            arElement.setAttribute("penznem", key);
            arakElement.appendChild(arElement);
        }

        for (String key : discountPrices.keySet()) {
            Element arElement = createElementAndAddToDoc(doc, "Akcios_ar", discountPrices.get(key));
            arElement.setAttribute("penznem", key);
            arakElement.appendChild(arElement);
        }

        akciosTermek.appendChild(nevElement);
        akciosTermek.appendChild(descriptionElement);
        akciosTermek.appendChild(kategoriaElement);
        akciosTermek.appendChild(arakElement);

        rootElement.appendChild(akciosTermek);
    }

    // Elem létrehozása és dokumentumhoz adása
    private static Element createElementAndAddToDoc(Document doc, String name, String value) {
        Element element = doc.createElement(name);
        element.appendChild(doc.createTextNode(value));
        return element;
    }

}