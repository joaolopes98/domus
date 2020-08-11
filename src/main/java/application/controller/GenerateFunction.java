package application.controller;

import application.controller.object.*;
import application.model.CashMovementModel;
import application.model.SaleModel;
import application.view.auxiliary.Formatter;
import application.view.auxiliary.Function;
import javafx.collections.ObservableList;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public abstract class GenerateFunction {

    public static Function veterinary(Access access, Animal linkedAnimal,
                                      ArrayList<MedicineItem> medicineItems){
        return () -> {
            try {
                Map<String, Object> map = new HashMap<>();
                map.put("User", access.getName());
                map.put("Crmv", access.getCrmv());
                map.put("Animal", linkedAnimal.getName());
                map.put("Specie", linkedAnimal.getSpecie());
                map.put("Customer", linkedAnimal.getCustomer().getName());

                try {
                    File fileImage = new File(GenerateFunction.class.getResource("/view/img/logoGrande.png").getFile());
                    BufferedImage image = ImageIO.read(fileImage);
                    map.put("Image", image);
                } catch (Exception e){
                    e.printStackTrace();
                }

                JasperPrint print = JasperFillManager.fillReport(
                        GenerateFunction.class.getResourceAsStream("/print/veterinary.jasper"),
                        map, new JRBeanCollectionDataSource(medicineItems));
                new File("C:/Domus").mkdir();
                JasperExportManager.exportReportToPdfFile(print, "C:/Domus/veterinary.pdf");
                Desktop.getDesktop().open(new File("C:\\Domus\\veterinary.pdf"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public static Function reportCash(long from, long to){
        return () -> {
            List<CashMovement> cashMovements = CashMovementModel.getAll(" WHERE date >= " + from + " AND " +
                    "date <= " + to + " ORDER BY date ASC");

            try {
                Map<String, Object> map = new HashMap<>();
                map.put("InitialDate", Formatter.formatDate(new Date(from)));
                map.put("FinalDate", Formatter.formatDate(new Date(to)));
                map.put("QuantityCash", String.valueOf(cashMovements.size()));

                File fileImage = new File(GenerateFunction.class.getResource("/view/img/logoGrande.png").getFile());
                BufferedImage image = ImageIO.read(fileImage);
                map.put("Image", image);

                List<CashItem> cashItems = new ArrayList<>();
                int quantitySale = 0;
                double total = 0;
                for (CashMovement cash : cashMovements) {
                    CashItem cashItem = new CashItem(cash);
                    cashItems.add(cashItem);
                    quantitySale += cash.getSales().size();
                    total += cashItem.getTotal();
                }

                double avgTicket = total / quantitySale;

                map.put("QuantitySale", quantitySale);
                map.put("AvgTicket", Formatter.formatMoney(avgTicket));
                System.out.println("CRIOU");
                JasperPrint print = JasperFillManager.fillReport(
                        GenerateFunction.class.getResourceAsStream("/print/cash.jasper"),
                        map, new JRBeanCollectionDataSource(cashItems));
                System.out.println("SALVOU");
                File dir = new File("C:/Domus");
                if(!dir.exists()) dir.mkdir();
                JasperExportManager.exportReportToPdfFile(print, "C:/Domus/cash.pdf");
                Desktop.getDesktop().open(new File("C:\\Domus\\cash.pdf"));
            } catch (JRException | IOException e) {
                e.printStackTrace();
            }
        };
    }

    public static Function reportSale(long from, long to) {
        return () -> {
            List<Sale> sales = SaleModel.getAll(" WHERE date >= " + from + " AND " +
                    "date <= " + to + " ORDER BY date ASC");

//            Sale bigger = SaleModel.get("ORDER BY value DESC, date DESC LIMIT 1");
//            Sale lower = SaleModel.get("ORDER BY value, date LIMIT 1");

            try {
                Map<String, Object> map = new HashMap<>();
                map.put("InitialDate", Formatter.formatDate(new Date(from)));
                map.put("FinalDate", Formatter.formatDate(new Date(to)));
                map.put("QuantitySale", sales.size());

                File fileImage = new File(GenerateFunction.class.getResource("/view/img/logoGrande.png").getFile());
                BufferedImage image = ImageIO.read(fileImage);
                map.put("Image", image);

                double total = 0;

                int chartMorning = 0;
                int chartAfternoon = 0;
                int chartNight = 0;
                for (Sale sale : sales) {
                    total += sale.getValue();

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(sale.getDate());
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    if(hour < 12){
                        chartMorning++;
                    } else if(hour <= 18){
                        chartAfternoon++;
                    } else {
                        chartNight++;
                    }
                    System.out.println(sale.getDate() + " - " + hour);
                }

                double avgTicket = total / sales.size();
                map.put("AvgTicket", Formatter.formatMoney(avgTicket));

                map.put("BiggerNumber", "AA");
                map.put("BiggerDate", "AA");
                map.put("BiggerUser", "AA");
                map.put("BiggerValue", "AA");
                map.put("BiggerDiscount", "AA");
                map.put("BiggerCustomer", "AA");

                map.put("LowerNumber", "AA");
                map.put("LowerDate", "AA");
                map.put("LowerUser", "AA");
                map.put("LowerValue", "AA");
                map.put("LowerDiscount", "AA");
                map.put("LowerCustomer", "AA");

                map.put("ChartMorning", chartMorning);
                map.put("ChartAfternoon", chartAfternoon);
                map.put("ChartNight", chartNight);

                System.out.println("CRIOU");
                JasperPrint print = JasperFillManager.fillReport(
                        GenerateFunction.class.getResourceAsStream("/print/sale.jasper"),
                        map, new JREmptyDataSource());
                System.out.println("SALVOU");
                File dir = new File("C:/Domus");
                if(!dir.exists()) dir.mkdir();
                JasperExportManager.exportReportToPdfFile(print, "C:/Domus/sale.pdf");
                Desktop.getDesktop().open(new File("C:\\Domus\\sale.pdf"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}
