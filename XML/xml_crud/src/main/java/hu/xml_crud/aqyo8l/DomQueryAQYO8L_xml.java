package hu.xml_crud.aqyo8l;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class DomQueryAQYO8L_xml {
    public static void main(String args[]) {
        try {
            // DocumentBuilder inicializálása
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Dokumentum beolvasása
            Document document = builder.parse("XML\\XMLAQYO8L.xml");

            // Üres sor beszúrása a konzolon, a jobb olvashatóság érdekében
            System.out.println();

            System.out.println("Összes áruház lekérdezése:");
            // Összes áruház lekérdezése
            NodeList aruhazList = document.getElementsByTagName("Aruhaz");
            // Végigiterál az összes Aruhaz Node-on
            for (int i = 0; i < aruhazList.getLength(); i++) {
                Node node = aruhazList.item(i);
                // Megnézi, hogy az elem elem típusú-e
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element aruhaz = (Element) node;

                    // Kiírja az áruház ID-ját és nevét
                    System.out.println("<aruhaz" + " aruhazid=\"" + aruhaz.getAttribute("aruhazid") + "\"" + ">\n"
                            + "     <Nev>" + aruhaz.getElementsByTagName("Nev").item(0).getTextContent() + "</Nev>\n"
                            + "</aruhaz>");
                }
            }

            // Üres sor beszúrása a konzolon, a jobb olvashatóság érdekében
            System.out.println();

            System.out.println("Áruház-Beszállító kapcsolatok lekérdezése:");

            // Áruház-Beszállító kapcsolatok lekérdezése
            NodeList aruhazBeszallitoKapcsolatList = document.getElementsByTagName("Aruhaz-Beszallito");

            // Összes áruház lekérdezése
            aruhazList = document.getElementsByTagName("Aruhaz");

            // Összes beszállító lekérdezése
            NodeList beszallitoList = document.getElementsByTagName("Beszallito");

            // Végigiterál az összes Aruhaz-Beszallito Node-on
            for (int i = 0; i < aruhazBeszallitoKapcsolatList.getLength(); i++) {
                Node node = aruhazBeszallitoKapcsolatList.item(i);

                // Inicializál egy stringet, ami a kiírandó sor lesz
                String kiirtSor = "";

                // Megnézi, hogy az elem elem típusú-e
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element aruhazBeszallitoKapcsolat = (Element) node;

                    // Végigiterál az összes áruházon
                    for (int j = 0; j < aruhazList.getLength(); j++) {
                        Node aruhazNode = aruhazList.item(j);

                        // Megnézi, hogy az elem elem típusú-e
                        if (aruhazNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element aruhaz = (Element) aruhazNode;

                            // Megnézi, hogy az áruház ID-ja megegyezik-e az áruház ID-jával az
                            // áruház-beszállító kapcsolatban
                            if (aruhaz.getAttribute("aruhazid")
                                    .equals(aruhazBeszallitoKapcsolat.getAttribute("aruhazid"))) {
                                // Hozzáadja a kiírandó sorhoz az áruház nevét
                                kiirtSor += "<aruhaz aruhazid=\"" + aruhazBeszallitoKapcsolat.getAttribute("aruhazid")
                                        + "\">\n"
                                        + "     <Nev>" + aruhaz.getElementsByTagName("Nev").item(0).getTextContent() + "</Nev>\n"
                                        + "</aruhaz>\n";
                            }
                        }
                    }

                    // Végigiterál az összes beszállítón
                    for (int j = 0; j < beszallitoList.getLength(); j++) {
                        Node beszallitoNode = beszallitoList.item(j);
                        // Megnézi, hogy az elem elem típusú-e
                        if (beszallitoNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element beszallito = (Element) beszallitoNode;

                            // Megnézi, hogy a beszállító ID-ja megegyezik-e a beszállító ID-jával az
                            // áruház-beszállító kapcsolatban
                            if (beszallito.getAttribute("beszallitoid")
                                    .equals(aruhazBeszallitoKapcsolat.getAttribute("beszallitoid"))) {
                                // Hozzáadja a kiírandó sorhoz a beszállító nevét
                                kiirtSor += "<beszallito beszallitoid=\""
                                        + aruhazBeszallitoKapcsolat.getAttribute("beszallitoid") + "\">\n"
                                        + "     <Nev>" +  beszallito.getElementsByTagName("Nev").item(0).getTextContent() + "</Nev>\n"
                                        + "</beszallito>";
                            }
                        }
                    }

                    // Kiírja a kapcsolatban álló áruházak és beszállítók neveit
                    System.out.println(kiirtSor);
                }
            }

            // Üres sor beszúrása a konzolon, a jobb olvashatóság érdekében
            System.out.println();

            System.out.println("Legtöbb árut rendelő áruház lekérdezése: ");

            // Áruház-Beszállító kapcsolatok lekérdezése
            aruhazBeszallitoKapcsolatList = document.getElementsByTagName("Aruhaz-Beszallito");

            // Összes áruház lekérdezése
            aruhazList = document.getElementsByTagName("Aruhaz");

            // Inicializál egy stringet, ami a kiírandó sor lesz
            String kiirtSor = "";

            // Inicializál egy int-et, ami a darabszámot fogja tárolni
            int aruDarab = 0;

            // Végigiterál az összes Aruhaz-Beszallito Node-on
            for (int i = 0; i < aruhazBeszallitoKapcsolatList.getLength(); i++) {
                Node node = aruhazBeszallitoKapcsolatList.item(i);

                // Megnézi, hogy az elem elem típusú-e
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element aruhazBeszallitoKapcsolat = (Element) node;

                    // Végigiterál az összes áruházon
                    for (int j = 0; j < aruhazList.getLength(); j++) {
                        Node aruhazNode = aruhazList.item(j);

                        // Megnézi, hogy az elem elem típusú-e
                        if (aruhazNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element aruhaz = (Element) aruhazNode;

                            int rendeltMennyiseg = Integer.valueOf(aruhazBeszallitoKapcsolat
                                    .getElementsByTagName("Atlagos_Rendelt_Arumennyiseg").item(0).getTextContent());

                            // Megnézi, hogy az áruház ID-ja megegyezik-e az áruház ID-jával az
                            // áruház-beszállító kapcsolatban
                            if (aruhaz.getAttribute("aruhazid")
                                    .equals(aruhazBeszallitoKapcsolat.getAttribute("aruhazid"))
                                    && rendeltMennyiseg > aruDarab) {
                                // Beállítja a legtöbb árut rendelő áruház darabszámát
                                aruDarab = rendeltMennyiseg;
                                // Hozzáadja a kiírandó sorhoz az áruház nevét
                                kiirtSor = "<aruhaz aruhazid=\"" + aruhazBeszallitoKapcsolat.getAttribute("aruhazid")
                                        + "\">\n" + "     <Nev>" + aruhaz.getElementsByTagName("Nev").item(0).getTextContent() + "</Nev>\n"
                                        + "</aruhaz>";
                            }
                        }
                    }
                }
            }
            // Kiírja a legtöbb árut rendelő áruház nevét és a rendelt áruk darabszámát
            System.out.println(kiirtSor);

            // Üres sor beszúrása a konzolon, a jobb olvashatóság érdekében
            System.out.println();

            System.out.println("Összes Miskolcon található áruház lekérdezése:");
            // Összes áruház lekérdezése
            aruhazList = document.getElementsByTagName("Aruhaz");
            // Végigiterál az összes Aruhaz Node-on
            for (int i = 0; i < aruhazList.getLength(); i++) {
                Node node = aruhazList.item(i);
                // Megnézi, hogy az elem elem típusú-e
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element aruhaz = (Element) node;
                    // Lekéri a címet
                    Node cimNode = aruhaz.getElementsByTagName("Cim").item(0);
                    Element cim = (Element) cimNode;
                    // Lekéri a települést
                    Node telepulesNode = cim.getElementsByTagName("Telepules").item(0);

                    // Ha az áruház Miskolcon található, akkor kiírja az áruház ID-ját, nevét és
                    // pontos címét
                    if (telepulesNode.getTextContent().equals("Miskolc")) {
                        System.out.println("<aruhaz aruhazid=\"" + aruhaz.getAttribute("aruhazid") + "\">\n"
                                + "     <Nev>" + aruhaz.getElementsByTagName("Nev").item(0).getTextContent() + "</Nev>\n"
                                + "     <Cim>\n"
                                + "         <Iranyitoszam>"
                                + cim.getElementsByTagName("Iranyitoszam").item(0).getTextContent()
                                + "</Iranyitoszam>\n"
                                + "         <Telepules>" + cim.getElementsByTagName("Telepules").item(0).getTextContent()
                                + "</Telepules>\n"
                                + "         <Utca>" + cim.getElementsByTagName("Utca").item(0).getTextContent() + "</Utca>\n"
                                + "         <Hazszam>" + cim.getElementsByTagName("Hazszam").item(0).getTextContent() + "</Hazszam>\n"
                                + "     </Cim>"
                                + "\n</aruhaz>");
                    }
                }
            }

            // Üres sor beszúrása a konzolon, a jobb olvashatóság érdekében
            System.out.println();

            System.out.println("Olyan beszállítók lekérdezése, amelyek legfeljebb 2 óra alatt szállítanak ki:");
            // Összes beszallito lekérdezése
            beszallitoList = document.getElementsByTagName("Beszallito");

            // Inicializál egy stringet, ami a kiírandó sor lesz
            kiirtSor = "";

            // Végigiterál az összes Beszallito Node-on
            for (int i = 0; i < beszallitoList.getLength(); i++) {
                Node node = beszallitoList.item(i);

                // Megnézi, hogy az elem elem típusú-e
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element beszallito = (Element) node;

                    // Lekéri az átlagos szállítási időt
                    Node szallitasiIdoNode = beszallito.getElementsByTagName("Atlagos_szallitasi_ido").item(0);

                    // Átcastolja Element típusra
                    Element szallitasiIdo = (Element) szallitasiIdoNode;

                    // Lekéri az időtartam mértékegységét
                    String mertekegyseg = szallitasiIdo.getAttribute("mertekegyseg");

                    // Ha az átlagos szállítási idő legfeljebb 2 óra, akkor hozzáadja a kiírandó
                    // sorhoz
                    if (mertekegyseg.equals("ora") && Integer.valueOf(szallitasiIdo.getTextContent()) <= 2
                            || mertekegyseg.equals("perc") && Integer.valueOf(szallitasiIdo.getTextContent()) <= 120) {
                        kiirtSor += "<Beszallito beszallitoid=\"" + beszallito.getAttribute("beszallitoid") + "\">\n"
                                + "     <Nev>" + beszallito.getElementsByTagName("Nev").item(0).getTextContent() + "</Nev>" + "\n     <Atlagos_szallitasi_ido mertekegyseg=\""+mertekegyseg+"\">"
                                + szallitasiIdo.getTextContent() + "</Atlagos_szallitasi_ido>" + "\n"
                                + "</Beszallito>\n";
                    }

                }
            }
            // Kiírja az eredményt
            System.out.println(kiirtSor);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}