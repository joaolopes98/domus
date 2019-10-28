package application.controller;

import application.controller.object.Animal;
import application.controller.object.User;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class GenerateFunction {

    public static Function veterinary(ObservableList<MedicineField> obsMedicine,
                                      Animal linkedAnimal,
                                      ArrayList<MedicineItem> medicineItems){
        return () -> {
            obsMedicine.clear();
            try {
                Map<String, Object> map = new HashMap<>();
                map.put("User", User.getUser().getName());
                map.put("Crmv", User.getUser().getCrmv());
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
            } catch (JRException | IOException e) {
                e.printStackTrace();
            }
        };
    }

    public static Function reportCash(){
        return () -> {};
    }
}
