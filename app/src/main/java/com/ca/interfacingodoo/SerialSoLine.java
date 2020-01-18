package com.ca.interfacingodoo;

import java.io.Serializable;
import java.util.List;

public class SerialSoLine implements Serializable {
    private List<SalesOrderLine> salesOrderLineList;

    public SerialSoLine(List<SalesOrderLine> salesOrderLineList) {
        this.salesOrderLineList = salesOrderLineList;
    }

    public List<SalesOrderLine> getSalesOrderLineList() {
        return salesOrderLineList;
    }

    public void setSalesOrderLineList(List<SalesOrderLine> salesOrderLineList) {
        this.salesOrderLineList = salesOrderLineList;
    }
}
