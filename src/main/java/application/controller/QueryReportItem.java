package application.controller;

import application.controller.object.Product;
import application.controller.object.Service;
import application.model.ProductModel;
import application.model.ServiceModel;
import application.view.auxiliary.Formatter;

public class QueryReportItem {

    private String type;
    private String code;
    private String name;
    private String price;
    private Integer quantity;
    private String costTime;


    public QueryReportItem(Object[] object) {
        if ((boolean) object[3]) {
            this.type = "Produto";
            Product product = ProductModel.get("WHERE id = " + object[1]);
            this.name = product.getName();
            this.price = Formatter.formatMoney(product.getPrice());
            this.costTime = Formatter.formatMoney(product.getCost());
        } else {
            this.type = "Serviço";
            Service service = ServiceModel.get("WHERE id = " + object[0]);
            this.name = service.getName();
            this.price = Formatter.formatMoney(service.getPrice());
            this.costTime = service.getTime() + " min";
        }
        this.quantity = (int) object[2];
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCostTime() {
        return costTime;
    }

    public void setCostTime(String costTime) {
        this.costTime = costTime;
    }
}
