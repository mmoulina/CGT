package com.poplicus.crawler.codegen.gui.components;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.Icon;
import javax.swing.UIManager;

public class PopNodeIcon implements Icon {

    private static final int SIZE = 9;

    private String type = null;

    public PopNodeIcon(String type) {
        this.type = type;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
    	Polygon polygon = null;
        g.setColor(UIManager.getColor("Tree.hash").darker());
        
        if(type.equalsIgnoreCase("collapse")) {
        	y = y - 3;
        	polygon = new Polygon(new int[]{x, (x + 7), x}, new int[]{y, (y + 8), (y + 15)}, 3);
        	//polygon = new Polygon(new int[]{x, (x + 11), x}, new int[]{y, (y + 7), (y + 14)}, 3);
        } else if(type.equalsIgnoreCase("expand")) {
        	polygon = new Polygon(new int[]{x, (x + 12), (x + 6)}, new int[]{y, y, (y + 10)}, 3);
        }
        g.fillPolygon(polygon);
    }

    public int getIconWidth() {
        return SIZE;
    }

    public int getIconHeight() {
        return SIZE;
    }
}
