package spotmetrics.ui;

import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: 6:22:19 PM - Sep 29, 2014
 * 
 */
public class SpotTreeCellRenderer extends DefaultTreeCellRenderer {

        private static final long serialVersionUID = -6406510049537561959L;

        public SpotTreeCellRenderer() {

        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
                return c;
        }
}
