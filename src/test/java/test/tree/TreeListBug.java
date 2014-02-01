/*
 * JTree tree = new JTree(new EventTreeModel(treeList));

        JPanel panel = new JPanel();
        panel.add(tree);
        JDialog dialog = new JDialog();
        dialog.add(panel);
        dialog.setVisible(true);
 */
package test.tree;
import java.util.Comparator;
import java.util.List;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TreeList;
import ca.odell.glazedlists.TreeList.Format;
import ca.odell.glazedlists.swing.EventTreeModel;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTree;

public class TreeListBug {

    private TreeList<Item> treeList;

    public TreeListBug() {
        // prepare the source list
        final EventList<Item> eventList = new BasicEventList<Item>();
        Item sect1 = new Item(null, "code1", "name1");
        Item sect11 = new Item(sect1, "code11", "name11");
        Item sect12 = new Item(sect1, "code12", "name12");
        final Item sect2 = new Item(null, "code2", "name2");
        Item sect21 = new Item(sect2, "code21", "name21");
        Item sect22 = new Item(sect2, "code22", "name22");
        eventList.add(sect1);
        eventList.add(sect2);
        eventList.add(sect11);
        eventList.add(sect12);
        eventList.add(sect21);
        eventList.add(sect22);

        // A trivial tree format - defined by item.parent
        Format format = new Format<Item>() {
            public boolean allowsChildren(Item element) { return true; }
            public Comparator<? super Item> getComparator(int depth) {
return null; }
            public void getPath(List<Item> path, Item item) {
                Item parent = item.parent;
                while (parent != null) {
                    path.add(0, parent);
                    parent = parent.parent;
                }
                path.add(item);
            }
            
        };


        // Build the treeList from the onlyLeaves list
        
        treeList = new TreeList<Item>(eventList, format,
TreeList.NODES_START_EXPANDED);
        

        System.err.println("eventList="+eventList);
        System.err.println("treeList="+treeList);

        System.err.println("refreshing item 3 in treeList...");

        Item item = treeList.get(3);
        treeList.set(3, item);

        System.err.println("eventList="+eventList);
        System.err.println("treeList="+treeList);
        
        JTree tree = new JTree(new EventTreeModel(treeList));

        JPanel panel = new JPanel();
        panel.add(tree);
        JDialog dialog = new JDialog();
        dialog.add(panel);
        dialog.setVisible(true);
    }


    public static class Item {
        public String code;
        public String name;
        public Item parent;

        public Item(Item parent, String code, String name) {
            this.parent = parent;
            this.code = code;
            this.name = name;
        }
        public String toString() { return code+"/"+name; }
    }

    public static void main(String[] args) {
        new TreeListBug();
    }
}
