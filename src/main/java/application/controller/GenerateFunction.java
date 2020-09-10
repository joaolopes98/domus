package application.controller;

import application.controller.object.*;
import application.model.*;
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
import java.text.Normalizer;
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

            Sale bigger = SaleModel.get(" WHERE date >= " + from + " AND " +
                    "date <= " + to + " ORDER BY value DESC, date DESC");
            Sale lower = SaleModel.get(" WHERE date >= " + from + " AND " +
                    "date <= " + to + " ORDER BY value, date");

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
                    if(hour >= 6 && hour <= 11){
                        chartMorning++;
                    } else if(hour >= 12 && hour <= 18){
                        chartAfternoon++;
                    } else {
                        chartNight++;
                    }
                }

                if(sales.size() != 0) {
                    double avgTicket = total / sales.size();
                    map.put("AvgTicket", Formatter.formatMoney(avgTicket));

                    map.put("BiggerNumber", Formatter.formatStringCode(bigger.getId()));
                    map.put("BiggerDate", Formatter.formatDate(bigger.getDate()));
                    map.put("BiggerUser", bigger.getAccess().getName());
                    map.put("BiggerValue", Formatter.formatMoney(bigger.getValue()));
                    map.put("BiggerDiscount", Formatter.formatMoney(bigger.getDiscount()));
                    if (bigger.getCustomer() != null) {
                        map.put("BiggerCustomer", bigger.getCustomer().getName());
                    } else {
                        map.put("BiggerCustomer", " - ");
                    }

                    map.put("LowerNumber", Formatter.formatStringCode(lower.getId()));
                    map.put("LowerDate", Formatter.formatDate(lower.getDate()));
                    map.put("LowerUser", lower.getAccess().getName());
                    map.put("LowerValue", Formatter.formatMoney(lower.getValue()));
                    map.put("LowerDiscount", Formatter.formatMoney(lower.getDiscount()));
                    if (lower.getCustomer() != null) {
                        map.put("LowerCustomer", lower.getCustomer().getName());
                    } else {
                        map.put("LowerCustomer", " - ");
                    }
                } else {
                    map.put("AvgTicket", Formatter.formatMoney(0));

                    map.put("BiggerNumber", " - ");
                    map.put("BiggerDate", " - ");
                    map.put("BiggerUser", " - ");
                    map.put("BiggerValue", " - ");
                    map.put("BiggerDiscount", " - ");
                    map.put("BiggerCustomer", " - ");

                    map.put("LowerNumber", " - ");
                    map.put("LowerDate", " - ");
                    map.put("LowerUser", " - ");
                    map.put("LowerValue", " - ");
                    map.put("LowerDiscount", " - ");
                    map.put("LowerCustomer", " - ");
                }

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

    public static Function reportItem(long from, long to) {
        return () -> {
            List<Sale> sales = SaleModel.getAll(" WHERE date >= " + from + " AND " +
                    "date <= " + to + " ORDER BY date ASC");

            try {
                Map<String, Object> map = new HashMap<>();
                map.put("InitialDate", Formatter.formatDate(new Date(from)));
                map.put("FinalDate", Formatter.formatDate(new Date(to)));

                File fileImage = new File(GenerateFunction.class.getResource("/view/img/logoGrande.png").getFile());
                BufferedImage image = ImageIO.read(fileImage);
                map.put("Image", image);

                double total = 0;
                int quantityItem = 0;
                for (Sale sale : sales) {
                    total += sale.getValue();

                    for (SaleItem saleItem : sale.getSaleItems()) {
                        quantityItem += saleItem.getQuantity();
                    }
                }
                double avgItemValue;
                if(quantityItem == 0){
                    avgItemValue = 0;
                } else {
                    avgItemValue = total / quantityItem;
                }

                map.put("TotalValue", Formatter.formatMoney(total));
                map.put("TotalQuantity", quantityItem);
                map.put("AvgValue", Formatter.formatMoney(avgItemValue));

                List<QueryReportItem> reportItems = SaleItemModel.getReportItem(from, to);
                Object[] reportProduct = SaleItemModel.getReportProduct(from, to);
                map.put("ProductTotalValue", Formatter.formatMoney((Double) reportProduct[0]));
                map.put("ProductCost", Formatter.formatMoney((Double) reportProduct[1]));
                if((long)reportProduct[2] != 0) {
                    map.put("ProductQuantity", Math.toIntExact((long) reportProduct[2]));
                    map.put("ProductAvgPrice", Formatter.formatMoney(
                            (Double) reportProduct[0] / Math.toIntExact((long) reportProduct[2])
                    ));
                } else {
                    map.put("ProductQuantity", 0);
                    map.put("ProductAvgPrice", Formatter.formatMoney(0));
                }

                Object[] reportService = SaleItemModel.getReportService(from, to);
                map.put("ServiceTotalValue", Formatter.formatMoney((Double) reportService[0]));
                map.put("ServiceTime", Formatter.formatMoney((Double) reportService[1]));
                if((long)reportService[2] != 0) {
                    map.put("ServiceQuantity", Math.toIntExact((long) reportService[2]));
                    map.put("ServiceAvgPrice", Formatter.formatMoney(
                            (Double) reportProduct[0] / Math.toIntExact((long) reportProduct[2])
                    ));
                } else {
                    map.put("ServiceQuantity", 0);
                    map.put("ServiceAvgPrice", Formatter.formatMoney(0));
                }

                System.out.println("CRIOU");
                JasperPrint print = JasperFillManager.fillReport(
                        GenerateFunction.class.getResourceAsStream("/print/item.jasper"),
                        map, new JRBeanCollectionDataSource(reportItems));
                System.out.println("SALVOU");
                File dir = new File("C:/Domus");
                if(!dir.exists()) dir.mkdir();
                JasperExportManager.exportReportToPdfFile(print, "C:/Domus/item.pdf");
                Desktop.getDesktop().open(new File("C:\\Domus\\item.pdf"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public static Function reportFinancial(long from, long to) {
        return () -> {
            List<CashMovement> cashMovements = CashMovementModel.getAll(" WHERE date >= " + from + " AND " +
                    "date <= " + to + " ORDER BY date ASC");

            try {
                Map<String, Object> map = new HashMap<>();
                map.put("InitialDate", Formatter.formatDate(new Date(from)));
                map.put("FinalDate", Formatter.formatDate(new Date(to)));

                File fileImage = new File(GenerateFunction.class.getResource("/view/img/logoGrande.png").getFile());
                BufferedImage image = ImageIO.read(fileImage);
                map.put("Image", image);

                int quantityCash = 0;
                int quantitySale = 0;
                double paymentMoney = 0;
                double paymentCredit = 0;
                double paymentDebit = 0;
                double paymentTotal = 0;
                double totalCost = 0;
                double financialInflows = 0;
                double financialOutflows = 0;
                double resultTotal = 0;

                for (CashMovement cashMovement : cashMovements) {
                    quantityCash ++;

                    for (Sale sale : cashMovement.getSales()) {
                        if(sale.isActive()) {
                            quantitySale++;

                            for (SaleItem saleItem : sale.getSaleItems()) {
                                if (saleItem.getCost() != null) {
                                    totalCost += saleItem.getCost();
                                }
                            }
                        }
                    }

                    for (FinancialInflow financialInflow : cashMovement.getFinancialInflows()) {
                        if(financialInflow.getSale() != null){
                            if(financialInflow.getSale().isActive()) {
                                Sale sale = financialInflow.getSale();
                                switch (financialInflow.getPaymentMethod().getId()) {

                                    case 1:
                                        double payment = 0;
                                        if(sale.getFinancialInflows().size() > 1) {
                                            for (FinancialInflow inflow : sale.getFinancialInflows()) {
                                                payment += inflow.getValue();
                                            }
                                            paymentMoney += financialInflow.getValue() - (payment - sale.getValue());
                                        } else {
                                            paymentMoney += sale.getValue();
                                        }
                                        break;

                                    case 2:
                                        if(sale.getFinancialInflows().size() > 1) {
                                            paymentCredit += financialInflow.getValue();
                                        } else {
                                            paymentCredit += sale.getValue();
                                        }
                                        break;

                                    case 3:
                                        if(sale.getFinancialInflows().size() > 1) {
                                            paymentDebit += financialInflow.getValue();
                                        } else {
                                            paymentDebit += sale.getValue();
                                        }
                                        break;
                                }
                            }
                        } else {
                            financialInflows += financialInflow.getValue();
                        }
                    }

                    for (FinancialOutflow financialOutflow : cashMovement.getFinancialOutflows()) {
                        financialOutflows += financialOutflow.getValue();
                    }

                }
                paymentTotal = paymentMoney + paymentCredit + paymentDebit;
                resultTotal = paymentTotal - totalCost + financialInflows - financialOutflows;

                map.put("QuantityCash", quantityCash);
                map.put("QuantitySale", quantitySale);
                map.put("PaymentMoney", Formatter.formatMoney(paymentMoney));
                map.put("PaymentCredit", Formatter.formatMoney(paymentCredit));
                map.put("PaymentDebit", Formatter.formatMoney(paymentDebit));
                map.put("PaymentTotal", Formatter.formatMoney(paymentTotal));
                map.put("TotalCost", Formatter.formatMoney(totalCost));
                map.put("FinancialInflows", Formatter.formatMoney(financialInflows));
                map.put("FinancialOutflows", Formatter.formatMoney(financialOutflows));
                if(resultTotal >= 0) {
                    map.put("ResultTotal", Formatter.formatMoney(resultTotal));
                } else {
                    map.put("ResultTotal", " - " + Formatter.formatMoney(resultTotal));
                }

                System.out.println("CRIOU");
                JasperPrint print = JasperFillManager.fillReport(
                        GenerateFunction.class.getResourceAsStream("/print/financial.jasper"),
                        map, new JREmptyDataSource());
                System.out.println("SALVOU");
                File dir = new File("C:/Domus");
                if(!dir.exists()) dir.mkdir();
                JasperExportManager.exportReportToPdfFile(print, "C:/Domus/financial.pdf");
                Desktop.getDesktop().open(new File("C:\\Domus\\financial.pdf"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public static Function reportAnimalHistory(Animal animal) {
        return () -> {
            List<Recipe> recipes = RecipeModel.getAll("WHERE animal = " + animal.getId() + " ORDER BY date DESC");

            try {
                Map<String, Object> map = new HashMap<>();
                File fileImage = new File(GenerateFunction.class.getResource("/view/img/logoGrande.png").getFile());
                BufferedImage image = ImageIO.read(fileImage);
                map.put("Image", image);

                map.put("AnimalName", animal.getName());
                map.put("AnimalSpecie", animal.getSpecie());
                map.put("AnimalCustomer", animal.getCustomer().getName());


                List<RecipeItemField> recipeItemFields = new ArrayList<>();
                for (Recipe recipe : recipes) {
                    String user = recipe.getUser().getUser();
                    String date = Formatter.formatDate(recipe.getDate());
                    String description;

                    for (RecipeItem recipeItem : recipe.getRecipeItems()) {
                        description = recipeItem.getName() + " " + recipeItem.getQuantity() + " " +
                                recipeItem.getQuantityUnity() + " " + recipeItem.getTime() + " VEZ(ES) A CADA " +
                                recipeItem.getTimeMetric() + " " + recipeItem.getTimeUnity();

                        recipeItemFields.add(new RecipeItemField(user, date, description));
                    }
                }

                System.out.println("CRIOU");
                JasperPrint print = JasperFillManager.fillReport(
                        GenerateFunction.class.getResourceAsStream("/print/animalHistory.jasper"),
                        map, new JRBeanCollectionDataSource(recipeItemFields));
                System.out.println("SALVOU");
                File dir = new File("C:/Domus");
                if(!dir.exists()) dir.mkdir();
                JasperExportManager.exportReportToPdfFile(print, "C:/Domus/animalHistory.pdf");
                Desktop.getDesktop().open(new File("C:\\Domus\\animalHistory.pdf"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}
