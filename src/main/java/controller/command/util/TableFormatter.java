package controller.command.util;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.List;
import java.util.Set;

public class TableFormatter {
    private final Set<String> columns;
    private final Table table;
    private final List<String> tableData;

    public TableFormatter(final Set<String> columns, final List<String> tableData) {
        this.columns = columns;
        this.tableData = tableData;
        table = new Table(columns.size(), BorderStyle.CLASSIC, ShownBorders.SURROUND_HEADER_AND_COLUMNS);
    }

    public String getTableString() {
        build();
        return table.render();
    }

    private void build() {
        buildHeader();
        buildRows();
    }

    private void buildHeader() {
        for (String column : columns) {
            table.addCell(column);
        }
    }

    private void buildRows() {
        for (Object data : tableData) {
            table.addCell(data.toString());
        }
    }
}
