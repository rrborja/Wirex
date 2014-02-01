/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.tree;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TreeList;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.EventTreeModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;
import ca.odell.glazedlists.swing.TreeTableSupport;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class MixedTypesTreeTableExample {

    public static enum Continent {NorthAmerica, SouthAmerica, Europe, Africa, Asia, Anarctica, Australia}
    public static enum Country {USA, Germany, Australia, Canada, England}

    /**
     * Location is an example of a Bean with many disparate types represented
     * by its properties: 2 enums, a String and an Integer.
     */
    public static final class Location {
        private final Continent continent;
        private final Country country;
        private final String city;
        private final Integer population;

        public Location(Continent continent, Country country, String city, Integer population) {
            this.continent = continent;
            this.country = country;
            this.city = city;
            this.population = population;
        }

        public Continent getContinent() { return continent; }
        public Country getCountry() { return country; }
        public String getCity() { return city; }
        public Integer getPopulation() { return population; }
    }

    /**
     * This TreeNode combines a regular Location object with an explicit value
     * to be used in the hierarchy column. For leaf nodes in the tree, the
     * Location object will be present and will match the corresponding element
     * of the source node. For synthetic nodes that represent a parent node in
     * the tree, the Location object will be null and only the hierarchyValue
     * will be present.
     */
    public static class LocationTreeNode {
        private final Location location;
        private final String hierarchyValue;

        public LocationTreeNode(Location location, String hierarchyValue) {
            this.location = location;
            this.hierarchyValue = hierarchyValue;
        }

        public Comparable getHierarchyValue() { return hierarchyValue; }
        public Continent getContinent() { return location == null ? null : location.getContinent(); }
        public Country getCountry() { return location == null ? null : location.getCountry(); }
        public String getCity() { return location == null ? null : location.getCity(); }
        public Integer getPopulation() { return location == null ? null : location.getPopulation(); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // create some sample data
                final EventList<Location> locations = new BasicEventList<Location>(10);
                locations.add(new Location(Continent.NorthAmerica, Country.USA, "Portland", 562690));
                locations.add(new Location(Continent.Europe, Country.Germany, "Dresden", 504635));
                locations.add(new Location(Continent.NorthAmerica, Country.USA, "San Jose", 929936));
                locations.add(new Location(Continent.Australia, Country.Australia, "Brisbane", 958504));
                locations.add(new Location(Continent.NorthAmerica, Country.Canada, "Regina", 179246));
                locations.add(new Location(Continent.Australia, Country.Australia, "Perth", 1507900));
                locations.add(new Location(Continent.NorthAmerica, Country.Canada, "Esterhazy", 2602));
                locations.add(new Location(Continent.Australia, Country.Australia, "Sydney", 4119190));
                locations.add(new Location(Continent.Europe, Country.England, "Brighton & Hove", 155919));

                // transform the Location -> LocationTreeNode (which provides an independent "hierarchyValue")
                final FunctionList.Function<Location, LocationTreeNode> treeNodeFunction = new FunctionList.Function<Location, LocationTreeNode>() {
                    public LocationTreeNode evaluate(Location sourceValue) {
                        return new LocationTreeNode(sourceValue, String.valueOf(sourceValue.getCity()));
                    }
                };
                final EventList<LocationTreeNode> treeNodes = new FunctionList<Location, LocationTreeNode>(locations, treeNodeFunction);

                // transfer all ListEvents onto the EDT
                final EventList<LocationTreeNode> swingThreadProxyList = GlazedListsSwing.swingThreadProxyList(treeNodes);

                // generate a tree from the base list of LocationTreeNode
                final TreeList<LocationTreeNode> locationTree = new TreeList<LocationTreeNode>(swingThreadProxyList, new LocationTreeFormat(), TreeList.NODES_START_EXPANDED);

                // built a flat table of LocationTreeNode
                final String[] labelNames = {"", "Continent", "Country", "City", "Population"};
                final String[] properties = {"hierarchyValue", "continent", "country", "city", "population"};
                final TableFormat<LocationTreeNode> locationTreeTableFormat = GlazedLists.tableFormat(properties, labelNames);
                final EventTableModel<LocationTreeNode> locationTreeTableModel = new EventTableModel<LocationTreeNode>(locationTree, locationTreeTableFormat);
                final JTable locationTreeTable = new JTable(locationTreeTableModel);

                JTree tree = new JTree(new EventTreeModel(locationTree));
                
                // now make the table a treetable
                TreeTableSupport.install(locationTreeTable, locationTree, 0);

                // show the treetable in a frame
                JFrame frame = new JFrame();
                frame.add(new JScrollPane(locationTreeTable), BorderLayout.CENTER);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    private static class LocationTreeFormat implements TreeList.Format {
        public void getPath(List path, Object element) {
            // first level of the tree is continents
            path.add(new LocationTreeNode(null, ((LocationTreeNode)element).getContinent().toString()));

            // second level of the tree is countries
            path.add(new LocationTreeNode(null, ((LocationTreeNode)element).getCountry().toString()));

            // third level of the tree is the raw LocationTreeNodes themselves
            path.add(element);
        }

        public boolean allowsChildren(Object element) {
            return true;
        }

        public Comparator getComparator(int depth) {
            // at all levels of the tree, we can simply group nodes based on their hierarchy value
            return GlazedLists.beanPropertyComparator(LocationTreeNode.class, "hierarchyValue");
        }
    }
}
