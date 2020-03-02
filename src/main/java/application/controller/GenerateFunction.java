package application.controller;

import application.controller.object.Access;
import application.controller.object.Animal;
import application.controller.object.CashMovement;
import application.controller.object.User;
import application.model.CashMovementModel;
import application.view.auxiliary.Formatter;
import application.view.auxiliary.Function;
import javafx.collections.ObservableList;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
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
                double profitCash = 0;
                for (CashMovement cash : cashMovements) {
                    cashItems.add(new CashItem(cash));
                    if(cash.isClosed()){
                        double profitDouble = cash.getValue() - cash.getValue_closed_system();
                        profitCash += profitDouble;
                    }
                };

                map.put("ProfitCash", Formatter.formatMoney(profitCash));
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
}
