package application.model;

import application.controller.QueryReportItem;
import application.controller.object.SaleItem;
import org.hibernate.type.BooleanType;
import org.hibernate.type.LongType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class SaleItemModel {

    public static List<SaleItem> getAll(String search){
        return GenericModel.getAll("FROM SaleItem " + search);
    }

    public static List<QueryReportItem> getReportItem(long from, long to){
//        return GenericModel.getAll(
//                "SELECT si.service, si.product, sum(si.quantity) as quantity, si.service IS NULL as typeProduct " +
//                "FROM SaleItem si " +
//                "JOIN Sale s " +
//                "ON si.sale = s.id " +
//                "WHERE s.date >= '" + from + "' AND s.date <= '" + to + "' " +
//                "GROUP BY si.service, si.product " +
//                "ORDER BY quantity DESC");
        List<Object[]> list =  HibernateUtilities.getSession().createSQLQuery(
                "SELECT si.service_id as service, si.product_id as product, sum(si.quantity) as quantity, si.service_id IS NULL as typeProduct " +
                        "FROM sales_items si " +
                        "JOIN sales s " +
                        "ON si.sale_id = s.id " +
                        "WHERE s.date >= '" + from + "' AND s.date <= '" + to + "' " +
                        "GROUP BY si.service_id, si.product_id " +
                        "ORDER BY quantity DESC LIMIT 5")
                .addScalar("service" , LongType.INSTANCE)
                .addScalar("product", LongType.INSTANCE)
                .addScalar("quantity", LongType.INSTANCE)
                .addScalar("typeProduct", BooleanType.INSTANCE)
                .list();

        List<QueryReportItem> reportItems = new ArrayList<>();
        for (Object[] objects : list) {
            reportItems.add(new QueryReportItem(objects));
        }

        return reportItems;
    }
}
