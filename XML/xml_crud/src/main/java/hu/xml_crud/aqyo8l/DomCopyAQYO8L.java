package hu.xml_crud.aqyo8l;

import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DomCopyAQYO8L {
    public static void main(String args[]) {
        try {
            // DocumentFactory inicializálása
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // DocumentBuilder inicializálása
            DocumentBuilder builder = factory.newDocumentBuilder();

            File file = new File("XML\\XMLAQYO8L.xml");

            // Dokumentum betöltése
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            // Kimeneti fájl inicializálása
            PrintWriter outfile = new PrintWriter(new File("XML\\XMLAQYO8L_Copy.xml"), "UTF-8");

            // XML adatok kiírása
            printToFileAndConsole("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", System.out, outfile);
            printToFileAndConsole(
                    "<Aruhaz-beszallito_AQYO8L xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"XMLSchemaAQYO8L.xsd\">",
                    System.out, outfile);

            // Áruházak beolvasása
            readAruhaz(doc, outfile);

            // Beszállítók beolvasása
            readBeszallito(doc, outfile);

            // Áruház-Beszállító kapcsolatok beolvasása
            readAruhazBeszallito(doc, outfile);

            // Raktárak beolvasása
            readRaktarak(doc, outfile);

            // Akciós termékek beolvasása
            readAkciosTermekek(doc, outfile);

            // XML gyökérelem lezárása
            printToFileAndConsole("</Aruhaz-beszallito_AQYO8L>", System.out, outfile);

            // Kimeneti fájl lezárása
            outfile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Kiíró metódus
    private static void printToFileAndConsole(final String msg, PrintStream console, PrintWriter file) {
        console.println(msg);
        file.println(msg);
    }

    // Eleme kiírás formázó metódus
    private static void printElement(String elementName, String content, PrintWriter file) {
        printToFileAndConsole("            <" + elementName + ">" + content + "</" + elementName + ">", System.out,
                file);
    }

    // Cím kiírás formázó metódus
    private static void printCim(Element cimElement, PrintWriter file) {
        printToFileAndConsole("            <" + cimElement.getNodeName() + ">", System.out, file);
        printToFileAndConsole(
                "               <" + cimElement.getElementsByTagName("Iranyitoszam").item(0).getNodeName()
                        + ">" + cimElement.getElementsByTagName("Iranyitoszam").item(0).getTextContent() + "</"
                        + cimElement.getElementsByTagName("Iranyitoszam").item(0).getNodeName() + ">",
                System.out, file);
        printToFileAndConsole(
                "               <" + cimElement.getElementsByTagName("Telepules").item(0).getNodeName()
                        + ">" + cimElement.getElementsByTagName("Telepules").item(0).getTextContent() + "</"
                        + cimElement.getElementsByTagName("Telepules").item(0).getNodeName() + ">",
                System.out, file);
        printToFileAndConsole(
                "               <" + cimElement.getElementsByTagName("Utca").item(0).getNodeName() + ">"
                        + cimElement.getElementsByTagName("Utca").item(0).getTextContent() + "</"
                        + cimElement.getElementsByTagName("Utca").item(0).getNodeName() + ">",
                System.out, file);
        printToFileAndConsole("             <" + cimElement.getElementsByTagName("Hazszam").item(0).getNodeName()
                + ">" + cimElement.getElementsByTagName("Hazszam").item(0).getTextContent() + "</"
                + cimElement.getElementsByTagName("Hazszam").item(0).getNodeName() + ">", System.out, file);
        printToFileAndConsole("                </" + cimElement.getNodeName() + ">", System.out, file);
    }

    // Áruházakat beolvasó metódus
    private static void readAruhaz(Document document, PrintWriter file) {
        NodeList aruhazList = document.getElementsByTagName("Aruhaz");
        printToFileAndConsole("    <Aruhazak>", System.out, file);
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
        printToFileAndConsole("    </Aruhazak>", System.out, file);
    }

    // Beszállítókat beolvasó metódus
    private static void readBeszallito(Document document, PrintWriter file) {
        NodeList beszallitoList = document.getElementsByTagName("Beszallito");
        printToFileAndConsole("    <Beszallitok>", System.out, file);
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
        printToFileAndConsole("     </Beszallitok>", System.out, file);
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

                printToFileAndConsole("     <Aruhaz-Beszallito aruhazid=\"" + aruhazid + "\" beszallitoid=\""
                        + beszallitoid + "\">", System.out, file);
                printToFileAndConsole("          <Atlagos_Rendelt_Arumennyiseg>" + atlagos_Rendelt_Arumennyiseg
                        + "</Atlagos_Rendelt_Arumennyiseg>", System.out, file);
                printToFileAndConsole("     </Aruhaz-Beszallito>", System.out, file);
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
}