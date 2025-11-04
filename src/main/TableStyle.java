/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Nat
 */
public class TableStyle {

    public static FontSetter fs = new FontSetter();

    public static void styleTable(JTable table) {
        table.setAutoCreateRowSorter(true);

        // Create a custom header renderer that works with sorting
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                // Handle HTML content
                String text = value != null ? value.toString() : "";
                if (!text.startsWith("<html>")) {
                    text = "<html>" + text + "</html>";
                }
                label.setText(text);

                // Apply styling
//                label.setFont(fs.tableHeaderFont());
                label.setBackground(new Color(128, 128, 128)); // grey
                label.setForeground(new Color(0, 0, 0)); //  black
                label.setHorizontalAlignment(SwingConstants.CENTER);

                // Add sort indicator space
                if (table.getRowSorter() != null) {
                    label.setText(label.getText() + "   "); // Space for sort indicator
                }
                return label;
            }
        };

        // Table body styling
        table.setRowHeight(40);
//        table.setFont(fs.tableTextFont());

        // Cell padding
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15));
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
    }
}
